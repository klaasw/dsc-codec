package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum FormatSpecifier {

    GEOGRAPHICAL_AREA(new Code(102)),
    DISTRESS(new Code(112)),
    COMMON_INTEREST(new Code(114)),
    ALL_SHIPS(new Code(116)),
    INDIVIDUAL(new Code(120)),
    RESERVED(new Code(121)),
    IDIVIDUAL_SEMI_AUTOMATIC(new Code(123));

    private final Code code;

    FormatSpecifier(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static FormatSpecifier fromSymbol(int symbol) {
        for (FormatSpecifier f : values())
            if (f.getCode().getSymbol() == symbol)
                return f;

        throw new IllegalArgumentException("Failed to find format specified " +
            "for specified symbol: " + symbol);
    }

    @Override
    public String toString() {
        switch (this) {
            case GEOGRAPHICAL_AREA:
                return "Geographical area";
            case DISTRESS:
                return "Distress";
            case COMMON_INTEREST:
                return "Ships having common interest";
            case ALL_SHIPS:
                return "All ships";
            case INDIVIDUAL:
                return "Individual stations";
            case RESERVED:
                return "Reserved for national non-calling purposes e.g. " +
                    "Report ITU-R M.1159";
            case IDIVIDUAL_SEMI_AUTOMATIC:
                return "Individual station semi-automatic/automatic service";
            default:
                throw new IllegalArgumentException();
        }
    }
}
