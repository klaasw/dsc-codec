package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum SecondTelecommand {

    NO_REASON_GIVEN(new Code(100)),
    CONGESTION_AT_MARITIME_SWITCHIN_CENTER(new Code(101)),
    BUSY(new Code(102)),
    QUEUE_INDICATOR(new Code(103)),
    STATION_BARRED(new Code(104)),
    NO_OPERATOR_AVAILABLE(new Code(105)),
    OPERATOR_TEMPORARILY_UNAVAILABLE(new Code(106)),
    EQUIPMENT_DISABLED(new Code(107)),
    UNABLE_TO_USE_PROPOSED_CHANNEL(new Code(108)),
    UNABLE_TO_USE_PROPOSED_MODE(new Code(109)),
    NOT_PARTIES_OF_ARMED_CONFLICT(new Code(110)),
    MEDICAL_TRANSPORTS(new Code(111)),
    PAY_PHONE_PUBLIC_CALL_OFFICE(new Code(112)),
    FACSIMILE(new Code(113)),
    NO_INFO(new Code(126));

    private final Code code;

    SecondTelecommand(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static SecondTelecommand fromSymbol(int symbol) {
        for (SecondTelecommand t : values())
            if (t.getCode().getSymbol() == symbol)
                return t;

        throw new IllegalArgumentException("Failed to find second " +
            "telecommand for specified symbol: " + symbol);
    }

    @Override
    public String toString() {
        switch (this) {
            case NO_REASON_GIVEN:
                return "No reason given";
            case CONGESTION_AT_MARITIME_SWITCHIN_CENTER:
                return "Congestion at maritime switching centre";
            case BUSY:
                return "Busy";
            case QUEUE_INDICATOR:
                return "Queue indication";
            case STATION_BARRED:
                return "Station barred";
            case NO_OPERATOR_AVAILABLE:
                return "No operator available";
            case OPERATOR_TEMPORARILY_UNAVAILABLE:
                return "Operator temporarily unavailable";
            case EQUIPMENT_DISABLED:
                return "Equipmet disabled";
            case UNABLE_TO_USE_PROPOSED_CHANNEL:
                return "Unable to use proposed channel";
            case UNABLE_TO_USE_PROPOSED_MODE:
                return "Unable to use proposed mode";
            case NOT_PARTIES_OF_ARMED_CONFLICT:
                return "Ships and aircraft of States not parties to an " +
                    "armed conflict";
            case MEDICAL_TRANSPORTS:
                return "Medical transports";
            case PAY_PHONE_PUBLIC_CALL_OFFICE:
                return "Pay-phone/public call office";
            case FACSIMILE:
                return "Facsimile/data according to Recommendation " +
                    "ITU-R M.1081";
            case NO_INFO:
                return "No information";
            default:
                throw new IllegalArgumentException();
        }
    }
}
