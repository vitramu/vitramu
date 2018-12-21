package org.vitramu.engine.validator;

@FunctionalInterface
public interface Validator<T> {

    boolean validate(T t);
}
