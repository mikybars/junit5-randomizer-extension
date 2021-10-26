package com.github.mperezi.test.randomizer.matchers;

/**
 * Matches fields of Long type.
 *
 * <p>
 * Allowed types:
 * <ul>
 * <li>{@link Long}</li>
 * <li>long</li>
 * </ul>
 * </p>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public final class LongField extends NamedField {

    private static final Class[] LONG_CLASSES = { Long.class, long.class };

    private LongField(final String... names) {
        super(names);
    }

    /**
     * Factory to create a long matcher for a list of given names.
     * @param names the list of allowed names (must match at least one)
     * @return a new long matcher
     */
    public static LongField named(final String... names) {
        return new LongField(names);
    }

    @Override
    Class<?>[] getTypes() {
        return LONG_CLASSES;
    }

}
