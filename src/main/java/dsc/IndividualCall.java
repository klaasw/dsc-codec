package dsc;

import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

/**
 * Individual call.
 *
 * This call may be call with request for acknowledge or acknowledge-call.
 * Difference in EOS symbol.
 *
 * @author AlexeyVorobyev
 */
public abstract class IndividualCall extends DigitalSelectiveCall {

    /** Address of receiver */
    private final Mmsi address;

    /** May be Safety, Urgency и Routine */
    private final Category category;

    private final FirstTelecommand firstTelecommand;

    private final SecondTelecommand secondTelecommand;

    private final Optional<Frequency> frequencyRx;

    private final Optional<Coordinates> coordinates;

    private final Optional<TimeUTC> time;

    private final Optional<Frequency> frequencyTx;

    public IndividualCall(Phasing eos, Mmsi selfId, Mmsi address,
                          Category category,
                          FirstTelecommand firstTelecommand,
                          SecondTelecommand secondTelecommand,
                          Optional<Frequency> frequencyRx,
                          Optional<Frequency> frequencyTx,
                          Optional<Coordinates> coordinates,
                          Optional<TimeUTC> time) {
        super(FormatSpecifier.INDIVIDUAL, eos, selfId);

        this.address = address;
        this.category = category;
        this.firstTelecommand = firstTelecommand;
        this.secondTelecommand = secondTelecommand;
        this.frequencyRx = frequencyRx;
        this.coordinates = coordinates;
        this.time = time;
        this.frequencyTx = frequencyTx;
    }

    public Optional<Frequency> getFrequencyTx() {
        return frequencyTx;
    }

    public Mmsi getAddress() {
        return address;
    }

    public Category getCategory() {
        return category;
    }

    public FirstTelecommand getFirstTelecommand() {
        return firstTelecommand;
    }

    public SecondTelecommand getSecondTelecommand() {
        return secondTelecommand;
    }

    public Optional<Frequency> getFrequencyRx() {
        return frequencyRx;
    }

    public Optional<Coordinates> getCoordinates() {
        return coordinates;
    }

    public Optional<TimeUTC> getTime() {
        return time;
    }

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.addAll(address.toCodes());
        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(firstTelecommand.getCode());
        codes.add(secondTelecommand.getCode());

        if (frequencyRx.isPresent()) {
            codes.addAll(frequencyRx.get().toCodes());
        } else if (coordinates.isPresent()) {
            codes.add(new Code(DscMarkers.posMarker));
            codes.addAll(coordinates.get().toCodes());
        }

        if (frequencyTx.isPresent())
            codes.addAll(frequencyTx.get().toCodes());
        else if (!coordinates.isPresent())
            codes.addAll(Frequency.getNoInfoCodes());

        if (time.isPresent())
            codes.addAll(time.get().toCodes());

        codes.add(getEos().getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }
}
