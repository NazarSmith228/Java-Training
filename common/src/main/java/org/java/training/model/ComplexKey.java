package org.java.training.model;

public record ComplexKey(String country, String city) {

    public static ComplexKey of(String country, String city) {
        return new ComplexKey(country, city);
    }
}
