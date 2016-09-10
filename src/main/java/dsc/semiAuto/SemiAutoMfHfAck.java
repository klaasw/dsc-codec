package dsc.semiAuto;

import dsc.SemiAutoCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

/** @author AlexeyVorobyev */
public final class SemiAutoMfHfAck extends SemiAutoCall {

    private final Optional<Frequency> rxFrequency;

    private final Optional<Frequency> txFrequency;

    private final Optional<Coordinates> pos;

    public SemiAutoMfHfAck(Mmsi selfId, Mmsi address,
                           FirstTelecommand ft, SecondTelecommand st,
                           PstnNumber number, Optional<Frequency> rxFrequency,
                           Optional<Frequency> txFrequency,
                           Optional<Coordinates> pos) {
        super(Phasing.EOS_ACK_BQ, selfId, address, ft, st, number);
        this.rxFrequency = rxFrequency;
        this.txFrequency = txFrequency;
        this.pos = pos;
    }

    @Override
    protected List<Code> getAdditionalCodes() {
        List<Code> codes = new ArrayList<>();

        if (rxFrequency.isPresent())
            codes.addAll(rxFrequency.get().toCodes());
        else if (pos.isPresent())
            codes.addAll(pos.get().toCodes());
        else
            codes.addAll(Frequency.getNoInfoCodes());

        if (txFrequency.isPresent())
            codes.addAll(txFrequency.get().toCodes());
        else if (!pos.isPresent())
            codes.addAll(Frequency.getNoInfoCodes());

        return codes;
    }

    @Override
    public String toString() {
        return "SemiAutoMfHfAck{" +
            "address=" + getAddress() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", rxFrequency=" + rxFrequency +
            ", txFrequency=" + txFrequency +
            ", pos=" + pos +
            ", number=" + getPstnNumber() +
            '}';
    }

}
