package dsc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Целое число.
 *
 * @author AlexeyVorobyev
 */
public final class DscNumber implements Transmittable {

    private final long number;

    public DscNumber(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    /** @see Transmittable#toCodes()  */
    @Override
    public List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        String n = String.valueOf(number);

        for (int i = 0; i < n.length(); i += 2) {
            codes.add(new Code(Integer.parseInt(n.substring(i, i + 2))));
        }

        return codes;
    }

    public static DscNumber fromCodes(List<Code> codes) {
        StringBuffer buffer = new StringBuffer();

        for (Code c : codes) {
            buffer.append(String.valueOf(c.getSymbol()));
        }

        return new DscNumber(Long.parseLong(buffer.toString()));
    }

    @Override
    public String toString() {
        return "DscNumber{ number=" + number + '}';
    }
}
