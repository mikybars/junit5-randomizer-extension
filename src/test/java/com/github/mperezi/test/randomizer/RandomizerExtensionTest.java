package com.github.mperezi.test.randomizer;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomizerExtension.class)
class RandomizerExtensionTest {

    @Random
    private String stringField;

    static class Nested {

        Nested nested;

    }

    @Test
    void shouldCreateField() {
        assertThat(this.stringField).isNotNull();
    }

    @Test
    void shouldCreateMethodParam(@Random final String s) {
        assertThat(s).isNotNull();
    }

    @Test
    void shouldCreateList(@Random(type = String.class) final List<String> l) {
        assertThat(l).isNotNull().isNotEmpty();
    }

    @Test
    void shouldCreateSet(@Random(type = String.class) final Set<String> s) {
        assertThat(s).isNotNull().isNotEmpty();
    }

    @Test
    void shouldNotCreateCollectionWithoutTypeParameter(@Random final List<String> l) {
        assertThat(l).isNull();
    }

    @Test
    void shouldCreateCollectionWithGivenSize(@Random(type = String.class, size = 15) final List<String> l) {
        assertThat(l).isNotNull().hasSize(15);
    }

    @Test
    void shouldCreateNestedObjectsUpToDepth4(@Random final Nested nested) {
        assertThat(nested).as("depth 0").isNotNull();
        assertThat(nested.nested).as("depth 1").isNotNull();
        assertThat(nested.nested.nested).as("depth 2").isNotNull();
        assertThat(nested.nested.nested.nested).as("depth 3").isNotNull();
        assertThat(nested.nested.nested.nested.nested).as("depth 4").isNotNull();
        assertThat(nested.nested.nested.nested.nested.nested).as("depth 5").isNull();
    }

}
