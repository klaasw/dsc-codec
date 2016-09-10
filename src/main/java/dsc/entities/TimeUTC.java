package dsc.entities;

import java.time.*;
import java.util.*;

/**
 * Класс-хелпер для представления времени в UTC и конвертаций.
 *
 * @author AlexeyVorobyev
 */
public final class TimeUTC implements Transmittable {

    private final Optional<ZonedDateTime> utcTime;

    /**
     * Создаёт контейнер для указанного времени по UTC
     *
     * @param utcTime время по UTC
     */
    public TimeUTC(ZonedDateTime utcTime) {
        this.utcTime = Optional.of(utcTime);
    }

    /** Создаёт заглушку для времени (часы и минуты равны 88) */
    public TimeUTC() {
        utcTime = Optional.empty();
    }

    public Optional<ZonedDateTime> getUtcTime() {
        return utcTime;
    }

    /** @see Transmittable#toCodes() */
    @Override
    public List<Code> toCodes() {
        List<Code> signs = new ArrayList<>();

        int hour = utcTime.isPresent() ? utcTime.get().getHour() : 88;
        int min = utcTime.isPresent() ? utcTime.get().getMinute() : 88;

        signs.add(new Code(hour));
        signs.add(new Code(min));

        return signs;
    }

    /**
     * Декодирует время по UTC.
     *
     * Для декодирования необходимо 2 символа (часы и минуты).
     *
     * @param codes коды для декодирования
     * @return время по UTC
     */
    public static TimeUTC fromCodes(List<Code> codes) {
        if (codes.size() < 2)
            throw new IllegalArgumentException("Для декодирования времени " +
                "необходимо 2 символа. Получено: " + codes.size());

        int hour = codes.get(0).getSymbol();
        int min = codes.get(1).getSymbol();

        // Если часы и минуты равны 88 - это означает, что время неизвестно
        if (hour == 88 || min == 88)
            return new TimeUTC();
        else
            return new TimeUTC(ZonedDateTime.of(
                LocalDate.now(), LocalTime.of(hour, min), ZoneOffset.UTC
            ));
    }

    /**
     * Возвращает текущее время по UTC
     *
     * @return текущее время по UTC
     */
    public static TimeUTC now() {
        return new TimeUTC(ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public String toString() {
        return "TimeUTC{" +
            "utcTime=" + (utcTime.isPresent() ? utcTime.get().getHour() +
            " h " + utcTime.get().getMinute() + " m" : utcTime) +
            '}';
    }
}
