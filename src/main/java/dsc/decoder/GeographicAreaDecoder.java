package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.distress.*;
import dsc.entities.*;
import dsc.safety.SafetyGeographicArea;
import dsc.structure.*;
import dsc.urgency.UrgencyGeographicArea;

import java.util.List;
import java.util.Optional;

import static dsc.structure.Category.SAFETY;
import static dsc.structure.Category.URGENCY;
import static dsc.structure.FormatSpecifier.ALL_SHIPS;
import static dsc.structure.FormatSpecifier.GEOGRAPHICAL_AREA;

/**
 * Decoder of "Geographical Area" DSCs.
 *
 * Decodes: retranslation of distress (with ack), safety and urgency.
 *
 * @author AlexeyVorobyev
 */
final class GeographicAreaDecoder extends CodesDecoder {

    @Override
    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Area area = Area.fromCodes(codes.subList(2, 7));

        Category category = Category.fromSymbol(
            codes.get(7).getSymbol()
        );

        Mmsi selfId = Mmsi.fromCodes(codes.subList(8, 13));

        if (category == SAFETY || category == URGENCY) {
            return decodeSafetyOrUrgency(selfId, area, category, codes);
        }

        return decodeDistressRelay(selfId, area, codes);
    }

    private DigitalSelectiveCall decodeSafetyOrUrgency(Mmsi selfId, Area area,
                                                       Category category,
                                                       List<Code> codes) {
        FirstTelecommand ft =
            FirstTelecommand.fromSymbol(codes.get(13).getSymbol());

        SecondTelecommand st =
            SecondTelecommand.fromSymbol(codes.get(14).getSymbol());

        Frequency freqRx = Frequency.fromCodes(codes.subList(15, 18));

        Optional<Frequency> freqTx = Optional.empty();
        if (hasFrequency(codes.subList(18, 21))) {
            freqTx = Optional.of(Frequency.fromCodes(codes.subList(18, 21)));
        }

        return category == SAFETY
            ? new SafetyGeographicArea(selfId, area, ft, st, freqRx, freqTx)
            : new UrgencyGeographicArea(selfId, area, ft, st, freqRx, freqTx);
    }

    private DigitalSelectiveCall decodeDistressRelay(Mmsi selfId, Area area,
                                                     List<Code> codes) {
        Mmsi distressMmsi = Mmsi.fromCodes(codes.subList(14, 19));

        DistressNature nature = DistressNature.fromSymbol(
            codes.get(19).getSymbol()
        );

        Coordinates coordinates = Coordinates.fromCodes(codes.subList(20, 25));

        TimeUTC time = TimeUTC.fromCodes(codes.subList(25, 27));

        FirstTelecommand subsequentTelecommunications =
            FirstTelecommand.fromSymbol(codes.get(27).getSymbol());

        Phasing eos = getEos(codes);

        Optional<ExpansionPosition> expPos = Optional.empty();
        if (isExpanded(codes)) {
            expPos = Optional.of(ExpansionPosition.fromCodes(
                codes.subList(31, 35)
            ));
        }

        switch (eos) {
            case EOS:
                return new DistressRelay(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expPos);
            case EOS_ACK_RQ:
                return new DistressRelayRq(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expPos);
            case EOS_ACK_BQ:
                return new DistressRelayAck(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expPos);
            default:
                throw new IllegalArgumentException("Неизвестный EOS! " + eos);
        }
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(GEOGRAPHICAL_AREA.getCode());
    }
}
