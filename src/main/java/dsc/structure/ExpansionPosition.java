package dsc.structure;

import dsc.entities.Code;
import dsc.entities.Transmittable;

import java.util.ArrayList;
import java.util.List;

/** @author AlexeyVorobyev */
public final class ExpansionPosition implements Transmittable {

    private final int expansionSpecifier = 100;

    private final String enhancedLatitude;

    private final String enhancedLongitude;

    public ExpansionPosition(String enhancedLatitude,
                             String enhancedLongitude) {
        this.enhancedLatitude = enhancedLatitude;
        this.enhancedLongitude = enhancedLongitude;
    }

    public int getExpansionSpecifier() {
        return expansionSpecifier;
    }

    public String getEnhancedLatitude() {
        return enhancedLatitude;
    }

    public String getEnhancedLongitude() {
        return enhancedLongitude;
    }

    public static ExpansionPosition fromCodes(List<Code> codes) {
        List<Code> latitudeCodes = codes.subList(0, 2);
        List<Code> longitudeCodes = codes.subList(2, 4);

        String latitude = latitudeCodes.stream().reduce(
            "",
            (c1, c2) -> c1 + c2.getSymbol(),
            (c1, c2) -> c1 + c2
        );

        String longitude = longitudeCodes.stream().reduce(
            "",
            (c1, c2) -> c1 + c2.getSymbol(),
            (c1, c2) -> c1 + c2
        );

        return new ExpansionPosition(latitude, longitude);
    }

    @Override
    public String toString() {
        return "ExpansionPosition{" +
            "expansionSpecifier=" + expansionSpecifier +
            ", enhancedLatitude='" + enhancedLatitude + '\'' +
            ", enhancedLongitude='" + enhancedLongitude + '\'' +
            '}';
    }

    /** @see Transmittable#toCodes()  */
    @Override
    public List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        // Спецификатор расширенной последовательности
        codes.add(new Code(100));

        codes.add(new Code(Integer.parseInt(
            enhancedLatitude.substring(0, 2)
        )));
        codes.add(new Code(Integer.parseInt(
            enhancedLatitude.substring(2, 4))
        ));

        codes.add(new Code(Integer.parseInt(
            enhancedLongitude.substring(0, 2)
        )));
        codes.add(new Code(Integer.parseInt(
            enhancedLongitude.substring(2, 4)
        )));

        return codes;
    }
}
