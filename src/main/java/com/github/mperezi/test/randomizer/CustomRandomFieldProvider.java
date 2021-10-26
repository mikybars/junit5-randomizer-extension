package com.github.mperezi.test.randomizer;

import java.util.function.Predicate;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;

/**
 * Provide a test class with the ability to plug in its own randomizers.
 *
 * Example usage:
 *
 * <pre class="code">
 * <code class="java">
 * <b>&#064;ExtendWith(RandomizerExtension.class)</b>
 * public class ExampleTest implements CustomRandomFieldProvider {
 *
 *   &#064;Override
 *   public void registerCustomRandomizers(CustomRandomizerRegistry registry) {
 *     // only positive integers
 *     registry.registerRandomizer(Integer.class, new IntegerRangeRandomizer(1, 1000));
 *
 *     // enum constants as Strings
 *     registry.registerRandomizer(
 *       StringField.named("state"),
 *       StringRandomizerFactory.ofEnum(State.class)
 *     );
 *
 *     // constant fixed value
 *     registry.registerRandomizer(IntField.named("month"), ConstantRandomizer.aNewConstantRandomizer(3));
 *
 *     // constant fixed values
 *     registry.registerRandomizer(StringField.named("country"), ChoiceRandomizer.aNewChoiceRandomizer("en", "es", "fr", "de"));
 *   }
 * }
 * </code>
 * </pre>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 * @see Randomizer
 * @see StringRandomizerFactory
 */
public interface CustomRandomFieldProvider {

    /**
     * Register one or more randomizers.
     * @param registry registry of custom randomizers provided by the underlying implementation.
     * @see CustomRandomizerRegistry#registerRandomizer(Predicate, Randomizer)
     * @see CustomRandomizerRegistry#registerRandomizer(Class, Randomizer)
     */
    void registerCustomRandomizers(CustomRandomizerRegistry registry);

}
