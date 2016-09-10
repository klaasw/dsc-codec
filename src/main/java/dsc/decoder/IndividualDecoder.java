package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.distress.*;
import dsc.entities.*;
import dsc.routine.RoutineIndividual;
import dsc.routine.RoutineIndividualAck;
import dsc.safety.SafetyIndividual;
import dsc.safety.SafetyIndividualAck;
import dsc.structure.*;
import dsc.urgency.UrgencyIndividual;
import dsc.urgency.UrgencyIndividualAck;

import java.util.List;
import java.util.Optional;

import static dsc.structure.Category.*;
import static dsc.structure.FormatSpecifier.ALL_SHIPS;
import static dsc.structure.Phasing.EOS_ACK_RQ;

/**
 * Decoder for "Individual" DSCs.
 *
 * @author AlexeyVorobyev
 */
final class IndividualDecoder extends CodesDecoder {

    @Override
    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Mmsi address = Mmsi.fromCodes(codes.subList(2, 7));

        Category category = Category.fromSymbol(
            codes.get(7).getSymbol()
        );

        Mmsi selfId = Mmsi.fromCodes(codes.subList(8, 13));

        if (category == SAFETY || category == URGENCY) {
            return decodeUrgOrSafety(codes, address, category, selfId);
        } else if (category == DISTRESS) {
            return decodeDistressRelay(codes, address, selfId);
        }

        return decodeRoutine(codes, address, selfId);
    }

    private DigitalSelectiveCall decodeDistressRelay(List<Code> codes,
                                                     Mmsi address,
                                                     Mmsi selfId) {
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

    private DigitalSelectiveCall decodeRoutine(List<Code> codes,
                                               Mmsi addr,
                                               Mmsi id) {
        FirstTelecommand ft =
            FirstTelecommand.fromSymbol(codes.get(13).getSymbol());

        SecondTelecommand st
            = SecondTelecommand.fromSymbol(codes.get(14).getSymbol());

        Optional<Coordinates> pos = Optional.empty();
        Optional<Frequency> freqRx = Optional.empty();
        Optional<Frequency> freqTx = Optional.empty();
        Phasing eos = getEos(codes);

        if (isPosMarker(codes.get(15))) {
            pos = Optional.of(Coordinates.fromCodes(codes.subList(16, 21)));
        } else {
            freqRx = Optional.of(Frequency.fromCodes(codes.subList(15, 18)));

            if (hasFrequency(codes.subList(18, 21))) {
                freqTx = Optional.of(Frequency.fromCodes(
                    codes.subList(18, 21)
                ));
            }
        }

        return eos == EOS_ACK_RQ
            ? new RoutineIndividual(id, addr, ft, st, freqRx, freqTx, pos)
            : new RoutineIndividualAck(id, addr, ft, st, freqRx, freqTx, pos);
    }

    private DigitalSelectiveCall decodeUrgOrSafety(List<Code> codes,
                                                   Mmsi address,
                                                   Category cat,
                                                   Mmsi selfId) {
        FirstTelecommand ft =
            FirstTelecommand.fromSymbol(codes.get(13).getSymbol());

        SecondTelecommand st
            = SecondTelecommand.fromSymbol(codes.get(14).getSymbol());

        Optional<Coordinates> pos = Optional.empty();
        Optional<TimeUTC> time = Optional.empty();
        Optional<Frequency> freqRx = Optional.empty();
        Optional<Frequency> freqTx = Optional.empty();
        Phasing eos = getEos(codes);

        if (isPosMarker(codes.get(15))) {
            pos = Optional.of(Coordinates.fromCodes(codes.subList(16, 21)));

            boolean withTime = codes.subList(21, codes.size()).size() > 2;
            if (withTime) {
                time = Optional.of(TimeUTC.fromCodes(codes.subList(21, 23)));
            }
        } else {
            freqRx = Optional.of(Frequency.fromCodes(codes.subList(15, 18)));

            if (hasFrequency(codes.subList(18, 21))) {
                freqTx = Optional.of(Frequency.fromCodes(
                    codes.subList(18, 21)
                ));
            }
        }

        switch (cat) {
            case URGENCY:
                return eos == EOS_ACK_RQ
                    ? new UrgencyIndividual(selfId, address, ft, st, freqRx,
                    freqTx, pos, time)
                    : new UrgencyIndividualAck(selfId, address, ft, st, freqRx,
                    freqTx, pos, time);
            case SAFETY:
                return eos == EOS_ACK_RQ
                    ? new SafetyIndividual(selfId, address, ft, st, freqRx,
                    freqTx, pos, time)
                    : new SafetyIndividualAck(selfId, address, ft, st, freqRx,
                    freqTx, pos, time);
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(FormatSpecifier.INDIVIDUAL.getCode());
    }
}
