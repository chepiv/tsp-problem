package model;

/**
 * Created by chepiv on 08/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class City {

    private final double x,y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "City{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
