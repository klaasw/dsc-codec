package dsc.structure;

import java.util.Optional;

/**
 * Interface-marker for expanded DSCs.
 *
 * @author AlexeyVorobyev
 */
public interface PossibleExpansion {

    Optional<ExpansionPosition> getExpansion();
}
