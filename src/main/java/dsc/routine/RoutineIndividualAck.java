package dsc.routine;

import dsc.IndividualCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.Optional;

/** @author AlexeyVorobyev */
public final class RoutineIndividualAck extends IndividualCall {

    public RoutineIndividualAck(Mmsi selfId, Mmsi address,
                                FirstTelecommand firstTelecommand,
                                SecondTelecommand secondTelecommand,
                                Optional<Frequency> frequencyRx,
                                Optional<Frequency> frequencyTx,
                                Optional<Coordinates> coordinates) {
        super(Phasing.EOS_ACK_BQ, selfId, address, Category.ROUTINE,
            firstTelecommand, secondTelecommand, frequencyRx, frequencyTx,
            coordinates, Optional.empty());
    }

    @Override
    public String toString() {
        return "RoutineIndividualAck{" +
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
