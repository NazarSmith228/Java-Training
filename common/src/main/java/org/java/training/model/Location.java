package org.java.training.model;

import lombok.Builder;

@Builder
public record Location(String country, String city, String street) {
}
