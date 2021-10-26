package com.github.mperezi.test.randomizer;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

/**
 * Create objects with random data based on reflection for testing purposes.
 *
 * <p>
 * Most useful when the bulk of the properties in your entity is irrelevant to the test but still
 * want non-null values.
 * </p>
 *
 * <p>
 * This extension is powered by JUnit Jupiter's method parameters. Example usage:
 * </p>
 *
 * <pre class="code">
 * <code class="java">
 * <b>&#064;ExtendWith(RandomizerExtension.class)</b>
 * public class ExampleTest {
 *
 *   &#064;Random
 *   private MyObject randomField;
 *
 *   &#064;Test
 *   public void shouldDoSomethingWhenStatusIsUnknown(&#064;Random MyObject randomParam) {
 *     // set only the data relevant to this test
 *     randomParam.setStatus(Status.UNKNOWN);
 *   }
 * }
 * </code>
 * </pre>
 *
 * You can also customize the randomizers that populate certain fields. This is particularly
 * convenient in mapper tests where some fields are typed different than the data they actually
 * represent:
 *
 * <pre class="code">
 * <code class="java">
 * <b>&#064;ExtendWith(RandomizerExtension.class)</b>
 * public class ExampleTest implements CustomRandomFieldProvider {
 *
 *   &#064;Override
 *   public void registerCustomRandomizers(CustomRandomizerRegistry registry) {
 *     // submissionDate should never hold values other than valid dates
 *     registry.registerRandomizer(
 *       StringField.named("submissionDate"),
 *       StringRandomizerFactory.ofInstant());
 *   }
 * }
 * </code>
 * </pre>
 *
 * Collection types (more explicitly {@link List} and {@link Set}) are also supported. You just need
 * to provide the type of the elements because of the
 * <a href="https://docs.oracle.com/javase/tutorial/java/generics/erasure.html">type erasure</a>
 * feature of the Java language:
 *
 * <pre class="code">
 * <code class="java">
 * &#064;Test
 * public void myTest(&#064;Random(type=MyObject.class) List&lt;MyObject&gt; objects) {
 *
 * }
 * </code>
 * </pre>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 * @see Random
 * @see CustomRandomFieldProvider
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">ObjectMother pattern</a>
 */
public class RandomizerExtension implements ParameterResolver, TestInstancePostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RandomizerExtension.class);

    private static EasyRandom getEasyRandomForTestInstance(final ExtensionContext context) {
        final Object testInstance = context.getRequiredTestInstance();
        return context.getStore(Namespace.GLOBAL).get(testInstance, EasyRandom.class);
    }

    private static EasyRandom createEasyRandomForTestInstance(final Object testInstance) {
        final CustomRandomizerRegistry customRandomizerRegistry = new CustomRandomizerRegistry();

        registerDefaultRandomizers(customRandomizerRegistry);
        registerCustomRandomizersIfAny(customRandomizerRegistry, testInstance);

        return new EasyRandom(getDefaultParams().randomizerRegistry(customRandomizerRegistry));
    }

    private static void registerDefaultRandomizers(final CustomRandomizerRegistry registry) {
        registry.registerRandomizer(Long.class, new LongRangeRandomizer(1L, 1000L));
        registry.registerRandomizer(Integer.class, new IntegerRangeRandomizer(1, 1000));
    }

    private static void registerCustomRandomizersIfAny(final CustomRandomizerRegistry registry,
            final Object testInstance) {
        if (testInstance instanceof CustomRandomFieldProvider) {
            final CustomRandomFieldProvider customRandomFieldProvider = (CustomRandomFieldProvider) testInstance;
            customRandomFieldProvider.registerCustomRandomizers(registry);
        }
    }

    private static EasyRandomParameters getDefaultParams() {
        return new EasyRandomParameters()
            .objectPoolSize(100)
            .randomizationDepth(4)
            .charset(StandardCharsets.UTF_8)
            .dateRange(LocalDate.now().minusMonths(1), LocalDate.now())
            .stringLengthRange(5, 20)
            .collectionSizeRange(1, 10)
            .ignoreRandomizationErrors(true);
    }

    private static void attachEasyRandomToTestInstance(final EasyRandom easyRandom, final Object testInstance,
            final ExtensionContext extensionContext) {
        extensionContext.getStore(Namespace.GLOBAL).put(testInstance, easyRandom);
    }

    private static void randomizeFields(final Object testInstance, final EasyRandom easyRandom)
            throws IllegalAccessException {
        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            if (isAnnotated(field, Random.class)) {
                final Random annotation = field.getAnnotation(Random.class);
                try {
                    final Object randomObject = resolve(field.getType(), annotation, easyRandom);
                    field.setAccessible(true);
                    field.set(testInstance, randomObject);
                } catch (final CollectionNotTypedException e) {
                    logger.warn(() -> "Random field '" + field.getName()
                            + "' in " + field.getDeclaringClass()
                            + " is a collection but is missing a type parameter");
                }
            }
        }
    }

    private static Object resolve(final Class<?> targetType, final Random annotation, final EasyRandom easyRandom) {
        if (targetType.isAssignableFrom(List.class)) {
            failOnMissingType(annotation);
            return easyRandom.objects(annotation.type(), annotation.size()).collect(toList());
        } else if (targetType.isAssignableFrom(Set.class)) {
            failOnMissingType(annotation);
            return easyRandom.objects(annotation.type(), annotation.size()).collect(toSet());
        }

        return easyRandom.nextObject(targetType);
    }

    private static void failOnMissingType(final Random annotation) {
        if (annotation.type().equals(void.class)) {
            throw new CollectionNotTypedException();
        }
    }

    /**
     * Select for randomization only those method parameters annotated with {@link Random}.
     * @param parameterContext the context for the parameter for which an argument should be resolved;
     *        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked; never
     *        {@code null}
     */
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(Random.class);
    }

    /**
     * Create a random instance of the type given by the parameter context.
     * @param parameterContext the context for the parameter for which an argument should be resolved;
     *        never {@code null}
     * @param extensionContext the extension context for the Executable about to be invoked; never
     *        {@code null}
     * @return a random instance of the correct type
     */
    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Parameter param = parameterContext.getParameter();
        final Random annotation = param.getAnnotation(Random.class);
        final EasyRandom easyRandom = getEasyRandomForTestInstance(extensionContext);
        try {
            return resolve(param.getType(), annotation, easyRandom);
        } catch (final CollectionNotTypedException e) {
            logger.warn(() -> "Random parameter '" + param.getName()
                    + "' in method " + param.getDeclaringExecutable()
                    + " is a collection but is missing a type parameter");
            return null;
        }
    }

    /**
     * Register custom randomizers and inject random values into fields annotated with {@link Random}.
     * @param testInstance the instance to post-process
     * @param extensionContext the extension context for the Executable about to be invoked; never
     *        {@code null}
     */
    @Override
    public void postProcessTestInstance(final Object testInstance, final ExtensionContext extensionContext)
            throws IllegalAccessException {
        final EasyRandom easyRandom = createEasyRandomForTestInstance(testInstance);
        attachEasyRandomToTestInstance(easyRandom, testInstance, extensionContext);
        randomizeFields(testInstance, easyRandom);
    }

}
