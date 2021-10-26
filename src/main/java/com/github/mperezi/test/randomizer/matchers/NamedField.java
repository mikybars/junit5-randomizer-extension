package com.github.mperezi.test.randomizer.matchers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Matches a {@link Field} based on its name and type.
 *
 * <ul>
 * <li>The list of allowed names should be given upon creation.</li>
 * <li>The list of allowed types should be defined by each subclass.</li>
 * </ul>
 *
 * @see StringField
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public abstract class NamedField implements Predicate<Field> {

    private final Predicate<Field> nameMatcher;

    NamedField(final String... names) {
        if (names.length == 0) {
            throw new IllegalArgumentException("Field number must be greater than 0");
        }
        this.nameMatcher = buildNameMatcher(names);
    }

    private static Predicate<Field> buildNameMatcher(final String[] names) {
        return Stream.of(names)
            .map(toFieldMatcher())
            .reduce(field -> false, Predicate::or);
    }

    private static Function<String, Predicate<Field>> toFieldMatcher() {
        return name -> isQualified(name) ? matchQualified(name) : matchNonQualified(name);
    }

    private static boolean isQualified(final String name) {
        return name.contains(".");
    }

    private static Predicate<Field> matchQualified(final String name) {
        return field -> qualifiedName(field).equalsIgnoreCase(name);
    }

    private static Predicate<Field> matchNonQualified(final String name) {
        return field -> name.equalsIgnoreCase(field.getName());
    }

    private static String qualifiedName(final Field field) {
        return String.format("%s.%s", field.getDeclaringClass().getSimpleName(), field.getName());
    }

    /**
     * Return true if the given field matches one of the names <b>and</b> one of the types.
     * @param f the {@link Field} to be matched
     * @return {@code true} if the given field matches the name and type
     * @see #getTypes()
     */
    @Override
    public boolean test(final Field f) {
        return Arrays.asList(this.getTypes()).contains(f.getType()) && this.nameMatcher.test(f);
    }

    /**
     * Define the list of allowed types that a {@link Field} must have in order to match the criteria.
     * @return a list with one or more types
     */
    abstract Class<?>[] getTypes();

}
