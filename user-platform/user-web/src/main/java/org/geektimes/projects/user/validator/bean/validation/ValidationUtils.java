package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 校验工具类
 *
 */
public class ValidationUtils {

    private static Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj, Class... groups){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, groups);
        if( CollectionUtils.isNotEmpty(set) ){
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<>();
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static <T> ValidationResult validateProperty(T obj,String propertyName,Class... groups){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj,propertyName,groups);
        if( CollectionUtils.isNotEmpty(set) ){
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<>();
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }
}

