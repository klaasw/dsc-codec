package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.routine.RoutineGroup;
import dsc.structure.FirstTelecommand;
import dsc.structure.FormatSpecifier;

import java.util.List;
import java.util.Optional;

/**
 * Decoder of "Group" DCSs.
 *
 * @author AlexeyVorobyev
 */
final class RoutineGroupDecoder extends CodesDecoder {

    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Mmsi address = Mmsi.fromCodes(codes.subList(2, 7));

        Mmsi selfId = Mmsi.fromCodes(codes.subList(8, 13));

        FirstTelecommand firstTelecommand =
            FirstTelecommand.fromSymbol(codes.get(13).getSymbol());

        Frequency freqRx = Frequency.fromCodes(codes.subList(15, 18));

        Optional<Frequency> freqTx = Optional.of(Frequency.fromCodes(
            codes.subList(18, 21)
        ));

        return new RoutineGroup(selfId, address, firstTelecommand, freqRx,
            freqTx);
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(FormatSpecifier.COMMON_INTEREST.getCode());
    }
}
