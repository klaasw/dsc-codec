package dsc.semiAuto;

import dsc.DscMarkers;
import dsc.SemiAutoCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.ArrayList;
import java.util.List;

/** @author AlexeyVorobyev */
public final class SemiAutoVhfAck extends SemiAutoCall {

    private final DscNumber channelNumber;

    public SemiAutoVhfAck(Mmsi selfId, Mmsi address,
                          FirstTelecommand ft, SecondTelecommand st,
                          PstnNumber number, DscNumber channelNumber) {
        super(Phasing.EOS_ACK_BQ, selfId, address, ft, st, number);
        this.channelNumber = channelNumber;
    }

    @Override
    public String toString() {
        return "SemiAutoVhfAck{" +
            "address=" + getAddress() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", channelNumber=" + channelNumber +
            ", number=" + getPstnNumber() +
            '}';
    }

    @Override
    protected List<Code> getAdditionalCodes() {
        List<Code> codes = new ArrayList<>();

        // Marker of VHF channel
        codes.add(new Code(DscMarkers.vhfChannelMarker));

        List<Code> channelNumCodes = channelNumber.toCodes();
        if (channelNumCodes.size() < 2) {
            codes.add(new Code(0));
            codes.add(channelNumCodes.get(0));
        } else if (channelNumCodes.size() == 2) {
            codes.addAll(channelNumCodes);
        } else {
            codes.addAll(channelNumCodes.subList(0, 2));
        }

        return codes;
    }
}
