package dsc.distress;

import dsc.DigitalSelectiveCall;
import dsc.entities.*;
import dsc.structure.*;

import java.util.*;

import static dsc.structure.FirstTelecommand.*;

/** @author AlexeyVorobyev */
public final class Distress extends DigitalSelectiveCall
    implements PossibleExpansion {

    /** Характер бедствия. 112 */
    private final DistressNature nature;

    /** Координаты судна, терпящего бедствие */
    private final Coordinates coordinates;

    /** Время бедствия */
    private final TimeUTC distressTime;

    /** Последующий тип связи */
    private final FirstTelecommand subsequentCommunication;

    private final Optional<ExpansionPosition> expansionPosition;

    public Distress(Mmsi selfId, DistressNature nature,
                    Coordinates coordinates, TimeUTC distressTime,
                    FirstTelecommand subsequentCommunication) {
        super(FormatSpecifier.DISTRESS, Phasing.EOS, selfId);

        if (subsequentCommunication == F3E_G3E_ALL_MODES_TP ||
            subsequentCommunication == J3E_TP ||
            subsequentCommunication == F1B_J2B_TTY_FEC ||
            subsequentCommunication == NO_INFO) {
            this.nature = nature;
            this.coordinates = coordinates;
            this.distressTime = distressTime;
            this.subsequentCommunication = subsequentCommunication;
            this.expansionPosition = Optional.empty();
        } else {
            throw new IllegalArgumentException("Неверно указан последующий " +
                "тип связи! Указан: " + subsequentCommunication);
        }
    }

    public Distress(Mmsi selfId, DistressNature nature,
                    Coordinates coordinates, TimeUTC distressTime,
                    FirstTelecommand subsequentCommunication,
                    Optional<ExpansionPosition> expansionPosition) {
        super(FormatSpecifier.DISTRESS, Phasing.EOS, selfId);

        if (subsequentCommunication == F3E_G3E_ALL_MODES_TP ||
            subsequentCommunication == J3E_TP ||
            subsequentCommunication == F1B_J2B_TTY_FEC ||
            subsequentCommunication == NO_INFO) {
            this.nature = nature;
            this.coordinates = coordinates;
            this.distressTime = distressTime;
            this.subsequentCommunication = subsequentCommunication;
            this.expansionPosition = expansionPosition;
        } else {
            throw new IllegalArgumentException("Неверно указан последующий " +
                "тип связи! Указан: " + subsequentCommunication);
        }
    }

    /** @see DigitalSelectiveCall#toCodes() */
    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(nature.getCode());
        codes.addAll(coordinates.toCodes());
        codes.addAll(distressTime.toCodes());
        codes.add(subsequentCommunication.getCode());
        codes.add(Phasing.EOS.getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }

    @Override
    public String toString() {
        return "Distress{" +
            "formatSpecifier=" + getFormatSpecifier() +
            ", selfId=" + getSelfId() +
            ", nature=" + nature +
            ", coordinates=" + coordinates +
            ", distressTime=" + distressTime +
            ", subsequentCommunication=" + subsequentCommunication +
            ", expansion=" + expansionPosition +
            ", eos=" + getEos() +
            '}';
    }

    @Override
    public Optional<ExpansionPosition> getExpansion() {
        return expansionPosition;
    }

    public DistressNature getNature() {
        return nature;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public TimeUTC getDistressTime() {
        return distressTime;
    }

    public FirstTelecommand getSubsequentCommunication() {
        return subsequentCommunication;
    }
}
