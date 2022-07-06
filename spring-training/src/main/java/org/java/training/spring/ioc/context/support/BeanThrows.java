package org.java.training.spring.ioc.context.support;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BeanThrows {

    @SuppressWarnings("rawtypes")
    List<Condition> conditions = new ArrayList<>();

    @Value
    private class Condition<T> {
        T arg;
        Predicate<? super T> predicate;
        Supplier<? extends RuntimeException> exceptionClass;

        private void throwIf() {
            if (predicate.test(arg)) {
                BeanThrows.this.conditions.clear();
                throw exceptionClass.get();
            }
        }
    }

    public <T> BeanThrows withCondition(T arg, Predicate<? super T> predicate,
                                        Supplier<? extends RuntimeException> exceptionSupplier) {
        requireNonNullAll(arg, predicate, exceptionSupplier);
        conditions.add(new Condition<>(arg, predicate, exceptionSupplier));
        return this;
    }

    public void throwIf() {
        if (conditions.isEmpty()) {
            throw new IllegalStateException("Cannot perform exception check. No conditions found");
        }
        conditions.forEach(Condition::throwIf);
        conditions.clear();
    }

    private void requireNonNullAll(Object... args) {
        for (var arg : args) {
            Objects.requireNonNull(arg);
        }
    }
}
