package com.github.mperezi.test.randomizer;

import com.github.mperezi.test.randomizer.matchers.DateField;
import com.github.mperezi.test.randomizer.matchers.IntField;
import com.github.mperezi.test.randomizer.matchers.LongField;
import com.github.mperezi.test.randomizer.matchers.StringField;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.jeasy.random.randomizers.misc.ConstantRandomizer.aNewConstantRandomizer;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(RandomizerExtension.class)
class CustomRandomFieldProviderTest implements CustomRandomFieldProvider {

    public static final Instant RANDOM_INSTANT = Instant.ofEpochMilli(432324L);

    private static final Date RANDOM_DATE = new Date(432324L);

    private static final LocalDate RANDOM_LOCAL_DATE = LocalDate.ofEpochDay(432324L);

    private static final LocalDateTime RANDOM_LOCAL_DATETIME = LocalDateTime.ofEpochSecond(432324L, 0, ZoneOffset.UTC);

    static class MyObject {

        int intVar;

        long longVar;

        String stringVar;

        Instant instantVar;

        Date dateVar;

        LocalDate localDateVar;

        LocalDateTime localDateTimeVar;

    }

    @Override
    public void registerCustomRandomizers(final CustomRandomizerRegistry registry) {
        registry.registerRandomizer(IntField.named("intVar"), aNewConstantRandomizer(1234));
        registry.registerRandomizer(LongField.named("longVar"), aNewConstantRandomizer(5678L));
        registry.registerRandomizer(StringField.named("stringVar"), aNewConstantRandomizer("random"));
        registry.registerRandomizer(DateField.named("instantVar"), aNewConstantRandomizer(RANDOM_INSTANT));
        registry.registerRandomizer(DateField.named("dateVar"), aNewConstantRandomizer(RANDOM_DATE));
        registry.registerRandomizer(DateField.named("localDateVar"), aNewConstantRandomizer(RANDOM_LOCAL_DATE));
        registry.registerRandomizer(DateField.named("localDateTimeVar"), aNewConstantRandomizer(RANDOM_LOCAL_DATETIME));
    }

    @Test
    void shouldGiveCustomValuesToEveryField(@Random final MyObject myObject) {
        assertAll(
                () -> assertEquals("random", myObject.stringVar),
                () -> assertEquals(1234, myObject.intVar),
                () -> assertEquals(5678L, myObject.longVar),
                () -> assertEquals(RANDOM_INSTANT, myObject.instantVar),
                () -> assertEquals(RANDOM_DATE, myObject.dateVar),
                () -> assertEquals(RANDOM_LOCAL_DATE, myObject.localDateVar),
                () -> assertEquals(RANDOM_LOCAL_DATETIME, myObject.localDateTimeVar));
    }

}
