package dsc.urgency;

import dsc.GeographicArea;
import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class UrgencyGeographicArea extends GeographicArea {

    public UrgencyGeographicArea(Mmsi selfId, Area area,
                                 FirstTelecommand ft,
                                 SecondTelecommand st,
                                 Frequency freqRx,
                                 Optional<Frequency> freqTx) {
        super(selfId, area, Category.URGENCY, ft, st, freqRx, freqTx);
    }

    public UrgencyGeographicArea(Mmsi selfId, Area area,
                                 FirstTelecommand ft,
                                 SecondTelecommand st,
                                 Frequency freqRx) {
        super(selfId, area, Category.URGENCY, ft, st, freqRx, Optional.empty());
    }

    @Override
    public String toString() {
        return "UrgencyGeographicArea{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", area=" + getArea() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", frequencyRx=" + getFrequencyRx() +
            ", frequencyTx=" + getFrequencyTx() +
            ", eos=" + getEos() +
            '}';
    }
}
