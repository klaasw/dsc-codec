package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.entities.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Main decoder of DSC.
 *
 * Converts list of symbols into DSC.
 *
 * @author AlexeyVorobyev
 */
public final class DscDecoder {

    private final Logger log = LoggerFactory.getLogger(DscDecoder.class);

    private final BitDecoder bitDecoder = new BitDecoder(this);

    private final List<DecoderListener> listeners = new ArrayList<>();

    private final List<CodesDecoder> codesDecoders = createDecoders();

    public void addListener(DecoderListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DecoderListener listener) {
        listeners.remove(listener);
    }

    public void onBit(byte bit) {
        bitDecoder.onBit(bit);
    }

    void decodeCodes(final List<Code> codes) {
        Code formatSpecifier = codes.get(0);

        if (!CodesDecoder.isEccCorrect(codes))
            log.error("Incorrect ECC!");

        if (CodesDecoder.isExpanded(codes) &&
            !CodesDecoder.isExpandEccCorrect(codes)) {
            log.error("Incorrect ECC of expanded sequence!");
        }

        Optional<CodesDecoder> decoderOpt = codesDecoders.stream().filter(
            (d) -> d.isMatches(formatSpecifier)
        ).findFirst();

        if (decoderOpt.isPresent()) {
            try {
                DigitalSelectiveCall dsc = decoderOpt.get().decodeCodes(codes);

                log.info("Decoded DSC: " + dsc.toString() + "\n");
                listeners.stream().forEach((l) -> l.onIncomeDsc(dsc));
            } catch (Exception e) {
                log.error("Failed decode DSC! Symbols:" + codes, e);
            }
        } else {
            log.error("Failed to find appropriate decoder for this " +
                "format speicifactor" + formatSpecifier.getSymbol() + "!");
        }
    }

    void notifyDotPatternFound() {
        listeners.forEach(DecoderListener::onDotPatternFound);
    }

    private List<CodesDecoder> createDecoders() {
        List<CodesDecoder> decoders = new ArrayList<>();

        decoders.add(new DistressDecoder());
        decoders.add(new AllShipsDecoder());
        decoders.add(new GeographicAreaDecoder());
        decoders.add(new RoutineGroupDecoder());
        decoders.add(new IndividualDecoder());
        decoders.add(new SemiAutoDecoder());

        return Collections.unmodifiableList(decoders);
    }
}
