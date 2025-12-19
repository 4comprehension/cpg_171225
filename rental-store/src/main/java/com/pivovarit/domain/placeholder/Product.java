package com.pivovarit.domain.placeholder;

import java.time.Instant;

public record Product(long id, String name, long priceCents, boolean active, Instant createdAt) {
}
