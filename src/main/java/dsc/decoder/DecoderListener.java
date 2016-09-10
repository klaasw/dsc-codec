package dsc.decoder;

import dsc.DigitalSelectiveCall;

/**
 * Listener of decoded DSC.
 *
 * @author AlexeyVorobyev
 */
public interface DecoderListener {

    void onIncomeDsc(DigitalSelectiveCall call);

    void onDotPatternFound();
}
