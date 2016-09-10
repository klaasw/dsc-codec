package dsc.entities;

import java.util.List;

/**
 * Интерфейс для сущностей, которые могут быть переданы через передатчик в
 * составе ЦИВа.
 *
 * @author AlexeyVorobyev
 */
public interface Transmittable {

    /**
     * Конвертирует сущность в символы для передачи
     *
     * @return символы для передачи
     */
    List<Code> toCodes();
}
