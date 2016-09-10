package dsc.distress;

import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public class DistressRelayAck extends AbstractDistressRelay {

    public DistressRelayAck(FormatSpecifier formatSpecifier, Mmsi  selfId,
                            Mmsi distressMmsi, DistressNature nature,
                            Coordinates distressCoordinates, TimeUTC time,
                            FirstTelecommand subsequentCommunications,
                            Optional<Address> address,
                            Optional<ExpansionPosition> expansionPosition) {

        super(formatSpecifier, Phasing.EOS_ACK_BQ, selfId, distressMmsi, nature,
            distressCoordinates, time, subsequentCommunications, address,
            expansionPosition);
    }

    @Override
    public String toString() {
        return "DistressRelayAck{" +
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
