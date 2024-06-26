/*
 * Engine Pi ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2024 Josef Friedrich and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.pirckheimer_gymnasium.engine_pi.actor;

import java.awt.Color;

import de.pirckheimer_gymnasium.engine_pi.Scene;
import de.pirckheimer_gymnasium.engine_pi.Vector;
import de.pirckheimer_gymnasium.engine_pi.annotations.API;

/**
 * Erzeugt verschiedene {@link Actor}-Objekte und fügt sie gleich zur Szene bzw.
 * zur Ebene hinzu.
 *
 * <p>
 * Mit Hilfe dieses Interfaces können die Klassen {@link Scene} and
 * {@link de.pirckheimer_gymnasium.engine_pi.Layer Layer} um einige
 * Hilfsmethoden erweitert werden, ohne sie dabei mit vielen weiteren Methoden
 * zu überfrachten. Die erzeugten {@link Actor}-Objekt werden gleich zur Szene
 * bzw. zur Ebene hinzugefügt.
 * </p>
 *
 * @author Josef Friedrich
 *
 * @see Scene
 * @see de.pirckheimer_gymnasium.engine_pi.Layer
 */
public interface ActorAdder
{
    Scene getScene();
    /* ___ Circle (Kreis) ___________________________________________________ */

    /**
     * Erzeugt einen <b>Kreis</b> durch Angabe des <b>Durchmessers</b> und der
     * <b>Position</b>.
     *
     * @param diameter Der Durchmesser des Kreises.
     * @param x        Die neue <code>x</code>-Koordinate.
     * @param y        Die neue <code>y</code>-Koordinate.
     *
     * @return Ein Kreis, der bereits zur Szene hinzugefügt wurde.
     *
     * @see Circle
     */
    default Circle addCircle(double diameter, double x, double y)
    {
        Circle actor = new Circle(diameter);
        actor.setPosition(x, y);
        getScene().add(actor);
        return actor;
    }

    /**
     * Erzeugt einen <b>Kreis</b> mit <b>einem Meter Durchmesser</b> durch
     * Angabe der <b>Position</b>.
     *
     * @param x Die neue <code>x</code>-Koordinate.
     * @param y Die neue <code>y</code>-Koordinate.
     *
     * @return Ein Kreis, der bereits zur Szene hinzugefügt wurde.
     *
     * @see Circle
     */
    default Circle addCircle(double x, double y)
    {
        return addCircle(1, x, y);
    }

    /**
     * Erzeugt einen <b>Kreis</b> mit <b>einem Meter Durchmesser</b> durch
     * Angabe der <b>Position</b> und der <b>Farbe</b>.
     *
     * @param x     Die neue <code>x</code>-Koordinate.
     * @param y     Die neue <code>y</code>-Koordinate.
     * @param color Die Farbe des Kreises.
     *
     * @return Ein Kreis, der bereits zur Szene hinzugefügt wurde.
     *
     * @see Circle
     */
    default Circle addCircle(double x, double y, Color color)
    {
        Circle actor = addCircle(1, x, y);
        actor.setColor(color);
        return actor;
    }

    /**
     * Erzeugt einen <b>Kreis</b> mit <b>einem Meter Durchmesser</b>.
     *
     * @return Ein Kreis, der bereits zur Szene hinzugefügt wurde.
     *
     * @see Circle
     */
    default Circle addCircle()
    {
        return addCircle(1, 0, 0);
    }

    /**
     * Erzeugt einen <b>Kreis</b> durch Angabe des <b>Durchmessers</b>.
     *
     * @param diameter Der Durchmesser des Kreises.
     *
     * @return Ein Kreis, der bereits zur Szene hinzugefügt wurde.
     *
     * @see Circle
     */
    default Circle addCircle(double diameter)
    {
        return addCircle(diameter, 0, 0);
    }
    /* ___ Hexagon (Sechseck) _____________________________________________ */

