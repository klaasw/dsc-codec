package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum DistressNature {

    FIRE_EXPLOSION(new Code(100)),
    FLOODING(new Code(101)),
    COLLISION(new Code(102)),
    GROUNDING(new Code(103)),
    LISTING(new Code(104)),
    SINKING(new Code(105)),
    DISABLED_AND_ADRIFT(new Code(106)),
    UNDESIGNATED(new Code(107)),
    ABANDONING_SHIP(new Code(108)),
    PIRACY(new Code(109)),
    MAN_OVERBOARD(new Code(110)),
    EPIRB_EMISSION(new Code(112));

    private final Code code;

    DistressNature(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static DistressNature fromSymbol(int symbol) {
        for (DistressNature nature : values())
            if (nature.code.getSymbol() == symbol)
                return nature;

        throw new IllegalArgumentException("Failed to find distress nature " +
            "for specified symbol: " + symbol);
    }

    @Override
    public String toString() {
        switch (this) {
            case FIRE_EXPLOSION: return "Fire, explosion";
            case FLOODING: return "Flooding";
            case COLLISION: return "Collision";
            case GROUNDING: return "Grounding";
            case LISTING: return "Listing, in danger of capsizing";
            case SINKING: return "Sinking";
            case DISABLED_AND_ADRIFT: return "Disabled and adrift";
            case UNDESIGNATED: return "Undesignated distress";
            case ABANDONING_SHIP: return "Abandoning ship";
            case PIRACY: return "Piracy/armed robbery attack";
            case MAN_OVERBOARD: return "Man overboard";
            case EPIRB_EMISSION: return "EPIRB emission";
            default: throw new IllegalArgumentException();
        }
    }
}
