package com.github.mperezi.test.randomizer.custom;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChoiceRandomizerTest {

    @Test
    void shouldRandomizeWithAGivenChoice() {
        final ChoiceRandomizer<Integer> integerChoiceRandomizer = new ChoiceRandomizer<>(1, 3, 5, 7, 11, 13);

        final Integer randomValue = integerChoiceRandomizer.getRandomValue();

        assertThat(randomValue).isNotNull();
        assertThat(List.of(1, 3, 5, 7, 11, 13)).contains(randomValue);
    }

    @Test
    void shouldNotCreateRandomizerWithNoChoices() {
        assertThatThrownBy(() -> new ChoiceRandomizer<Integer>()).isInstanceOf(IllegalArgumentException.class);
    }

}
