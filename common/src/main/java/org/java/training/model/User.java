package org.java.training.model;

import lombok.Builder;

@Builder
public record User(int age, int rate, String name, Location location) {
}
