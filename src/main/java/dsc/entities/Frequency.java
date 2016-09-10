package dsc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет частоту.
 *
 * Частота состоит из 3 символов.
 *
 * Пример: 0234,56 kHz будет представлена как 02 34 56.
 *
 * TODO: Выяснить, в каком типе данных удобнее хранить и как представлять.
 *
 * @author AlexeyVorobyev
 */
public final class Frequency implements Transmittable {

    private final int value;

    public Frequency(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /** @see Transmittable#toCodes() */
    @Override
    public List<Code> toCodes() {
        List<Code> signs = new ArrayList<>();

        long f = value;

        int divider = 10000;
        for (int i = 0; i < 3; i++) {
            int temp = (int) (f / divider);
            f -= temp * divider;
            signs.add(new Code(temp));
            divider /= 100;
        }

        return signs;
    }

    public static Frequency fromCodes(List<Code> codes) {
        int frequency = codes.get(0).getSymbol() * 10000 +
            codes.get(1).getSymbol() * 100 + codes.get(2).getSymbol();

        frequency *= 100;

        return new Frequency(frequency);
    }

    public static List<Code> getNoInfoCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(new Code(126));
        codes.add(new Code(126));
        codes.add(new Code(126));

        return codes;
    }

    @Override
    public String toString() {
        return value + " " + "Hz";
    }
}
