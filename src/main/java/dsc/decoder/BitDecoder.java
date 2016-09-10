package dsc.decoder;

import dsc.entities.Code;
import dsc.structure.Phasing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static dsc.entities.Code.ERROR_CODE;
import static dsc.entities.Code.SKIP_CODE;
import static dsc.structure.Phasing.EOS;

/**
 * Decoder for bits (0 and 1)
 *
 * @author AlexeyVorobyev
 */
final class BitDecoder {

    private static final Logger logger =
        LoggerFactory.getLogger(BitDecoder.class);

    private final DscDecoder codeDecoder;

    private final List<Code> beginningPattern = createBeginningPattern();

    private final List<Code> endingPattern = createEndingPattern();

    private final ByteBuffer buffer = ByteBuffer.allocate(10 * 400);

    public BitDecoder(DscDecoder codeDecoder) {
        this.codeDecoder = codeDecoder;
    }

    public void onBit(byte bit) {
        buffer.put(bit);

        byte[] bytes = new byte[buffer.position()];

        System.arraycopy(buffer.array(), 0, bytes, 0, bytes.length);

        int toDelete = decodeBytes(bytes);
        if (toDelete == 0)
            return;

        int newPosition = buffer.position() - toDelete;
        byte[] tempBytes = new byte[buffer.capacity() - toDelete];
        buffer.position(toDelete);
        buffer.get(tempBytes);
        buffer.clear();

        buffer.put(tempBytes);
        buffer.position(newPosition);
    }

    public int decodeBytes(byte[] bytes) {
        // Not enough bytes for decoding?
        if (bytes.length < 200)
            return 0;

        List<Code> createdCodes = null;
        // Size of symbol is 10 bytes, so we need to shift
        for (int i = 0; i < 10; i++) {
            byte[] tempBytes = new byte[bytes.length - i];
            System.arraycopy(bytes, i, tempBytes, 0, bytes.length - i);

            createdCodes = Code.createCodes(tempBytes);

            // Counter for incorrect symbols
            int bad = 0;

            // Check on errors
            for (Code sign : createdCodes)
                if (sign.equals(ERROR_CODE))
                    bad++;

            if (!(tempBytes.length / 10 - bad < 10))
                break;
        }

        // Message ready for further decoding?
        int headPosition = findHeadPosition(createdCodes);
        // if we've found beginning then continue decoding
        // else remove 21 bytes
        if (headPosition < 0)
            return 21;

        // Do we need to put back into buffer and save?
        int endPosition = findEndingPosition(createdCodes);
        // if we've found end of sequence then continue decoding
        // else remove bytes
        if (endPosition < 0) {
            if (bytes.length > 2000)
                return 50;

            return 0;
        }

        // Is DSC with expanded sequence?
        int expandPosition = findExpandPosition(
            createdCodes, endPosition + endingPattern.size()
        );
        if (expandPosition < 0 && bytes.length < 2000) {
            return 0;
        }

        // To the end position adds two for decoding ECC symbol
        List<Code> message = processMessage(
            createdCodes, headPosition,
            expandPosition < 0 ? endPosition + 3 : expandPosition + 3,
            expandPosition > 0
        );

        String decodedSymbols = message.stream().map(
            (c) -> c.getSymbol() + "" + " "
        ).reduce(
            (s1, s2) -> s1 + s2
        ).orElse("");

        logger.info("Decoded symbols: " + decodedSymbols);

        codeDecoder.decodeCodes(message);

        return (endPosition * 10 + endingPattern.size() * 10);
    }

    public int findHeadPosition(List<Code> codes) {
        List<Code> tempCodes = new ArrayList<>();

        for (int i = 0; i < codes.size(); i++) {
            tempCodes.add(i, codes.get(i));

            if (i > 3 && isMatchesBeginning(tempCodes))
                return i + 1;
        }

        return -1;
    }

    public int findEndingPosition(List<Code> codes) {
        List<Code> tempCodes = new ArrayList<>();

        for (int i = 0; i < codes.size(); i++) {
            tempCodes.add(i, codes.get(i));

            if (i > 3 && isMatchesEnding(tempCodes))
                return (i - endingPattern.size());
        }

        return -1;
    }

