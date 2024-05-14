/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha/src/main/java/ea/actor/Actor.java
 *
 * Engine Omega ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2018 Michael Andonie and contributors.
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
package rocks.friedrich.engine_omega.actor;

import java.awt.Graphics2D;
import java.util.function.Supplier;

import rocks.friedrich.engine_omega.FixtureBuilder;
import rocks.friedrich.engine_omega.annotations.API;
import rocks.friedrich.engine_omega.physics.FixtureData;

/**
 * Beschreibt ein Rechteck.
 *
 * @author Michael Andonie
 * @author Niklas Keller
 */
public class Rectangle extends Geometry
{
    /**
     * Die Breite
     */
    private double width;

    /**
     * Die Höhe
     */
    private double height;

    /**
     * Für abgerundete Ecken, Prozent der Abrundung der kleineren Seite
     */
    private double borderRadius;

    /**
     * Konstruktor.
     *
     * @param width  Die Breite des Rechtecks
     * @param height Die Höhe des Rechtecks
     */
    public Rectangle(double width, double height)
    {
        this(width, height, () -> FixtureBuilder
                .createSimpleRectangularFixture(width, height));
    }

    public Rectangle(double width, double height,
            Supplier<FixtureData> shapeSupplier)
    {
        super(shapeSupplier);
        assertPositiveWidthAndHeight(width, height);
        this.width = width;
        this.height = height;
    }

    @API
    public double getWidth()
    {
        return width;
    }

    @API
    public double getHeight()
    {
        return height;
    }

    /**
     * Setzt die Höhe und Breite des Rechtecks neu. Ändert die physikalischen
     * Eigenschaften (Masse etc.).
     *
     * @param width  Neue Breite für das Rechteck.
     * @param height Neue Höhe für das Rechteck.
     */
    @API
    public void setSize(double width, double height)
    {
        assertPositiveWidthAndHeight(width, height);
        this.width = width;
        this.height = height;
        this.setFixture(() -> FixtureBuilder
                .createSimpleRectangularFixture(width, height));
    }

    @API
    public double getBorderRadius()
    {
        return borderRadius;
    }

    @API
    public void setBorderRadius(double percent)
    {
        if (percent < 0 || percent > 1)
        {
            throw new IllegalArgumentException(
                    "Borderradius kann nur zwischen 0 und 1 sein. War "
                            + percent);
        }
        this.borderRadius = percent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Graphics2D g, double pixelPerMeter)
    {
        g.setColor(getColor());
        if (borderRadius == 0)
        {
            g.fillRect(0, (int) (-height * pixelPerMeter),
                    (int) (width * pixelPerMeter),
                    (int) (height * pixelPerMeter));
        }
        else
        {
            int borderRadius = (int) (Math.min(width, height) * pixelPerMeter
                    * this.borderRadius);
            g.fillRoundRect(0, (int) (-height * pixelPerMeter),
                    (int) (width * pixelPerMeter),
                    (int) (height * pixelPerMeter), borderRadius, borderRadius);
        }
    }
}