    default Hexagon addHexagon(double radius)
    {
        Hexagon actor = new Hexagon(radius);
        getScene().add(actor);
        return actor;
    }

    default Hexagon addHexagon(double radius, double x, double y)
    {
        Hexagon actor = addHexagon(radius);
        actor.setPosition(x, y);
        return actor;
    }

    default Hexagon addHexagon(double x, double y)
    {
        return addHexagon(0.5, x, y);
    }

    default Hexagon addHexagon(double x, double y, String color)
    {
        Hexagon actor = addHexagon(x, y);
        actor.setColor(color);
        return actor;
    }

    default Hexagon addHexagon(double radius, double x, double y, String color)
    {
        Hexagon actor = addHexagon(radius, x, y);
        actor.setColor(color);
        return actor;
    }
    /* ___ Image (Bild) _____________________________________________________ */

    default Image addImage(String filePath, double pixelPerMeter)
    {
        Image actor = new Image(filePath, pixelPerMeter);
        getScene().add(actor);
        return actor;
    }

    default Image addImage(String filePath, double width, double height)
    {
        Image actor = new Image(filePath, width, height);
        getScene().add(actor);
        return actor;
    }
    /* ___ Pentagon (Fünfeck) _______________________________________________ */

    default Pentagon addPentagon(double radius)
    {
        Pentagon actor = new Pentagon(radius);
        getScene().add(actor);
        return actor;
    }

    default Pentagon addPentagon(double radius, double x, double y)
    {
        Pentagon actor = addPentagon(radius);
        actor.setPosition(x, y);
        return actor;
    }

    default Pentagon addPentagon(double x, double y)
    {
        return addPentagon(0.5, x, y);
    }
    /* ___ Rectangle (Rechteck) _____________________________________________ */

    /**
     * Erzeugt ein Rechteck durch Angabe der Breite und Höhe sowie der Position.
     *
     * @param width  Die Breite des Rechtecks.
     * @param height Die Höhe des Rechtecks.
     * @param x      Die neue <code>x</code>-Koordinate.
     * @param y      Die neue <code>y</code>-Koordinate.
     *
     * @return Ein Rechteck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Rectangle#Rectangle(double, double)
     */
    default Rectangle addRectangle(double width, double height, double x,
            double y)
    {
        Rectangle actor = new Rectangle(width, height);
        actor.setPosition(x, y);
        getScene().add(actor);
        return actor;
    }

    /**
     * Erzeugt ein Rechteck durch Angabe der Breite und Höhe.
     *
     * @param width  Die Breite des Rechtecks.
     * @param height Die Höhe des Rechtecks.
     *
     * @return Ein Rechteck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Rectangle#Rectangle(double, double)
     */
    default Rectangle addRectangle(double width, double height)
    {
        return addRectangle(width, height, 0, 0);
    }

    /**
     * Erzeugt ein Quadrat mit der Seitenlängen von einem Meter an eine bestimme
     * Position.
     *
     * @param x Die neue <code>x</code>-Koordinate.
     * @param y Die neue <code>y</code>-Koordinate.
     *
     * @return Ein Rechteck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Rectangle#Rectangle()
     */
    default Rectangle addRectangle(int x, int y)
    {
        return addRectangle(1, 1, x, y);
    }
    /* ___ Triangle (Dreieck) _______________________________________________ */

    /**
     * Erzeugt ein neues Dreieck mit der Höhe und Breite von einem Meter, das an
     * eine bestimmte Position gesetzt wird.
     *
     * @param x Die neue <code>x</code>-Koordinate.
     * @param y Die neue <code>y</code>-Koordinate.
     *
     * @return Ein Dreieck, das bereits zur Szene hinzugefügt wurde.
     */
    default Triangle addTriangle(int x, int y)
    {
        Triangle actor = addTriangle(1.0, 1.0);
        actor.setPosition(x, y);
        return actor;
    }

