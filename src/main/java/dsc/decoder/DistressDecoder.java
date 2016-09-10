package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.distress.Distress;
import dsc.entities.*;
import dsc.structure.*;

import java.util.List;
import java.util.Optional;

/**
 * Decoder of "Distress" messages.
 *
 * @author AlexeyVorobyev
 */
final class DistressDecoder extends CodesDecoder {

    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Mmsi selfMmsi = Mmsi.fromCodes(codes.subList(2, 7));
        DistressNature nature = DistressNature.fromSymbol(
            codes.get(7).getSymbol()
        );
        Coordinates coordinates = Coordinates.fromCodes(
            codes.subList(8, 13)
        );
        TimeUTC timeUTC = TimeUTC.fromCodes(
            codes.subList(13, 15)
        );
        FirstTelecommand subsequentTransmissions =
            FirstTelecommand.fromSymbol(codes.get(15).getSymbol());

        Optional<ExpansionPosition> expansionPosition = Optional.empty();
        if (isExpanded(codes)) {
            expansionPosition = Optional.of(ExpansionPosition.fromCodes(
                codes.subList(19, 23)
            ));
        }

        return new Distress(
            selfMmsi, nature, coordinates, timeUTC, subsequentTransmissions,
            expansionPosition
        );
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(FormatSpecifier.DISTRESS.getCode());
    }
}
