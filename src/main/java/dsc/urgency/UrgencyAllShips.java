package dsc.urgency;

import dsc.AllShipsCall;
import dsc.entities.Frequency;
import dsc.entities.Mmsi;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class UrgencyAllShips extends AllShipsCall {

    public UrgencyAllShips(Mmsi selfId, FirstTelecommand ft,
                           SecondTelecommand st, Frequency freqRx,
                           Optional<Frequency> freqTx) {
        super(selfId, Category.URGENCY, ft, st, freqRx, freqTx);
    }

    public UrgencyAllShips(Mmsi selfId, FirstTelecommand ft,
                           SecondTelecommand st, Frequency freqRx) {
        super(selfId, Category.URGENCY, ft, st, freqRx, Optional.empty());
    }

    @Override
    public String toString() {
        return "UrgencyAllShips{" +
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