    /**
     * Erzeugt ein neues Dreieck durch Angabe von drei Punkten.
     *
     * @param point1 Die Koordinate des ersten Eckpunkts.
     * @param point2 Die Koordinate des zweiten Eckpunkts.
     * @param point3 Die Koordinate des dritten Eckpunkts.
     *
     * @return Ein Dreieck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Triangle#Triangle(Vector, Vector, Vector)
     */
    default Triangle addTriangle(Vector point1, Vector point2, Vector point3)
    {
        Triangle actor = new Triangle(point1, point2, point3);
        getScene().add(actor);
        return actor;
    }

    /**
     * Erzeugt ein neues Dreieck durch Angabe der x- und y-Koordinate von drei
     * Punkten.
     *
     * @param x1 Die x-Koordinate des ersten Eckpunkts.
     * @param y1 Die y-Koordinate des ersten Eckpunkts.
     * @param x2 Die x-Koordinate des zweiten Eckpunkts.
     * @param y2 Die y-Koordinate des zweiten Eckpunkts.
     * @param x3 Die x-Koordinate des dritten Eckpunkts.
     * @param y3 Die y-Koordinate des dritten Eckpunkts.
     *
     * @return Ein Dreieck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Triangle#Triangle(double, double, double, double, double, double)
     */
    @API
    default Triangle addTriangle(double x1, double y1, double x2, double y2,
            double x3, double y3)
    {
        Triangle actor = new Triangle(x1, y1, x2, y2, x3, y3);
        getScene().add(actor);
        return actor;
    }

    /**
     * Erzeugt ein gleichschenkliges Dreieck, dessen Symmetrieachse vertikal
     * ausgerichtet ist. Die Spitze zeigt nach oben.
     *
     * @param width  Die Breite des gleichschenkligen Dreiecks - genauer gesagt
     *               die Länge der Basis.
     * @param height Die Höhe der Symmetrieachse.
     *
     * @return Ein Dreieck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Triangle#Triangle(double, double)
     */
    default Triangle addTriangle(double width, double height)
    {
        Triangle actor = new Triangle(width, height);
        getScene().add(actor);
        return actor;
    }

    /**
     * Erzeugt ein gleichseitiges Dreieck. Die Spitze zeigt nach oben.
     *
     * @param sideLength Die Seitenlänge des gleichseitigen Dreiecks.
     *
     * @return Ein Dreieck, das bereits zur Szene hinzugefügt wurde.
     *
     * @see Triangle#Triangle(double, double)
     */
    default Triangle addTriangle(double sideLength)
    {
        Triangle actor = new Triangle(sideLength);
        getScene().add(actor);
        return actor;
    }
    /* ___ RegularPolygon (Reguläres Vieleck) _______________________________ */

    default RegularPolygon addRegularPolygon(int numSides, double radius)
    {
        RegularPolygon actor = new RegularPolygon(numSides, radius);
        getScene().add(actor);
        return actor;
    }

    default RegularPolygon addRegularPolygon(int numSides)
    {
        return addRegularPolygon(numSides, 1);
    }

    default RegularPolygon addRegularPolygon(int numSides, double radius,
            double x, double y)
    {
        RegularPolygon actor = addRegularPolygon(numSides, radius);
        actor.setPosition(x, y);
        return actor;
    }

    default RegularPolygon addRegularPolygon(int numSides, double x, double y)
    {
        return addRegularPolygon(numSides, 1, x, y);
    }
    /* ___ Text _____________________________________________________________ */

    default Text addText(String content, double height)
    {
        Text actor = new Text(content, height);
        getScene().add(actor);
        return actor;
    }

    default Text addText(String content)
    {
        return addText(content, 1);
    }

    default Text addText(String content, double height, double x, double y)
    {
        Text actor = addText(content, height);
        actor.setPosition(x, y);
        return actor;
    }

    default Text addText(String content, double x, double y)
    {
        return addText(content, 1, x, y);
    }
}
