package dsc.entities;

import dsc.structure.Phasing;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents code of symbol.
 *
 * On creation calculates 10-bits ECC code.
 *
 * @author AlexeyVorobyev
 */
public final class Code {

    public static final Code SKIP_CODE = new Code(0);

    public static final Code ERROR_CODE = new Code(-1);

    /** Code in decimal nubmer system */
    private final int symbol;

    /** Rec. ITU-R M.493-13, pg. 4 */
    private byte[] tenDigitCode;

    public Code(int symbol) {
        this.symbol = symbol;
    }

    public int getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code) {
            Code other = (Code) obj;

            if (symbol == other.symbol)
                return true;
            else if (Phasing.isEOS(this) && Phasing.isEOS(other))
                return true;
        }

        return false;
    }

    public boolean isErrorCode() {
        return symbol == ERROR_CODE.symbol;
    }

    /** Returns ECC. See Rec. ITU-R M.493-13, pg. 4 */
    public byte[] getTenDigitCode() {
        if (tenDigitCode == null) {
            int tempValue = symbol;

            byte[] bytes = new byte[10];

            int divisor = 64;

            int zeroCount = 0;
            for (int i = 6; i >= 0; i--) {
                if (tempValue / divisor == 0) {
                    bytes[i] = 0;
                    zeroCount++;
                } else {
                    bytes[i] = 1;
                    tempValue -= divisor;
                }
                divisor /= 2;
            }

            divisor = 4;
            for (int i = 7; i < 10; i++) {
                if (zeroCount / divisor == 0)
                    bytes[i] = 0;
                else {
                    bytes[i] = 1;
                    zeroCount -= divisor;
                }
                divisor /= 2;
            }

            tenDigitCode = bytes;
        }

        return tenDigitCode;
    }

    public static List<Code> createCodes(byte[] bytes) {
        List<Code> codes = new ArrayList<>();

        for (int i = 0; i < bytes.length / 10; i++) {
            int counter = 0;

            byte[] bValue = new byte[7];
            byte[] errbar = new byte[3];

            byte b;
            for (int j = 0; j < 10; j++) {
                // Cчитываем 10 байт
                b = bytes[j + i * 10];
                // Подсчитываем количество нулей
                if (j < 7) {
                    bValue[j] = b;
                    if (b == 0x00)
                        counter++;
                } else {
                    errbar[j - 7] = b;
                }
            }

            if (counter == bytesToInt(errbar, false))
                codes.add(new Code(bytesToInt(bValue, true)));
            else
                codes.add(Code.ERROR_CODE);

        }
        return codes;
    }

    private static int bytesToInt(byte[] bytes, boolean order) {
        int result = 0;
        int power = 1;

        if (order) {
            for (byte aByte : bytes) {
                if (aByte == 0x01)
                    result += power;

                power *= 2;
            }
        } else {
            for (int i = bytes.length - 1; i >= 0; i--) {
                if (bytes[i] == 0x01)
                    result += power;

                power *= 2;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "Code{" + symbol + '}';
    }
}
