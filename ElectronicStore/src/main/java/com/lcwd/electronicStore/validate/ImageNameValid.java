package com.lcwd.electronicStore.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface  ImageNameValid {

    //error
    String message() default "Invalid message!!";
   //group of constraint
    Class<?>[] groups() default {};
 // additional information
    Class<? extends Payload>[] payload() default {};

}