    public int findExpandPosition(List<Code> codes, int endPos) {
        List<Code> tempCodes = new ArrayList<>();

        for (int i = endPos; i < codes.size(); ++i) {
            tempCodes.add(i - endPos, codes.get(i));

            if (i > 3 && isMatchesEnding(tempCodes)) {
                return (i - endingPattern.size());
            }
        }

        return -1;
    }

    /** Codes are beginning of the message if phasing was achieved. */
    public boolean isMatchesBeginning(List<Code> codes) {
        int dxCounter = 0;
        int rxCounter = 0;

        for (int i = 0; i < codes.size(); i++) {
            if (beginningPattern.size() - codes.size() + i < 0)
                continue;

            if (codes.get(i).equals(beginningPattern.get
                (beginningPattern.size() + i - codes.size()))) {
                if (Phasing.isDx(codes.get(i)))
                    dxCounter += 1;
                else if (Phasing.isRx(codes.get(i)))
                    rxCounter += 1;
            }
            boolean phasingAchieved = (rxCounter >= 3)
                || ((rxCounter >= 2) && (dxCounter >= 1))
                || ((rxCounter >= 1) && (dxCounter >= 2));

            if (phasingAchieved)
                return true;
        }

        return false;
    }

    public boolean isMatchesEnding(List<Code> codes) {
        int eosCounter = 0;

        for (int i = 0; i < codes.size(); i++) {
            if (codes.size() > endingPattern.size() + i)
                continue;

            boolean flag = codes.get(i).equals(
                endingPattern.get(endingPattern.size() + i - codes.size())
            );
            boolean isEos = Phasing.isEOS(codes.get(i));

            if (flag && isEos) {
                eosCounter++;

                if (eosCounter >= 4)
                    return true;
            }
        }

        return false;
    }

    /** Converts double DSC into single */
    private List<Code> processMessage(List<Code> createdCodes,
                                      final int startPosition,
                                      final int endPosition,
                                      boolean expand) {
        List<Code> message = new ArrayList<>(endPosition - startPosition + 4);
        for (int i = 0; i <= (endPosition - startPosition) / 2 + 2; i++)
            message.add(ERROR_CODE);

        for (int i = 0; i <= ((endPosition - startPosition) / 2) + 2; i++) {
            boolean flag1 = createdCodes.get(startPosition - 4 + i * 2).equals
                (createdCodes.get(startPosition + i * 2 + 1));

            if (flag1) {
                message.set(i, createdCodes.get(startPosition + i * 2 + 1));
            } else if (
                createdCodes.get(startPosition - 4 + i * 2).isErrorCode()
                ) {
                if (createdCodes.get(startPosition + i * 2 + 1).isErrorCode()) {
                    message.set(i, createdCodes.get(startPosition + i * 2 + 1));
                }
            } else
                message.set(i, createdCodes.get(startPosition - 4 + i * 2));
        }

        // Removing of superfluos EOS symbols
        if (expand) {
            for (int i = 0; i < 2; ++i) {
                int eosPos = message.lastIndexOf(Phasing.EOS.getCode());
                message.remove(eosPos);
            }

            int eosPos = message.lastIndexOf(Phasing.EOS.getCode());
            Code expandEos = message.get(eosPos);
            message.remove(eosPos);
            message.add(message.size() - 1, expandEos);
        }

        return message;
    }

    private List<Code> createBeginningPattern() {
        List<Code> pattern = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            // Dx
            pattern.add(i * 2, new Code(125));
            // Rx
            pattern.add(i * 2 + 1, new Code(111 - i));
        }

        pattern.add(12, SKIP_CODE);
        pattern.add(13, new Code(105));
        pattern.add(14, SKIP_CODE);
        pattern.add(15, new Code(104));

        return pattern;
    }

    private List<Code> createEndingPattern() {
        List<Code> pattern = new ArrayList<>();

        pattern.add(0, EOS.getCode());
        for (int i = 1; i < 4; i++)
            pattern.add(i, SKIP_CODE);

        for (int i = 4; i < 7; i++)
            pattern.add(i, EOS.getCode());

        pattern.add(7, SKIP_CODE);

        return pattern;
    }
}
