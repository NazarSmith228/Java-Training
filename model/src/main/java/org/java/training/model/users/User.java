package org.java.training.model.users;

import lombok.Builder;

@Builder
public record User(int age, int rate, String name, Location location) {
}
