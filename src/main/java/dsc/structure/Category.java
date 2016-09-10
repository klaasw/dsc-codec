package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum Category {

    ROUTINE(new Code(100)),
    SAFETY(new Code(108)),
    URGENCY(new Code(110)),
    DISTRESS(new Code(112));

    private final Code code;

    Category(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static Category fromSymbol(int symbol) {
        for (Category c : values())
            if (c.getCode().getSymbol() == symbol)
                return c;

        throw new IllegalArgumentException("Failed to find category for " +
            "specified symbol: " + symbol);
    }

    @Override
    public String toString() {
        switch (this) {
            case ROUTINE: return "Routine";
            case SAFETY: return "Safety";
            case URGENCY: return "Urgency";
            case DISTRESS: return "Distress";
            default: throw new IllegalArgumentException();
        }
    }
}
