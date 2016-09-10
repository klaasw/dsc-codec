package dsc.routine;

import dsc.IndividualCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class RoutineIndividual extends IndividualCall {

    public RoutineIndividual(Mmsi selfId, Mmsi address,
                             FirstTelecommand firstTelecommand,
                             SecondTelecommand secondTelecommand,
                             Optional<Frequency> frequencyRx,
                             Optional<Frequency> frequencyTx,
                             Optional<Coordinates> coordinates) {
        super(Phasing.EOS_ACK_RQ, selfId, address, Category.ROUTINE,
            firstTelecommand, secondTelecommand, frequencyRx, frequencyTx,
            coordinates, Optional.empty());
    }

    @Override
    public String toString() {
        return "RoutineIndividual{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", address=" + getAddress() +
            ", category=" + getCategory() +
            ", firstTelecommand=" + getFirstTelecommand() +
            ", secondTelecommand=" + getSecondTelecommand() +
            ", frequencyRx=" + getFrequencyRx() +
            ", frequencyTx=" + getFrequencyTx() +
            ", coordinates=" + getCoordinates() +
            ", eos=" + getEos() +
            '}';
    }

}
