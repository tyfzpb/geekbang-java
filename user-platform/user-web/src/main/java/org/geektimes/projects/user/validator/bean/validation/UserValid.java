package org.geektimes.projects.user.validator.bean.validation;

import org.geektimes.projects.user.validator.bean.validation.group.UpdateGroup;

import javax.validation.Constraint;
import javax.validation.GroupSequence;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidAnnotationValidator.class)
public @interface UserValid {

    int idRange() default 0;
}
