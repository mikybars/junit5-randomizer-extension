package com.github.mperezi.test.randomizer.matchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Matches fields of date type.
 *
 * <p>
 * Allowed types:
 * <ul>
 * <li>{@link Date}</li>
 * <li>{@link Instant}</li>
 * <li>{@link LocalDate}</li>
 * <li>{@link LocalDateTime}</li>
 * </ul>
 * </p>
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public final class DateField extends NamedField {

    private static final Class[] DATE_CLASSES = {
            Date.class, Instant.class, LocalDate.class, LocalDateTime.class };

    private DateField(final String... names) {
        super(names);
    }

    /**
     * Factory to create a date matcher for a list of given names.
     * @param names the list of allowed names (must match at least one)
     * @return a new date matcher
     */
    public static DateField named(final String... names) {
        return new DateField(names);
    }

    @Override
    Class<?>[] getTypes() {
        return DATE_CLASSES;
    }

}
