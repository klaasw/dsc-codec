package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.distress.*;
import dsc.entities.*;
import dsc.safety.SafetyAllShips;
import dsc.structure.*;
import dsc.urgency.UrgencyAllShips;

import java.util.List;
import java.util.Optional;

import static dsc.structure.Category.SAFETY;
import static dsc.structure.Category.URGENCY;
import static dsc.structure.FirstTelecommand.DISTRESS_ACK;
import static dsc.structure.FirstTelecommand.DISTRESS_RELAY;
import static dsc.structure.FormatSpecifier.ALL_SHIPS;

/**
 * Decodes DSC with category of "All Ships".
 *
 * Calls: "Acknowledge of distress, retranslation of distress,
 * acknowledge of distress retranslation, safety and urgency.
 *
 * @author AlexeyVorobyev
 */
final class AllShipsDecoder extends CodesDecoder {

    @Override
    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Category category =
            Category.fromSymbol(codes.get(2).getSymbol());

        if (category == SAFETY || category == URGENCY) {
            return decodeSafetyOrUrgency(codes, category);
        }

        return decodeDistress(codes);
    }

    private DigitalSelectiveCall decodeDistress(List<Code> codes) {
        // Телекомманда должна быть обязательно связана с бедствием
        FirstTelecommand firstTelecommand =
            FirstTelecommand.fromSymbol(codes.get(8).getSymbol());

        if (firstTelecommand == DISTRESS_ACK)
            return decodeDistressAck(codes);
        else if (firstTelecommand == DISTRESS_RELAY)
            return decodeDistressRelay(codes);

        return null;
    }

    /** Decodes retranslation of distress (with acknowledge) */
    private DigitalSelectiveCall decodeDistressRelay(List<Code> codes) {
        Mmsi selfId = Mmsi.fromCodes(codes.subList(3, 8));

        Mmsi distressMmsi = Mmsi.fromCodes(codes.subList(9, 14));

        DistressNature nature = DistressNature.fromSymbol(
            codes.get(14).getSymbol()
        );

        Coordinates coordinates = Coordinates.fromCodes(codes.subList(15, 20));

        TimeUTC time = TimeUTC.fromCodes(codes.subList(20, 22));

        FirstTelecommand subsequentTelecommunications =
            FirstTelecommand.fromSymbol(codes.get(22).getSymbol());

        Phasing eos = getEos(codes);

        Optional<ExpansionPosition> expansionPosition = Optional.empty();
        if (isExpanded(codes)) {
            expansionPosition = Optional.of(ExpansionPosition.fromCodes(
                codes.subList(26, 30)
            ));
        }

        switch (eos) {
            case EOS:
                return new DistressRelay(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expansionPosition);
            case EOS_ACK_RQ:
                return new DistressRelayRq(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expansionPosition);
            case EOS_ACK_BQ:
                return new DistressRelayAck(ALL_SHIPS, selfId, distressMmsi,
                    nature, coordinates, time, subsequentTelecommunications,
                    Optional.empty(), expansionPosition);
            default:
                throw new IllegalArgumentException("Неизвестный EOS! " + eos);
        }
    }

    /** Decodes distress acknowledge */
    private DigitalSelectiveCall decodeDistressAck(List<Code> codes) {
        Mmsi selfId = Mmsi.fromCodes(codes.subList(3, 8));

        Mmsi distressMmsi = Mmsi.fromCodes(codes.subList(9, 14));

        DistressNature nature = DistressNature.fromSymbol(
            codes.get(14).getSymbol()
        );

        Coordinates distressCoordinates = Coordinates.fromCodes(
            codes.subList(15, 20)
        );

        TimeUTC time = TimeUTC.fromCodes(codes.subList(20, 22));

        FirstTelecommand subsequentCommunications =
            FirstTelecommand.fromSymbol(codes.get(22).getSymbol());

        Optional<ExpansionPosition> expPos = Optional.empty();
        if (isExpanded(codes)) {
            expPos = Optional.of(ExpansionPosition.fromCodes(
                codes.subList(26, 30)
            ));
        }

        return new DistressAck(
            selfId, distressMmsi, nature, distressCoordinates, time,
            subsequentCommunications, expPos
        );
    }

    /** Decodes "Safety" and "Urgency" calls */
    private DigitalSelectiveCall decodeSafetyOrUrgency(List<Code> codes,
                                                       Category category) {
        Mmsi selfId = Mmsi.fromCodes(codes.subList(3, 8));

        FirstTelecommand ft
            = FirstTelecommand.fromSymbol(codes.get(8).getSymbol());

        SecondTelecommand st
            = SecondTelecommand.fromSymbol(codes.get(9).getSymbol());

        Frequency rxFreq = Frequency.fromCodes(codes.subList(10, 13));

        Optional<Frequency> txFreq = Optional.empty();
        if (hasFrequency(codes.subList(13, 16))) {
            txFreq = Optional.of(Frequency.fromCodes(codes.subList(13, 16)));
        }

        return category == SAFETY
            ? new SafetyAllShips(selfId, ft, st, rxFreq, txFreq)
            : new UrgencyAllShips(selfId, ft, st, rxFreq, txFreq);
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(ALL_SHIPS.getCode());
    }
}
