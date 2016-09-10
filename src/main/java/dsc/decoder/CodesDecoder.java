package dsc.decoder;

import dsc.DigitalSelectiveCall;
import dsc.DscMarkers;
import dsc.entities.Code;
import dsc.structure.Phasing;

import java.util.List;

/**
 * Abstract decoder for symbols of DSC.
 *
 * @author AlexeyVorobyev
 */
abstract class CodesDecoder {

    /**
     * Returns EOS symbol from specified codes.
     *
     * Method expects full sequence of codes of single DSC.
     *
     * @param codes codes of full DSC
     * @return EOS symbol
     */
    static Phasing getEos(List<Code> codes) {
        int eosIndex = codes.indexOf(Phasing.EOS.getCode());
        int eosBq = codes.indexOf(Phasing.EOS_ACK_BQ.getCode());

        if (eosIndex > 0) return Phasing.EOS;
        else if (eosBq > 0) return Phasing.EOS_ACK_BQ;
        else return Phasing.EOS_ACK_RQ;
    }

    /**
     * Checks whether specified code is position marker.
     *
     * If specified code is position marker then it is followed by coordinates.
     */
    static boolean isPosMarker(Code c) {
        return c.getSymbol() == DscMarkers.posMarker;
    }

    /**
     * Checks whether specified code is VHF channel marker.
     *
     * If specified code is VHF channel marker then it is folled by number of
     * VHF channel (two-digit).
     */
    static boolean isVhfChannelMarker(Code c) {
        return c.getSymbol() == DscMarkers.vhfChannelMarker;
    }

    /** Checks wether DSC is expanded */
    static boolean isExpanded(List<Code> codes) {
        int eosIndex = codes.indexOf(getEos(codes).getCode());

        return codes.size() > eosIndex + 2;
    }

    /** Checks wether specified code is marker of "Number" type. */
    static boolean isNumberMarker(Code c) {
        return c.getSymbol() == DscMarkers.pstnMarker;
    }

    /** Checks whether specified code is MF/HF channel marker. */
    static boolean isMfHfChannelMarker(Code c) {
        return (c.getSymbol() / 10) == 3;
    }

    static Code parseSecondSymbolOfCode(Code c) {
        String n = String.valueOf(c.getSymbol());

        return new Code(Integer.parseInt(n.substring(1, 2)));
    }

    /** Checks whether DSC has frequency. */
    static boolean hasFrequency(List<Code> codes) {
        // 126 is marker meaning "No information"
        // 106 is marker meaning that is is followed by number
        return codes.stream().filter(
            (c) -> c.getSymbol() == 126
        ).count() != 3 && codes.get(0).getSymbol() != 106;
    }

    static boolean isEccCorrect(List<Code> codes) {
        int eosIndex = codes.indexOf(getEos(codes).getCode());
        int receivedEcc = codes.get(eosIndex + 1).getSymbol();

        return codes.subList(1, eosIndex + 1).stream().map(
            Code::getSymbol
        ).reduce(0, (s1, s2) -> s1 ^ s2) == receivedEcc;
    }

    static boolean isExpandEccCorrect(List<Code> codes) {
        int eosIndex = codes.indexOf(getEos(codes).getCode());

        if (isExpanded(codes)) {
            int receivedExpanEcc = codes.get(codes.size() - 1).getSymbol();

            return codes.subList(eosIndex + 2, codes.size() - 1).stream().map(
                Code::getSymbol
            ).reduce(0, (s1, s2) -> s1 ^ s2) == receivedExpanEcc;
        }

        return false;
    }

    abstract DigitalSelectiveCall decodeCodes(List<Code> codes);

    /**
     * Checks wether this decoder appropriate for specified format specificator.
     */
    abstract boolean isMatches(Code code);
}
