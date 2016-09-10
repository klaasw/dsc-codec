package dsc.distress;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

import static dsc.structure.Category.DISTRESS;
import static dsc.structure.FirstTelecommand.DISTRESS_ACK;

/** @author AlexeyVorobyev */
public final class DistressAck extends DigitalSelectiveCall
    implements PossibleExpansion {

    private final Category category = DISTRESS;

    private final FirstTelecommand telecommand = DISTRESS_ACK;

    private final Mmsi distressMmsi;

    private final DistressNature nature;

    private final Coordinates distressCoordinates;

    private final TimeUTC time;

    private final FirstTelecommand subsequentCommunications;

    private final Optional<ExpansionPosition> expansionPosition;

    public DistressAck(Mmsi selfId, Mmsi distressMmsi,
                       DistressNature nature,
                       Coordinates distressCoordinates, TimeUTC time,
                       FirstTelecommand subsequentCommunications,
                       Optional<ExpansionPosition> expansionPosition) {
        super(FormatSpecifier.ALL_SHIPS, Phasing.EOS, selfId);

        this.distressMmsi = distressMmsi;
        this.nature = nature;
        this.distressCoordinates = distressCoordinates;
        this.time = time;
        this.subsequentCommunications = subsequentCommunications;
        this.expansionPosition = expansionPosition;
    }

    public Category getCategory() {
        return category;
    }

    public FirstTelecommand getTelecommand() {
        return telecommand;
    }

    public Mmsi getDistressMmsi() {
        return distressMmsi;
    }

    public DistressNature getNature() {
        return nature;
    }

    public Coordinates getDistressCoordinates() {
        return distressCoordinates;
    }

    public TimeUTC getTime() {
        return time;
    }

    public FirstTelecommand getSubsequentCommunications() {
        return subsequentCommunications;
    }

    public Optional<ExpansionPosition> getExpansionPosition() {
        return expansionPosition;
    }

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(telecommand.getCode());
        codes.addAll(distressMmsi.toCodes());
        codes.add(nature.getCode());
        codes.addAll(distressCoordinates.toCodes());
        codes.addAll(time.toCodes());
        codes.add(subsequentCommunications.getCode());
        codes.add(Phasing.EOS.getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }

    @Override
    public Optional<ExpansionPosition> getExpansion() {
        return expansionPosition;
    }

    @Override
    public String toString() {
        return "DistressAck{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", category=" + category +
            ", telecommand=" + telecommand +
            ", distressMmsi=" + distressMmsi +
            ", nature=" + nature +
            ", distressCoordinates=" + distressCoordinates +
            ", time=" + time +
            ", subsequentCommunications=" + subsequentCommunications +
            ", expansion=" + expansionPosition +
            ", eos=" + getEos() +
            '}';
    }
}
