package com.github.mperezi.test.randomizer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.jeasy.random.api.Randomizer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringRandomizerFactoryTest {

    private static Predicate<String> isParseableUsing(final Function<? super String, ?> parseFunc) {
        return s -> {
            try {
                parseFunc.apply(s);
                return true;
            } catch (final Exception e) {
                return false;
            }
        };
    }

    private static Condition<? super String> numericBetween(final int min, final int max) {
        final Predicate<Integer> withinRange = i -> i >= min && i < max;
        return new Condition<>(
                parseThenFilter(Integer::parseInt, withinRange),
                "a number between " + min + " and " + max);
    }

    private static Condition<? super String> numericBetween(final long min, final long max) {
        final Predicate<Long> withinRange = l -> l >= min && l < max;
        return new Condition<>(
                parseThenFilter(Long::parseLong, withinRange),
                "a number between " + min + " and " + max);
    }

    private static <R> Predicate<String> parseThenFilter(final Function<String, R> parseFunc,
            final Predicate<R> filter) {
        return s -> {
            try {
                final R r = parseFunc.apply(s);
                return filter.test(r);
            } catch (final Exception e) {
                return false;
            }
        };
    }

    enum MyEnum {

        CONST1, CONST2, CONST3

    }

    private Condition<? super String> numericBetween(final double min, final double max) {
        final Predicate<Double> withinRange = d -> d >= min && d < max;
        return new Condition<>(
                parseThenFilter(Double::parseDouble, withinRange),
                "a number between " + min + " and " + max);
    }

    private Condition<? super String> aValidInstant() {
        return new Condition<>(isParseableUsing(Instant::parse), "an Instant value");
    }

    private Condition<? super String> aValidLocalDate() {
        return new Condition<>(isParseableUsing(LocalDate::parse), "a LocalDate value");
    }

    private Condition<? super String> aValidLocalDateTime() {
        return new Condition<>(isParseableUsing(LocalDateTime::parse), "a LocalDateTime value");
    }

    @Test
    void ofIntRange() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofIntRange(10, 100);

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(numericBetween(10, 100));
    }

    @Test
    void ofLongRange() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofLongRange(10L, 100L);

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(numericBetween(10L, 100L));
    }

    @Test
    void ofDoubleRange() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofDoubleRange(10.0, 100.0);

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(this.numericBetween(10.0, 100.0));
    }

    @Test
    void ofInstant() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofInstant();

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(this.aValidInstant());
    }

    @Test
    void ofLocalDate() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofLocalDate();

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(this.aValidLocalDate());
    }

    @Test
    void ofLocalDateTime() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofLocalDateTime();

        final String randomValue = randomizer.getRandomValue();

        assertThat(randomValue).isNotNull().is(this.aValidLocalDateTime());
    }

    @Test
    void ofEnum() {
        final Randomizer<String> randomizer = StringRandomizerFactory.ofEnum(MyEnum.class);

        final String randomValue = randomizer.getRandomValue();

        final Stream<String> enumNames = Arrays.stream(MyEnum.values()).map(Enum::name);
        assertThat(randomValue).isNotNull();
        assertThat(enumNames).contains(randomValue);
    }

}
