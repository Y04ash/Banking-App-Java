package util;

import exceptions.ValidationExceptions;
@FunctionalInterface
public interface Validation<T> {
    void validate(T value) throws ValidationExceptions;
}
