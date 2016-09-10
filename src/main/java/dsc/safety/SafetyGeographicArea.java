package dsc.safety;

import dsc.GeographicArea;
import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class SafetyGeographicArea extends GeographicArea {

    public SafetyGeographicArea(Mmsi selfId, Area area,
                                FirstTelecommand ft,
                                SecondTelecommand st,
                                Frequency freqRx,
                                Optional<Frequency> freqTx) {
        super(selfId, area, Category.SAFETY, ft, st, freqRx, freqTx);
    }

    public SafetyGeographicArea(Mmsi selfId, Area area,
                                FirstTelecommand ft,
                                SecondTelecommand st,
                                Frequency freqRx) {
        super(selfId, area, Category.SAFETY, ft, st, freqRx, Optional.empty());
    }

    @Override
    public String toString() {
        return "SafetyGeographicArea{" +
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
