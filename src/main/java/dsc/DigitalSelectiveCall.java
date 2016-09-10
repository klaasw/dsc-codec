package dsc;

import dsc.entities.Code;
import dsc.entities.Mmsi;
import dsc.structure.*;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Abstract Digital Selective Call.
 *
 * Contains format specifier and symbol of the end of the sequence called "EOS".
 *
 * @author AlexeyVorobyev
 */
public abstract class DigitalSelectiveCall implements Serializable {

    private final FormatSpecifier formatSpecifier;

    private final Phasing eos;

    private final Mmsi selfId;

    public DigitalSelectiveCall(FormatSpecifier formatSpecifier,
                                Phasing eos, Mmsi selfId) {
        if (!Phasing.isEOS(eos))
            throw new IllegalArgumentException(
                "Expected symbol of the end of the sequence. " +
                    "Received: " + eos.toString()
            );

        this.formatSpecifier = formatSpecifier;
        this.eos = eos;
        this.selfId = selfId;
    }

    public FormatSpecifier getFormatSpecifier() {
        return formatSpecifier;
    }

    public Phasing getEos() {
        return eos;
    }

    public Mmsi getSelfId() {
        return selfId;
    }

    /**
     * Calculates symbol of error detection "ECC" from information codes.
     *
     * @param infoCodes information codes
     * @return ECC symbol
     */
    protected Code calculateEccFromInfoCodes(Collection<Code> infoCodes) {
        return new Code(
            infoCodes.stream().map(Code::getSymbol).reduce(0, (a, b) -> a ^ b)
        );
    }

    /**
     * Converts DSC into symbol representation for transmission.
     *
     * Contains two specifications of format, information codes, ECC and EOS.
     *
     * @return list of symbol for transmission
     */
    protected abstract List<Code> toCodes();

    /**
     * Converts DSC into bytes representation
     *
     * @return sequence of bytes
     */
    public byte[] encode() {
        List<Code> dxSeq = new ArrayList<>();
        List<Code> rxSeq = new ArrayList<>();

        // Construction of phasing sequence
        for (int i = 0; i < 6; i++) dxSeq.add(Phasing.DX.getCode());
        for (Phasing rx : Phasing.getRxes()) rxSeq.add(rx.getCode());

        List<Code> codes = toCodes();

        for (Code s : codes) {
            dxSeq.add(s);
            rxSeq.add(s);
        }

        dxSeq.add(getEos().getCode());
        dxSeq.add(getEos().getCode());

        if (this instanceof PossibleExpansion) {
            PossibleExpansion pe = (PossibleExpansion) this;

            if (pe.getExpansion().isPresent()) {
                ExpansionPosition ep = pe.getExpansion().get();
                List<Code> epCodes = ep.toCodes();
                epCodes.add(getEos().getCode());
                epCodes.add(calculateEccFromInfoCodes(epCodes));

                rxSeq.add(new Code(126));
                rxSeq.add(new Code(126));

                for (Code s : epCodes) {
                    dxSeq.add(s);
                    rxSeq.add(s);
                }

                dxSeq.add(getEos().getCode());
                dxSeq.add(getEos().getCode());
            }
        }

        ByteBuffer bytes = ByteBuffer.allocate(
            200 + (dxSeq.size() + rxSeq.size()) * 10
        );

        // Adding dot pattern
        for (int i = 0; i < 200; i++) {
            if (i % 2 == 0)
                bytes.put((byte) 0x00);
            else
                bytes.put((byte) 0x01);
        }

        // Connection of DX and RX sequences
        for (int i = 0; i < (bytes.array().length - 200) / 20; i++) {
            bytes.put(dxSeq.get(i).getTenDigitCode());
            bytes.put(rxSeq.get(i).getTenDigitCode());
        }

        return byteToShort(bytes.array());
    }

    /**
     * Converts bytes to short type.
     *
     * @param bytes array of bytes
     * @return array of "short" type
     */
    private static byte[] byteToShort(byte[] bytes) {
        byte[] tempBytes = new byte[bytes.length * 2];

        for (int i = 0; i < tempBytes.length; i = i + 2) {
            tempBytes[i] = 0;
            tempBytes[i + 1] = bytes[i / 2];
        }

        return tempBytes;
    }
}
