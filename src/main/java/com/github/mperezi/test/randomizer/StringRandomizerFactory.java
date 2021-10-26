package com.github.mperezi.test.randomizer;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.misc.EnumRandomizer;
import org.jeasy.random.randomizers.range.DoubleRangeRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringDelegatingRandomizer;
import org.jeasy.random.randomizers.time.InstantRandomizer;
import org.jeasy.random.randomizers.time.LocalDateRandomizer;
import org.jeasy.random.randomizers.time.LocalDateTimeRandomizer;

/**
 * Factory class to create randomizers that output a String value from a different type.
 */
public final class StringRandomizerFactory {

    private StringRandomizerFactory() {

    }

    /**
     * Create a randomizer that outputs a String from a {@link Long} value in the given interval.
     * @param min min value
     * @param max max value
     * @return a new randomizer that transforms a random {@link Long} into a String
     */
    public static Randomizer<String> ofLongRange(final Long min, final Long max) {
        return new StringDelegatingRandomizer(new LongRangeRandomizer(min, max));
    }

    /**
     * Create a randomizer that outputs a String from a {@link Double} value in the given interval.
     * @param min min value
     * @param max max value
     * @return a new randomizer that transforms a random {@link Double} into a String
     */
    public static Randomizer<String> ofDoubleRange(final Double min, final Double max) {
        return new StringDelegatingRandomizer(new DoubleRangeRandomizer(min, max));
    }

    /**
     * Create a randomizer that outputs a String from an {@link Integer} value in the given interval.
     * @param min min value
     * @param max max value
     * @return a new randomizer that transforms a random {@link Integer} into a String
     */
    public static Randomizer<String> ofIntRange(final Integer min, final Integer max) {
        return new StringDelegatingRandomizer(new IntegerRangeRandomizer(min, max));
    }

    /**
     * Create a randomizer that outputs a String from an {@link java.time.Instant} value.
     * @return a new randomizer that transforms a random {@link java.time.Instant} into a String
     */
    public static Randomizer<String> ofInstant() {
        return new StringDelegatingRandomizer(new InstantRandomizer());
    }

    /**
     * Create a randomizer that outputs a String from a {@link java.time.LocalDate} value.
     * @return a new randomizer that transforms a random {@link java.time.LocalDate} into a String
     */
    public static Randomizer<String> ofLocalDate() {
        return new StringDelegatingRandomizer(new LocalDateRandomizer());
    }

    /**
     * Create a randomizer that outputs a String from a {@link java.time.LocalDateTime} value.
     * @return a new randomizer that transforms a random {@link java.time.LocalDateTime} into a String
     */
    public static Randomizer<String> ofLocalDateTime() {
        return new StringDelegatingRandomizer(new LocalDateTimeRandomizer());
    }

    /**
     * Create a randomizer that outputs a String from the name of an enum constant.
     * @param enumeration the enumeration from which this randomizer will generate random values
     * @return a new randomizer that transforms a random enum constant into a String
     */
    public static <E extends Enum<E>> Randomizer<String> ofEnum(final Class<E> enumeration) {
        return new StringDelegatingRandomizer(new EnumRandomizer<>(enumeration));
    }

}
