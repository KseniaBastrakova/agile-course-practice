package ru.unn.agile.Triangle.Model;

import java.awt.geom.Point2D;

public class Triangle {
    public Triangle(final Point2D a, final Point2D b, final Point2D c) {
        if (isDegenerated(a, b, c)) {
            throw new IllegalArgumentException("Triangle is degenerated.");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public final Point2D getPointA() {
        return a;
    }

    public final Point2D getPointB() {
        return b;
    }

    public final Point2D getPointC() {
        return c;
    }

    public final double getPerimeter() {
        return getLengthAB() + getLengthBC() + getLengthAC();
    }

    public final double getLengthAB() {
        return getLength(a, b);
    }

    public final double getLengthBC() {
        return getLength(b, c);
    }

    public final double getLengthAC() {
        return getLength(a, c);
    }

    public final double getSurfaceArea() {
        double p = getSemiperimeter();

        double ab = getLengthAB();
        double bc = getLengthBC();
        double ac = getLengthAC();

        return Math.sqrt(p * (p - ab) * (p - bc) * (p - ac));
    }

    public final double getABCAngle() {
        double ab = getLengthAB();
        double bc = getLengthBC();
        double ac = getLengthAC();

        return Math.acos((bc * bc + ab * ab - ac * ac) / (2 * bc * ab));
    }

    public final double getBCAAngle() {
        return Math.PI - (getABCAngle() + getCABAngle());
    }

    public final double getCABAngle() {
        double ab = getLengthAB();
        double bc = getLengthBC();
        double ac = getLengthAC();

        return Math.acos((ac * ac + ab * ab - bc * bc) / (2 * ac * ab));
    }

    private double getSemiperimeter() {
        return getPerimeter() / 2;
    }

    private boolean isOnOneLine(final Point2D a, final Point2D b, final Point2D c) {
        double lineCoeffK = (b.getY() - a.getY()) / (b.getX() - a.getX());
        double lineCoeffB = a.getY() - lineCoeffK * a.getX();

        return c.getY() == lineCoeffK * c.getX() + lineCoeffB;
    }

    private boolean isDegenerated(final Point2D a, final Point2D b, final Point2D c) {

        if (a.equals(b) || b.equals(c) || c.equals(a)) {
            return true;
        }

        return isOnOneLine(a, b, c);
    }

    private double getLength(final Point2D point1, final Point2D point2) {
        return Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2)
                + Math.pow((point1.getY() - point2.getY()), 2));
    }

    private final Point2D a;
    private final Point2D b;
    private final Point2D c;
}
