package org.vitramu.master.validator;

@FunctionalInterface
public interface Validator<T> {

    boolean validate(T t);
}
