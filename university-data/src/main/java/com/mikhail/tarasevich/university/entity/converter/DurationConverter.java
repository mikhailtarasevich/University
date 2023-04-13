package com.mikhail.tarasevich.university.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Duration duration) {
        return (int) duration.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Integer duration) {
        return Duration.ofMinutes(duration);
    }

}
