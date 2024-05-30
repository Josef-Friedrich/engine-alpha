/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha/src/main/java/ea/actor/Image.java
 *
 * Engine Omega ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2014 Michael Andonie and contributors.
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import rocks.friedrich.engine_omega.FixtureBuilder;
import rocks.friedrich.engine_omega.Game;
import rocks.friedrich.engine_omega.annotations.API;
import rocks.friedrich.engine_omega.annotations.Internal;

/**
 * Ein Bild als grafische Repräsentation einer Bilddatei, die gezeichnet werden
 * kann.
 *
 * @author Michael Andonie
 */
public class Image extends Actor
{
    /**
     * Das {@link BufferedImage}, das dieses Image darstellt.
     */
    private final BufferedImage image;

    private double width;

    private double height;

    private boolean flipVertical = false;

    private boolean flipHorizontal = false;

    /**
     * Der Konstruktor für ein Bildobjekt.
     *
     * @param filepath Der Verzeichnispfad des Bildes, das geladen werden soll.
     * @param width    Die Breite in M für das Bildobjekt
     * @param height   Die Höhe in M für das Bildobjekt
     */
    @API
    public Image(String filepath, double width, double height)
    {
        super(() -> FixtureBuilder.createSimpleRectangularFixture(width,
                height));
        assertViableSizes(width, height);
        this.image = Game.getImages().get(filepath);
        this.width = width;
        this.height = height;
    }

    /**
     * Konstruktor für ein Bildobjekt.
     *
     * @param filepath      Der Verzeichnispfad des Bildes, das geladen werden
     *                      soll.
     * @param pixelPerMeter Der Umrechnungsfaktor für die Größe des Bildes. Gibt
     *                      an, wie viele Pixel in der Bilddatei einem Meter in
     *                      der Engine entsprechen.
     */
    @API
    public Image(String filepath, final double pixelPerMeter)
    {
        super(() -> FixtureBuilder.createSimpleRectangularFixture(
                Game.getImages().get(filepath).getWidth() / pixelPerMeter,
                Game.getImages().get(filepath).getHeight() / pixelPerMeter));
        assertViablePPM(pixelPerMeter);
        this.image = Game.getImages().get(filepath);
        this.width = image.getWidth() / pixelPerMeter;
        this.height = image.getHeight() / pixelPerMeter;
    }

    /**
     * Konstruktor für ein Bildobjekt.
     *
     * @param image         Ein bereits im Speicher vorhandenes Bild vom
     *                      Datentyp BufferedImage.
     * @param pixelPerMeter Der Umrechnungsfaktor für die Größe des Bildes. Gibt
     *                      an, wie viele Pixel in der Bilddatei einem Meter in
     *                      der Engine entsprechen.
     */
    @API
    public Image(BufferedImage image, final double pixelPerMeter)
    {
        super(() -> FixtureBuilder.createSimpleRectangularFixture(
                image.getWidth() / pixelPerMeter,
                image.getHeight() / pixelPerMeter));
        assertViablePPM(pixelPerMeter);
        this.image = image;
        this.width = image.getWidth() / pixelPerMeter;
        this.height = image.getHeight() / pixelPerMeter;
    }

    /**
     * @return Größe des Bildes in Pixeln
     */
    @Internal
    public Dimension getImageSizeInPx()
    {
        return new Dimension(this.image.getWidth(), this.image.getHeight());
    }

    /**
     * @return AWT-Repräsentation des Bildes
     */
    @API
    public BufferedImage getImage()
    {
        return this.image;
    }

    /**
     * Setzt die Größe des Bildes innerhalb der Physik neu. Ändert die
     * physikalischen Eigenschaften. Das Bild füllt die neuen Maße und wird ggf.
     * verzerrt.
     *
     * @param width  Die neue Breite des Objekts in M.
     * @param height Die neue Höhe des Objekts in M.
     *
     * @see #resetPixelPerMeter(double)
     */
    public void resetImageSize(double width, double height)
    {
        assertViableSizes(width, height);
        this.width = width;
        this.height = height;
        this.setFixture(() -> FixtureBuilder
                .createSimpleRectangularFixture(width, height));
    }

    /**
     * Ändert die Größe des Bildobjektes, sodass es dem angegebenen
     * Umrechnungsfaktor entspricht. Ändert auch die physikalischen
     * Eigenschaften des Bildes.
     *
     * @param pixelPerMeter Der Umrechnungsfaktor für die Größe des Bildes. Gibt
     *                      an, wie viele Pixel in der Bilddatei einem Meter in
     *                      der Engine entsprechen.
     *
     * @see #resetImageSize(double, double)
     */
    public void resetPixelPerMeter(double pixelPerMeter)
    {
        assertViablePPM(pixelPerMeter);
        resetImageSize(image.getWidth() / pixelPerMeter,
                image.getHeight() / pixelPerMeter);
    }

    private void assertViableSizes(double width, double height)
    {
        if (width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException(
                    "Bildhöhe und Breite müssen größer als 0 sein.");
        }
    }

    private void assertViablePPM(double pixelPerMeter)
    {
        if (pixelPerMeter <= 0)
        {
            throw new IllegalArgumentException(
                    "Die Umrechnungszahl für Pixel pro Meter darf nicht negativ sein. War "
                            + pixelPerMeter);
        }
    }

    /**
     * Setzt, ob dieses Bild horizontal gespiegelt dargestellt werden sollen.
     * Hiermit lassen sich zum Beispiel Bewegungsrichtungen (links/rechts)
     * einfach umsetzen.
     *
     * @param flipHorizontal Ob das Bild horizontal geflippt dargestellt werden
     *                       soll.
     *
     * @see #setFlipVertical(boolean)
     */
    @API
    public void setFlipHorizontal(boolean flipHorizontal)
    {
        this.flipHorizontal = flipHorizontal;
    }

    /**
     * Setzt, ob das Bild vertikal gespiegelt dargestellt werden sollen.
     *
     * @param flipVertical Ob die Animation horizontal geflippt dargestellt
     *                     werden soll.
     *
     * @see #setFlipVertical(boolean)
     */
    @API
    public void setFlipVertical(boolean flipVertical)
    {
        this.flipVertical = flipVertical;
    }

    /**
     * Gibt an, ob das Objekt horizontal gespiegelt ist.
     *
     * @return <code>true</code>, wenn das Objekt gerade horizontal gespiegelt
     *         ist. Sonst <code>false</code>.
     */
    @API
    public boolean isFlipHorizontal()
    {
        return flipHorizontal;
    }

    /**
     * Gibt an, ob das Objekt vertikal gespiegelt ist.
     *
     * @return <code>true</code>, wenn das Objekt gerade vertikal gespiegelt
     *         ist. Sonst <code>false</code>.
     */
    @API
    public boolean isFlipVertical()
    {
        return flipVertical;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Graphics2D g, double pixelPerMeter)
    {
        AffineTransform pre = g.getTransform();
        g.scale(width * pixelPerMeter / this.image.getWidth(),
                height * pixelPerMeter / this.image.getHeight());
        g.drawImage(this.image, flipHorizontal ? image.getWidth() : 0,
                -image.getHeight() + (flipVertical ? image.getHeight() : 0),
                (flipHorizontal ? -1 : 1) * image.getWidth(),
                (flipVertical ? -1 : 1) * image.getHeight(), null);
        g.setTransform(pre);
    }
}
