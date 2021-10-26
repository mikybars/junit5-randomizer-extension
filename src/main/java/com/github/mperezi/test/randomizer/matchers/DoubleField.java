package com.github.mperezi.test.randomizer.matchers;

/**
 * Matches fields of double type.
 *
 * <p>
 * Allowed types:
 * <ul>
 * <li>{@link Double}</li>
 * <li>double</li>
 * </ul>
 * </p>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public final class DoubleField extends NamedField {

    private static final Class[] DOUBLE_CLASSES = { Double.class, double.class };

    private DoubleField(final String... names) {
        super(names);
    }

    /**
     * Factory to create a double matcher for a list of given names.
     * @param names the list of allowed names (must match at least one)
     * @return a new double matcher
     */
    public static DoubleField named(final String... names) {
        return new DoubleField(names);
    }

    @Override
    Class<?>[] getTypes() {
        return DOUBLE_CLASSES;
    }

}
