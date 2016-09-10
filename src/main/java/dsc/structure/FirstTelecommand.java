package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum FirstTelecommand {

    F3E_G3E_ALL_MODES_TP(new Code(100)),
    F3E_G3E_DUPLEX_TP(new Code(101)),
    POLLING(new Code(103)),
    UNABLE_TO_COMPLY(new Code(104)),
    END_OF_CALL(new Code(105)),
    DATA(new Code(106)),
    J3E_TP(new Code(109)),
    DISTRESS_ACK(new Code(110)),
    DISTRESS_RELAY(new Code(112)),
    F1B_J2B_TTY_FEC(new Code(113)),
    F1B_J2B_TTY_ARQ(new Code(115)),
    TEST(new Code(118)),
    SHIP_POSITION(new Code(121)),
    NO_INFO(new Code(126));

    private final Code code;

    FirstTelecommand(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static FirstTelecommand fromSymbol(int symbol) {
        for (FirstTelecommand t : values())
            if (t.getCode().getSymbol() == symbol)
                return t;

        throw new IllegalArgumentException("Failed to find first telecommand " +
            "for specified symbol: " + symbol);
    }

    @Override
    public String toString() {
        switch (this) {
            case F3E_G3E_ALL_MODES_TP:
                return "F3E/G3E All modes TP";
            case F3E_G3E_DUPLEX_TP:
                return "F3E/G3E duplex TP";
            case POLLING:
                return "Polling";
            case UNABLE_TO_COMPLY:
                return "Unable to comply";
            case END_OF_CALL:
                return "End of call";
            case DATA:
                return "Data";
            case J3E_TP:
                return "J3E TP";
            case DISTRESS_ACK:
                return "Distress acknowledgement";
            case DISTRESS_RELAY:
                return "Distress relay";
            case F1B_J2B_TTY_FEC:
                return "F1B/J2B TTY-FEC";
            case F1B_J2B_TTY_ARQ:
                return "F1B/J2B TTY-ARQ";
            case TEST:
                return "Test";
            case SHIP_POSITION:
                return "Ship position or location registration updating";
            case NO_INFO:
                return "No information";
            default:
                throw new IllegalArgumentException();
        }
    }
}
