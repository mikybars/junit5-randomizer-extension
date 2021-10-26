package com.github.mperezi.test.randomizer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark a field or a method parameter as random.
 *
 * @author Miguel Ibars (mperezibars@gmail.com)
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface Random {

    /**
     * Set the number of random objects to be created. Only applicable to collection types.
     */
    int size() default 10;

    /**
     * Specify the type of the elements in a collection. Only applicable to collection types.
     *
     * <p>
     * In case no type is given for a collection a warning message will be displayed and a null object
     * will be created.
     * </p>
     */
    Class<?> type() default void.class;

}
