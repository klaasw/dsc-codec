package dsc.distress;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

import static dsc.structure.Category.DISTRESS;
import static dsc.structure.FirstTelecommand.DISTRESS_RELAY;

/** @author AlexeyVorobyev */
public abstract class AbstractDistressRelay extends DigitalSelectiveCall
    implements PossibleExpansion {

    private final Optional<Address> address;

    private final Category category = DISTRESS;

    private final FirstTelecommand telecommand = DISTRESS_RELAY;

    private final Mmsi distressMmsi;

    private final DistressNature nature;

    private final Coordinates distressCoordinates;

    private final TimeUTC time;

    private final FirstTelecommand subsequentCommunications;

    private final Optional<ExpansionPosition> expansionPosition;

    public AbstractDistressRelay(FormatSpecifier formatSpecifier, Phasing eos,
                                 Mmsi selfId, Mmsi distressMmsi,
                                 DistressNature nature,
                                 Coordinates distressCoordinates, TimeUTC time,
                                 FirstTelecommand subsequentCommunications,
                                 Optional<Address> address,
                                 Optional<ExpansionPosition> expansionPosition) {
        super(formatSpecifier, eos, selfId);

        this.address = address;
        this.distressMmsi = distressMmsi;
        this.nature = nature;
        this.distressCoordinates = distressCoordinates;
        this.time = time;
        this.subsequentCommunications = subsequentCommunications;
        this.expansionPosition = expansionPosition;
    }

    public Optional<Address> getAddress() {
        return address;
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

    @Override
    public Optional<ExpansionPosition> getExpansion() {
        return expansionPosition;
    }

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());

        if (address.isPresent())
            codes.addAll(address.get().toCodes());

        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(telecommand.getCode());
        codes.addAll(distressMmsi.toCodes());
        codes.add(nature.getCode());
        codes.addAll(distressCoordinates.toCodes());
        codes.addAll(time.toCodes());
        codes.add(subsequentCommunications.getCode());
        codes.add(getEos().getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }
}
