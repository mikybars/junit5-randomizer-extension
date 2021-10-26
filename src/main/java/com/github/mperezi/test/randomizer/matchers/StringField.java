package com.github.mperezi.test.randomizer.matchers;

/**
 * Matches fields of String type.
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public final class StringField extends NamedField {

    private static final Class[] STRING_CLASSES = { String.class };

    private StringField(final String... names) {
        super(names);
    }

    /**
     * Factory to create a String matcher for a list of given names.
     * @param names the list of allowed names (must match at least one)
     * @return a new String matcher
     */
    public static StringField named(final String... names) {
        return new StringField(names);
    }

    @Override
    Class<?>[] getTypes() {
        return STRING_CLASSES;
    }

}
