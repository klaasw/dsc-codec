package dsc.safety;

import dsc.IndividualCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class SafetyIndividual extends IndividualCall {

    public SafetyIndividual(Mmsi selfId, Mmsi address,
                            FirstTelecommand firstTelecommand,
                            SecondTelecommand secondTelecommand,
                            Optional<Frequency> frequencyRx,
                            Optional<Frequency> frequencyTx,
                            Optional<Coordinates> coordinates,
                            Optional<TimeUTC> time) {
        super(Phasing.EOS_ACK_RQ, selfId, address, Category.SAFETY,
            firstTelecommand, secondTelecommand, frequencyRx, frequencyTx,
            coordinates, time);
    }

    @Override
    public String toString() {
        return "SafetyIndividual{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", address=" + getAddress() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", frequencyRx=" + getFrequencyRx() +
            ", frequencyTx=" + getFrequencyTx() +
            ", coordinates=" + getCoordinates() +
            ", time=" + getTime() +
            ", eos=" + getEos() +
            '}';
    }
}
