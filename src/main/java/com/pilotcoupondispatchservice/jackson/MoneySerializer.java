package com.pilotcoupondispatchservice.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Writes a BigDecimal money field as a plain 2-decimal string (e.g. "1500.00"), never scientific
// notation and never null, so dashboard cards can render it directly.
public class MoneySerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        BigDecimal scaled = (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
        gen.writeString(scaled.toPlainString());
    }
}
