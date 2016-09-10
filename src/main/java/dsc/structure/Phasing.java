package dsc.structure;

import dsc.entities.Code;

/** @author AlexeyVorobyev */
public enum Phasing {

    RX_0(new Code(104)),
    RX_1(new Code(105)),
    RX_2(new Code(106)),
    RX_3(new Code(107)),
    RX_4(new Code(108)),
    RX_5(new Code(109)),
    RX_6(new Code(110)),
    RX_7(new Code(111)),
    EOS_ACK_RQ(new Code(117)),
    EOS_ACK_BQ(new Code(122)),
    DX(new Code(125)),
    EOS(new Code(127));

    private final Code code;

    Phasing(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public static boolean isEOS(Phasing p) {
        return p == EOS || p == EOS_ACK_BQ || p == EOS_ACK_RQ;
    }

    public static boolean isEOS(Code code) {
        return code.getSymbol() == EOS.getCode().getSymbol() ||
            code.getSymbol() == EOS_ACK_BQ.getCode().getSymbol() ||
            code.getSymbol() == EOS_ACK_RQ.getCode().getSymbol();
    }

    public static Phasing[] getRxes() {
        return new Phasing[]{
            RX_7, RX_6, RX_5, RX_4, RX_3, RX_2, RX_1, RX_0
        };
    }

    public static boolean isRx(Code p) {
        int s = p.getSymbol();

        return s == 104 || s == 105 || s == 106 || s == 107 || s == 108 ||
            s == 109 || s == 110 || s == 111;
    }

    public static boolean isDx(Code p) {
        return p.getSymbol() == 125;
    }

    public static Phasing fromSymbol(int symbol) {
        for (Phasing p : values())
            if (p.getCode().getSymbol() == symbol)
                return p;

        throw new IllegalArgumentException("Failed to find phasing for " +
            "specified symbol: " + symbol);
    }
}
