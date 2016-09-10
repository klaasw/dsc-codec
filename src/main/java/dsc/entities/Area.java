package dsc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Geographical area.
 *
 * Coding in accordance with Rec. ITU-R M.493-13 p.5.3.
 *
 * Note: coding of coordinate in transsmition of "Geographical area" DSCs
 * is different. We take only degrees.
 *
 * @author AlexeyVorobyev
 */
public final class Area implements Address {

    private final Coordinates coordinates;

    private final int dy;

    private final int dx;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getDy() {
        return dy;
    }

    public int getDx() {
        return dx;
    }

    public Area(Coordinates coordinates, int dy, int dx) {
        this.coordinates = coordinates;
        this.dy = dy;
        this.dx = dx;
    }

    /** @see Transmittable#toCodes()  */
    @Override
    public List<Code> toCodes() {
        List<Code> codes = new ArrayList<>();

        Coordinates.Quadrant quadrant = coordinates.getQuadrant();
        String latitude = coordinates.getLatitude();
        String longitude = coordinates.getLongitude();

        codes.add(new Code(quadrant.getCode() * 10 +
            Integer.parseInt(latitude.substring(0, 1))));

        codes.add(new Code(Integer.parseInt(latitude.substring(1, 2)) * 10 +
            Integer.parseInt(longitude.substring(0, 1))));

        codes.add(new Code(Integer.parseInt(longitude.substring(1, 2)) * 10 +
            Integer.parseInt(longitude.substring(2, 3))));

        codes.add(new Code(dy));
        codes.add(new Code(dx));

        return codes;
    }

    /**
     * Decodes geographical area.
     *
     * We take only degrees.
     */
    public static Area fromCodes(List<Code> codes) {
        Coordinates coordinates = Coordinates.fromCodes(
            codes.subList(0, codes.size() - 2)
        );

        int dy = codes.get(3).getSymbol();
        int dx = codes.get(4).getSymbol();

        return new Area(coordinates, dy, dx);
    }

    @Override
    public String toString() {
        return "Area{" +
            "coordinates=" + coordinates +
            ", dy=" + dy +
            ", dx=" + dx +
            '}';
    }
}
