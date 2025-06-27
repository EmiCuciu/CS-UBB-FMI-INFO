package com.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return String.valueOf(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String epochMilliString) {
        if (epochMilliString == null || epochMilliString.isEmpty()) {
            return null;
        }
        try {
            long epochMilli = Long.parseLong(epochMilliString);
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
        } catch (NumberFormatException e) {
            // In case it's already in standard ISO format, try parsing it directly
            return LocalDateTime.parse(epochMilliString);
        }
    }
}