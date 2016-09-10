package dsc.distress;

import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public class DistressRelay extends AbstractDistressRelay {

    public DistressRelay(FormatSpecifier formatSpecifier, Mmsi selfId,
                         Mmsi distressMmsi, DistressNature nature,
                         Coordinates distressCoordinates, TimeUTC time,
                         FirstTelecommand subsequentCommunications,
                         Optional<Address> address,
                         Optional<ExpansionPosition> expansionPosition) {

        super(formatSpecifier, Phasing.EOS, selfId, distressMmsi, nature,
            distressCoordinates, time, subsequentCommunications, address,
            expansionPosition);
    }

    @Override
    public String toString() {
        return "DistressRelay{" +
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
