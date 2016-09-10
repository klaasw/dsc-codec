package dsc;

import dsc.entities.*;
import dsc.structure.*;

import java.util.ArrayList;
import java.util.List;

/**
 * "Semi-auto" call.
 *
 * @author AlexeyVorobyev
 */
abstract public class SemiAutoCall extends DigitalSelectiveCall {

    private final Mmsi address;

    private final Category category = Category.ROUTINE;

    private final FirstTelecommand firstTelecommand;

    private final SecondTelecommand secondTelecommand;

    private final PstnNumber pstnNumber;

    public SemiAutoCall(Phasing eos, Mmsi selfId, Mmsi address,
                        FirstTelecommand firstTelecommand,
                        SecondTelecommand secondTelecommand,
                        PstnNumber pstnNumber) {
        super(FormatSpecifier.IDIVIDUAL_SEMI_AUTOMATIC, eos, selfId);
        this.address = address;
        this.firstTelecommand = firstTelecommand;
        this.secondTelecommand = secondTelecommand;
        this.pstnNumber = pstnNumber;
    }

    protected abstract List<Code> getAdditionalCodes();

    @Override
    protected List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        codes.add(getFormatSpecifier().getCode());
        codes.addAll(address.toCodes());
        codes.add(category.getCode());
        codes.addAll(getSelfId().toCodes());
        codes.add(firstTelecommand.getCode());
        codes.add(secondTelecommand.getCode());

        codes.addAll(getAdditionalCodes());

        // Addinf of PSTN number
        codes.add(new Code(DscMarkers.pstnMarker));
        codes.addAll(pstnNumber.toCodes());

        codes.add(getEos().getCode());

        codes.add(calculateEccFromInfoCodes(codes));

        codes.add(0, getFormatSpecifier().getCode());

        return codes;
    }

    public Mmsi getAddress() {
        return address;
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

    public PstnNumber getPstnNumber() {
        return pstnNumber;
    }
}
