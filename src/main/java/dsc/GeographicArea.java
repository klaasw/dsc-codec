package dsc;

import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

/**
 * Geographical area calls.
 *
 * @author AlexeyVorobyev
 */
public abstract class GeographicArea extends DigitalSelectiveCall {

    private final Area area;

    private final Category category;

    private final FirstTelecommand firstTelecommand;

    private final SecondTelecommand secondTelecommand;

    private final Frequency frequencyRx;

    private final Optional<Frequency> frequencyTx;

    public GeographicArea(Mmsi selfId, Area area, Category category,
                          FirstTelecommand firstTelecommand,
                          SecondTelecommand secondTelecommand,
                          Frequency frequencyRx,
                          Optional<Frequency> frequencyTx) {
        super(FormatSpecifier.GEOGRAPHICAL_AREA, Phasing.EOS, selfId);
        this.area = area;
        this.category = category;
        this.firstTelecommand = firstTelecommand;
        this.secondTelecommand = secondTelecommand;
        this.frequencyRx = frequencyRx;
        this.frequencyTx = frequencyTx;
    }

    public Area getArea() {
        return area;
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

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.addAll(area.toCodes());
        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(firstTelecommand.getCode());
        codes.add(secondTelecommand.getCode());
        codes.addAll(frequencyRx.toCodes());

        if (frequencyTx.isPresent())
            codes.addAll(frequencyTx.get().toCodes());
        else
            codes.addAll(Frequency.getNoInfoCodes());

        codes.add(Phasing.EOS.getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }
}
