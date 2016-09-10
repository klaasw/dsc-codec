package dsc.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Представляет собой обычные координаты, без расширенной последовательности.
 *
 * @author AlexeyVorobyev
 */
public final class Coordinates implements Transmittable {

    /** Долгота. Состоит из 5 цифр: градусов(3) и минут(2) */
    private final String longitude;

    /** Широта. Состоит из 4 цифр: градусов(2) и минут(2) */
    private final String latitude;

    private final Quadrant quadrant;

    /**
     * Создаёт класс координат
     *
     * @param longitude долгота (5 цифр)
     * @param latitude  широта (4 цифры)
     * @param quadrant  квадрант
     */
    public Coordinates(String longitude, String latitude, Quadrant quadrant) {
        if (longitude.length() == 5 && latitude.length() == 4) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.quadrant = quadrant;
        } else throw new IllegalArgumentException(
            "Долгота(longitude) должна состоять из 5 цифр, " +
                "а широта(latitude) из 4!"
        );
    }

    public Coordinates(String longitude, String latitude) {
        this(longitude, latitude, Quadrant.NE);
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Quadrant getQuadrant() {
        return quadrant;
    }

    public enum Quadrant implements Serializable {

        NE, NW, SE, SW;

        public int getCode() {
            switch (this) {
                case NE: return 0;
                case NW: return 1;
                case SE: return 2;
                case SW: return 3;
                default: throw new IllegalArgumentException();
            }
        }

        public static Quadrant fromString(String s) {
            switch (s) {
                case "NE": return NE;
                case "NW": return NW;
                case "SE": return SE;
                case "SW": return SW;
                default: throw new IllegalArgumentException(
                    "Квадранта " + s + " не существует!");
            }
        }

        public static Quadrant fromCode(int code) {
            switch (code) {
                case 0: return NE;
                case 1: return NW;
                case 2: return SE;
                case 3: return SW;
                default: throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case NE: return "NE";
                case NW: return "NW";
                case SE: return "SE";
                case SW: return "SW";
                default: throw new IllegalArgumentException();
            }
        }
    }

    /** @see Transmittable#toCodes()  */
    @Override
    public List<Code> toCodes() {
        List<Code> signs = new ArrayList<>();

        signs.add(new Code(quadrant.getCode() * 10
            + Integer.parseInt(latitude.substring(0, 1))));

        signs.add(new Code(Integer.parseInt(latitude.substring(1, 2)) * 10
            + Integer.parseInt(latitude.substring(2, 3))
        ));

        signs.add(new Code(Integer.parseInt(latitude.substring(3, 4)) * 10
            + Integer.parseInt(longitude.substring(0, 1))));

        signs.add(new Code(Integer.parseInt(longitude.substring(1, 2)) * 10
            + Integer.parseInt(longitude.substring(2, 3))));

        signs.add(new Code(Integer.parseInt(longitude.substring(3, 4)) * 10
            + Integer.parseInt(longitude.substring(4, 5))));

        return signs;
    }

    /**
     * Возвращает декодированные координаты из 5 символов.
     *
     * Исключения из-за того, что непонятно что может пойти не так.
     *
     * @param codes коды
     * @return координаты
     */
    private static Coordinates fromCodes5(List<Code> codes) {
        Quadrant quadrant = Quadrant.fromCode(
            codes.get(0).getSymbol() / 10
        );

        int lval = (codes.get(0).getSymbol() - ((codes.get(0).getSymbol()
            / 10) * 10)) * 1000 + codes.get(1).getSymbol() * 10 +
            codes.get(2).getSymbol() / 10;

        String latitude = "";
        if ((lval / 100) < 10) latitude += "0";
        latitude += String.valueOf((double) lval / 100);
        if ((lval - (lval / 100) * 100) < 10) latitude += "0";
        latitude += "00";

        lval = (codes.get(2).getSymbol() - ((codes.get(2).getSymbol() / 10)
            * 10)) * 10000 + codes.get(3).getSymbol() * 100 + codes
            .get(4).getSymbol();
        String longitude = "";
        if (lval / 100 < 100) longitude += "0";
        if (lval / 100 < 10) longitude += "0";
        longitude += String.valueOf((double) lval / 100);

        if (lval - (lval / 100) * 100 < 10) longitude += "0";
        longitude += "00";

        return new Coordinates(longitude, latitude, quadrant);
    }

    /**
     * Возвращает декодированные координаты из 3 символов.
     *
     * Исключения из-за того, что непонятно что может пойти не так.
     *
     * @param codes коды
     * @return координаты
     */
    private static Coordinates fromCodes3(List<Code> codes) {
        Quadrant quadrant = Coordinates.Quadrant.fromCode(
            codes.get(0).getSymbol() / 10
        );

        int lval = (codes.get(0).getSymbol() -
            ((codes.get(0).getSymbol() / 10) * 10)) * 10 +
            codes.get(1).getSymbol() / 10;

        String latitude = "";
        latitude += String.valueOf(lval);

        lval = (codes.get(1).getSymbol() - ((codes.get(1).getSymbol() /
            10) * 10)) * 100 + codes.get(2).getSymbol();

        String longitude = "";
        longitude += String.valueOf(lval);

        return new Coordinates(longitude, latitude, quadrant);
    }

    /**
     * Возвращает декодированные координаты из символов.
     *
     * При получении координат по умолчанию выводит их как получил.
     *
     * @param codes коды
     * @return координаты
     */
    public static Coordinates fromCodes(List<Code> codes) {
        int quadrantCode = codes.get(0).getSymbol() / 10;
        boolean quadrantExists = (quadrantCode > 0 && quadrantCode < 3);

        // Координаты по умолчанию (не определены)
        if (!quadrantExists)
            return new Coordinates("99999", "9999", Quadrant.NE);

        if (codes.size() == 5) return fromCodes5(codes);
        else if (codes.size() == 3) return fromCodes3(codes);
        else throw new IllegalArgumentException("Количество символов для " +
                "декодирования должно быть 3 или 5. Получено: " + codes.size());
    }

    @Override
    public String toString() {
        String q = getQuadrant().toString();

        String lon = longitude.substring(0, 3) + "'"
            + longitude.substring(3, 5) + q.charAt(0);

        String lat = latitude.substring(0, 2) + "'" +
            latitude.substring(2, 4) + q.charAt(1);

        return lon + " " + lat;
    }
}
