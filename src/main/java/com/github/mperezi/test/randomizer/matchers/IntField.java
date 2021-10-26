package com.github.mperezi.test.randomizer.matchers;

/**
 * Matches fields of integer type.
 *
 * <p>
 * Allowed types:
 * <ul>
 * <li>{@link Integer}</li>
 * <li>int</li>
 * </ul>
 * </p>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public final class IntField extends NamedField {

    private static final Class[] INT_CLASSES = { Integer.class, int.class };

    private IntField(final String... names) {
        super(names);
    }

    /**
     * Factory to create a integer matcher for a list of given names.
     * @param names the list of allowed names (must match at least one)
     * @return a new integer matcher
     */
    public static IntField named(final String... names) {
        return new IntField(names);
    }

    @Override
    Class<?>[] getTypes() {
        return INT_CLASSES;
    }

}
