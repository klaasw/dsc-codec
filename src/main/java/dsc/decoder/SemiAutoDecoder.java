package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.semiAuto.*;
import dsc.structure.*;

import java.util.List;
import java.util.Optional;

import static dsc.structure.FormatSpecifier.IDIVIDUAL_SEMI_AUTOMATIC;
import static dsc.structure.Phasing.EOS_ACK_RQ;

/**
 * Decoder of "Semi-auto" DSCs.
 *
 * @author AlexeyVorobyev
 */
final class SemiAutoDecoder extends CodesDecoder {

    @Override
    public DigitalSelectiveCall decodeCodes(List<Code> codes) {
        Mmsi address = Mmsi.fromCodes(codes.subList(2, 7));

        Mmsi selfId = Mmsi.fromCodes(codes.subList(8, 13));

        FirstTelecommand ft =
            FirstTelecommand.fromSymbol(codes.get(13).getSymbol());

        SecondTelecommand st =
            SecondTelecommand.fromSymbol(codes.get(14).getSymbol());

        Optional<Coordinates> pos = Optional.empty();
        Optional<Frequency> freqRx = Optional.empty();
        Optional<Frequency> freqTx = Optional.empty();
        Optional<DscNumber> vhfChannelNumber = Optional.empty();
        Phasing eos = getEos(codes);

        boolean withMfHfChannel = isMfHfChannelMarker(codes.get(15));
        boolean withVhfChannel = isVhfChannelMarker(codes.get(15));
        boolean withPos = isPosMarker(codes.get(15));

        // Отслеживание позиции, с которой необходимо начать считывать номер
        int lastPos;
        if (withPos) {
            // Координаты
            pos = Optional.of(Coordinates.fromCodes(codes.subList(16, 21)));
            lastPos = 21;
        } else if (withVhfChannel) {
            // VHF канал
            vhfChannelNumber = Optional.of(DscNumber.fromCodes(
                codes.subList(16, 18)
            ));

            lastPos = 18;
        } else if (withMfHfChannel) {
            // ПВ/КВ канал. Должен представлять частоту
            List<Code> channelCodes = codes.subList(16, 18);
            channelCodes.add(
                0, parseSecondSymbolOfCode(codes.get(15))
            );

            freqRx = Optional.of(Frequency.fromCodes(channelCodes));

            lastPos = 20;
        } else {
            // Частота
            freqRx = Optional.of(Frequency.fromCodes(
                codes.subList(15, 18)
            ));

            lastPos = 18;
        }

        // Проверка на присутствие tx частоты
        if (!withPos && hasFrequency(codes.subList(lastPos, lastPos + 3))) {
            if (withMfHfChannel) {
                List<Code> txCodes = codes.subList(lastPos, lastPos + 2);
                txCodes.add(
                    0, parseSecondSymbolOfCode(codes.get(lastPos - 1))
                );
                freqTx = Optional.of(Frequency.fromCodes(txCodes));
            } else {
                freqTx = Optional.of(Frequency.fromCodes(
                    codes.subList(lastPos, lastPos + 3)
                ));
            }

            lastPos += 3;
        }

        PstnNumber number = PstnNumber.fromCodes(
            codes.subList(lastPos + 1, codes.size() - 2)
        );

        // Если был передан номер УКВ канала, то это УКВ вызов
        if (withVhfChannel) {
            return eos == EOS_ACK_RQ
                ? new SemiAutoVhf(selfId, address, ft, st, number,
                vhfChannelNumber.get())
                : new SemiAutoVhfAck(selfId, address, ft, st, number,
                vhfChannelNumber.get());
        } else {
            return eos == EOS_ACK_RQ
                ? new SemiAutoMfHf(selfId, address, ft, st, number,
                freqRx, freqTx, pos)
                : new SemiAutoMfHfAck(selfId, address, ft, st, number,
                freqRx, freqTx, pos);
        }
    }

    @Override
    public boolean isMatches(Code code) {
        return code.equals(IDIVIDUAL_SEMI_AUTOMATIC.getCode());
    }
}
