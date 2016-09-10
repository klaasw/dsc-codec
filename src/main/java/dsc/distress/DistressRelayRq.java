package dsc.distress;

import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public class DistressRelayRq extends AbstractDistressRelay {

    public DistressRelayRq(FormatSpecifier formatSpecifier, Mmsi selfId,
                           Mmsi distressMmsi, DistressNature nature,
                           Coordinates distressCoordinates, TimeUTC time,
                           FirstTelecommand subsequentCommunications,
                           Optional<Address> address,
                           Optional<ExpansionPosition> expansionPosition) {

        super(formatSpecifier, Phasing.EOS_ACK_RQ, selfId, distressMmsi, nature,
            distressCoordinates, time, subsequentCommunications, address,
            expansionPosition);
    }

    @Override
    public String toString() {
        return "DistressRelayRq{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", address=" + getAddress() +
            ", category=" + getCategory() +
            ", telecommand=" + getTelecommand() +
            ", distressMmsi=" + getDistressMmsi() +
            ", nature=" + getNature() +
            ", distressCoordinates=" + getDistressCoordinates() +
            ", time=" + getTime() +
            ", subsequentCommunications=" + getSubsequentCommunications() +
            ", expansion=" + getExpansion() +
            ", eos=" + getEos() +
            '}';
    }
}
