package com.bing.water.common.validator.impl;

import com.google.common.collect.Sets;
import com.bing.water.common.validator.MemberOf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */
public class MemberOfValidator implements ConstraintValidator<MemberOf, String> {

    private Set<String> values;

    @Override
    public void initialize(MemberOf anno) {
        values = Sets.newHashSet(anno.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return values.contains(value);
    }
}
