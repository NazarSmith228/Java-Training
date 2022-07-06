package org.java.training.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.ObjectUtils.allNull;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default, it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}.
 * <p>
 * It allows null values for the fields, and it treats null value grater than a non-null value (nulls last).
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomFieldComparator<T> implements Comparator<T> {

    Class<? extends T> type;

    boolean compareOnlyAccessibleFields;

    Order order;

    Field random;

    public enum Order {

        ASCENDING,

        DESCENDING
    }

    public RandomFieldComparator(Class<? extends T> type) {
        this(type, false, Order.ASCENDING);
    }

    public RandomFieldComparator(Class<? extends T> type, boolean compareOnlyAccessibleFields, Order order) {
        this.type = type;
        this.compareOnlyAccessibleFields = compareOnlyAccessibleFields;
        this.order = order;
        this.random = Optional.ofNullable(initRandom())
                .orElseThrow(() -> new IllegalArgumentException("Unable to pick random field for comparison"));
    }

    private Field initRandom() {
        Field[] fieldsForComparing = pickFieldsForComparing(
                compareOnlyAccessibleFields ? type.getFields() : type.getDeclaredFields());

        Field random = null;
        if (isNotEmpty(fieldsForComparing)) {
            int index = ThreadLocalRandom.current().nextInt(fieldsForComparing.length);
            random = fieldsForComparing[index];

            if (!compareOnlyAccessibleFields) {
                random.setAccessible(true);
            }
        }
        return random;
    }

    //todo: implement possibility to pick nested object fields
    private Field[] pickFieldsForComparing(Field[] typeFields) {
        return Arrays.stream(typeFields)
                .filter(field -> isPrimitiveOrWrapper(field.getType()) ||
                        isAssignable(field.getType(), Comparable.class))
                .toArray(Field[]::new);
    }

    private boolean isAscending() {
        return order.equals(Order.ASCENDING);
    }

    @Override
    public String toString() {
        return String.format("Random field comparator of class [%s] is comparing [%s] in %s order",
                type.getSimpleName(), random.getName(), order.name());
    }

    @Override
    @SneakyThrows
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int compare(T o1, T o2) {
        Objects.requireNonNull(o1, "1st object is null - cannot compare");
        Objects.requireNonNull(o2, "2nd object is null - cannot compare");

        Comparable field1 = (Comparable) random.get(o1);
        Comparable field2 = (Comparable) random.get(o2);

        if (allNull(field1, field2)) {
            return 0;
        } else if (isNull(field1)) {
            return 1;
        } else if (isNull(field2)) {
            return -1;
        } else {
            return isAscending()
                    ? field1.compareTo(field2)
                    : field2.compareTo(field1);
        }
    }
}

