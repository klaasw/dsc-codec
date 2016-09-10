package dsc.routine;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

import static dsc.structure.Category.ROUTINE;
import static dsc.structure.SecondTelecommand.NO_INFO;

/** @author AlexeyVorobyev */
public final class RoutineGroup extends DigitalSelectiveCall {

    private final Mmsi address;

    private final Category category = ROUTINE;

    private final FirstTelecommand firstTelecommand;

    private final SecondTelecommand secondTelecommand = NO_INFO;

    private final Frequency frequencyRx;

    private final Optional<Frequency> frequencyTx;

    public RoutineGroup(Mmsi selfId, Mmsi address,
                        FirstTelecommand firstTelecommand,
                        Frequency frequencyRx,
                        Optional<Frequency> frequencyTx) {
        super(FormatSpecifier.COMMON_INTEREST, Phasing.EOS, selfId);
        this.address = address;
        this.firstTelecommand = firstTelecommand;
        this.frequencyRx = frequencyRx;
        this.frequencyTx = frequencyTx;
    }

    public RoutineGroup(Mmsi selfId, Mmsi address,
                        FirstTelecommand firstTelecommand,
                        Frequency frequencyRx) {
        super(FormatSpecifier.COMMON_INTEREST, Phasing.EOS, selfId);
        this.address = address;
        this.firstTelecommand = firstTelecommand;
        this.frequencyRx = frequencyRx;
        this.frequencyTx = Optional.empty();
    }

    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.addAll(address.toCodes());
        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(firstTelecommand.getCode());
        codes.add(secondTelecommand.getCode());
        codes.addAll(frequencyRx.toCodes());

        if (frequencyTx.isPresent())
            codes.addAll(frequencyTx.get().toCodes());
        else
            codes.addAll(Frequency.getNoInfoCodes());

        codes.add(getEos().getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
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

    public Frequency getFrequencyRx() {
        return frequencyRx;
    }

    public Optional<Frequency> getFrequencyTx() {
        return frequencyTx;
    }

    @Override
    public String toString() {
        return "RoutineGroup{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", address=" + address +
            ", category=" + category +
            ", firstTelecommand=" + firstTelecommand +
            ", secondTelecommand=" + secondTelecommand +
            ", frequencyRx=" + frequencyRx +
            ", frequencyTx=" + frequencyTx +
            ", eos=" + getEos() +
            '}';
    }
}
