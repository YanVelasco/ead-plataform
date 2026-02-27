package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscriptionDto(
        @NotNull(message = "userId cannot be null") UUID userId
) {
}