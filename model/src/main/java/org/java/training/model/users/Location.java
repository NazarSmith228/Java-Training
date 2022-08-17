package org.java.training.model.users;

import lombok.Builder;

@Builder
public record Location(String country, String city, String street) {
}
