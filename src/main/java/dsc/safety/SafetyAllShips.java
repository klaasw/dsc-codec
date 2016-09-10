package dsc.safety;

import dsc.AllShipsCall;
import dsc.entities.Frequency;
import dsc.entities.Mmsi;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class SafetyAllShips extends AllShipsCall {

    public SafetyAllShips(Mmsi selfId, FirstTelecommand ft,
                          SecondTelecommand st, Frequency freqRx,
                          Optional<Frequency> freqTx) {
        super(selfId, Category.SAFETY, ft, st, freqRx,freqTx);
    }

    public SafetyAllShips(Mmsi selfId, FirstTelecommand ft,
                          SecondTelecommand st, Frequency freqRx) {
        super(selfId, Category.SAFETY, ft, st, freqRx, Optional.empty());
    }

    @Override
    public String toString() {
        return "SafetyAllShips{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", frequencyRx=" + getFrequencyRx() +
            ", frequencyTx=" + getFrequencyTx() +
            ", eos=" + getEos() +
            '}';
    }
}
