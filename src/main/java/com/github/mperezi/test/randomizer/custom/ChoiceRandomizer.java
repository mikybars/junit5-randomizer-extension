package com.github.mperezi.test.randomizer.custom;

import org.jeasy.random.randomizers.AbstractRandomizer;

/**
 * A randomizer that picks one random value from a fixed list each time.
 *
 * For a randomizer that always generates the same constant value see
 * {@link org.jeasy.random.randomizers.misc.ConstantRandomizer}
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
public class ChoiceRandomizer<T> extends AbstractRandomizer<T> {

    private final T[] values;

    /**
     * Create a new ChoiceRandomizer with the given values as input.
     * @param values the list of values to choose from (must not be empty)
     */
    @SafeVarargs
    public ChoiceRandomizer(final T... values) {
        this.assertValidInputArgs(values);
        this.values = values;
    }

    public ChoiceRandomizer(final T[] values, final long seed) {
        super(seed);
        this.assertValidInputArgs(values);
        this.values = values;
    }

    @SafeVarargs
    public static <T> ChoiceRandomizer<T> aNewChoiceRandomizer(final T... values) {
        return new ChoiceRandomizer<>(values);
    }

    @Override
    public T getRandomValue() {
        final int randomIndex = this.random.nextInt(this.values.length);
        return this.values[randomIndex];
    }

    private void assertValidInputArgs(final T[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("input values must not be null nor empty");
        }
    }

}
