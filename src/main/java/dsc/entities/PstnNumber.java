package dsc.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PSTN номер.
 *
 * У номера нет фиксированного размера, но есть ограничение до 16 цифр.
 *
 * @author AlexeyVorobyev
 */
public final class PstnNumber implements Transmittable {

    private final String number;

    /**
     * Создаёт PSTN номер
     *
     * @param number строковое представление номера, состоящее из 16 цифр
     */
    public PstnNumber(String number) {
        if (!checkFormat(number)) {
            throw new IllegalArgumentException(
                "PSTN номер должен состоять из цифр! Передано: " + number +
                    "с размером " + number.length()
            );
        }

        this.number = number;
    }

    /**
     * Проверяет, является ли переданная строка номером
     *
     * @param num строка для проверки
     * @return является ли номером
     */
    private boolean checkFormat(String num) {
        String numPattern = "\\d+";
        Pattern pattern = Pattern.compile(numPattern);
        Matcher matcher = pattern.matcher(num);

        return matcher.matches();
    }

    /**
     * Декодирует и возвращает PSTN номер из переданных кодов
     *
     * @param codes 8 кодов для декодирования
     * @return декодированный PSTN
     */
    public static PstnNumber fromCodes(List<Code> codes) {
        String number = codes.stream().reduce(
            "",
            (c1, c2) -> c1 + c2.getSymbol(),
            (c1, c2) -> c1 + c2
        );

        return new PstnNumber(number);
    }

    /**
     * При передаче PSTN номера необходимо передавать 8 символов.
     *
     * В случае, если номер отсутствует, достаточно передать 1 нулевой символ.
     *
     * @see Transmittable#toCodes()
     */
    @Override
    public List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        StringBuilder builder = new StringBuilder(number);
        // Если размер меньше 16, то необходимо дополнить его нулями спереди
        int numLength = number.length();
        if (numLength < 16) {
            while (builder.length() != 16) {
                builder.insert(0, "0");
            }
        }

        String[] numArray = builder.toString().split("(?<=\\G..)");

        for (String s : numArray) {
            codes.add(new Code(Integer.parseInt(s)));
        }

        return codes;
    }

    @Override
    public String toString() {
        return "PstnNumber{" + number + '}';
    }
}
