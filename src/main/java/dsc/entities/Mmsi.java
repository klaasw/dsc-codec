package dsc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Maritime Mobile Service Identity (MMSI).
 *
 * Состоит строго из 10 цифр.
 *
 * @author AlexeyVorobyev
 */
public final class Mmsi implements Address {

    /** Номер MMSI */
    private final long value;

    public Mmsi(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    /** @see Transmittable#toCodes() */
    @Override
    public List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        long tempMid = value;
        int divider = 10000000;
        for (int i = 0; i < 4; i++) {
            long temp = tempMid / divider;
            tempMid -= temp * divider;
            codes.add(new Code((int) temp));
            divider /= 100;
        }
        tempMid *= 10;

        codes.add(new Code((int) tempMid));

        return codes;
    }

    /**
     * Возвращает MMSI из символов.
     *
     * Как правило, MMSI кодируется 5 символами.
     *
     * @param codes символы
     * @return MMSI
     */
    public static Mmsi fromCodes(List<Code> codes) {
        long temp;

        temp = codes.get(0).getSymbol() * 100000000l;
        temp += codes.get(1).getSymbol() * 1000000l;
        temp += codes.get(2).getSymbol() * 10000l;
        temp += codes.get(3).getSymbol() * 100l;
        temp += codes.get(4).getSymbol();

        return new Mmsi(temp);
    }

    @Override
    public String toString() {
        return "Mmsi{" + value + '}';
    }
}
