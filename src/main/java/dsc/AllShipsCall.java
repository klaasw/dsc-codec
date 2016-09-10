package dsc;

import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

/**
 * "All ships" type of call.
 *
 * Used for "Safety" and "Urgency" calls.
 *
 * @author AlexeyVorobyev
 */
public abstract class AllShipsCall extends DigitalSelectiveCall {

    private final Category category;

    private final FirstTelecommand firstTelecommand;

    private final SecondTelecommand secondTelecommand;

    private final Frequency frequencyRx;

    private final Optional<Frequency> frequencyTx;

    public AllShipsCall(Mmsi selfId, Category category,
                        FirstTelecommand firstTelecommand,
                        SecondTelecommand secondTelecommand,
                        Frequency frequencyRx,
                        Optional<Frequency> frequencyTx) {
        super(FormatSpecifier.ALL_SHIPS, Phasing.EOS, selfId);
        this.category = category;
        this.firstTelecommand = firstTelecommand;
        this.secondTelecommand = secondTelecommand;
        this.frequencyRx = frequencyRx;
        this.frequencyTx = frequencyTx;
    }

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
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
}
