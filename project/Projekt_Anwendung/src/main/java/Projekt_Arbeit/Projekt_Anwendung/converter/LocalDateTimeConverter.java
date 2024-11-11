package Projekt_Arbeit.Projekt_Anwendung.converter;

import jakarta.persistence.AttributeConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>
{
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime)
    {
        return Timestamp.valueOf(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp)
    {
        return timestamp.toLocalDateTime();
    }
}
