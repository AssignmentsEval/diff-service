package com.limac.diffservice.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The field annotated must be base64 encoded.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$", message = "must be base64 encoded")
@NotBlank
@Constraint(validatedBy = { })
public @interface Base64 {

    /**
     * Sets the message to be sent when validation fails.
     * The default message is "must be base64 encoded".
     *
     * @return message.
     */
    String message() default "must be base64 encoded";

    /**
     * Sets groups parameters for validation.
     * The default groups is an empty array.
     *
     * @return groups
     */
    Class[] groups() default {};

    /**
     * Sets the payload for validation.
     * The default payload is an empty array.
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};
}

