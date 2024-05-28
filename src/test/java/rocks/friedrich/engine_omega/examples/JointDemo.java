/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha-examples/src/main/java/ea/example/showcase/JointDemo.java
 *
 * Engine Alpha ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2024 Michael Andonie and contributors.
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
package rocks.friedrich.engine_omega.examples;

import java.awt.Color;
import java.awt.event.KeyEvent;

import rocks.friedrich.engine_omega.Game;
import rocks.friedrich.engine_omega.Scene;
import rocks.friedrich.engine_omega.Vector;
import rocks.friedrich.engine_omega.actor.BodyType;
import rocks.friedrich.engine_omega.actor.Circle;
import rocks.friedrich.engine_omega.actor.Polygon;
import rocks.friedrich.engine_omega.actor.Rectangle;
import rocks.friedrich.engine_omega.event.KeyListener;

/**
 * Einfaches Programm zur Demonstration von Joints in der Engine Created by
 * Michael on 12.04.2017.
 */
public class JointDemo extends ForceKlickEnvironment implements KeyListener
{
    private boolean schwerkraftActive = false;

    private Rectangle wippe;

    private Polygon basis;

    private Rectangle[] kette;

    private Circle ball;

    /**
     * Erstellt das Demo-Objekt
     */
    public JointDemo(Scene parent)
    {
        super(parent);
        initialisieren2();
    }

    public void initialisieren2()
    {
        wippeBauen();
        ketteBauen(15);
        leashBauen();
        hoverHolderBauen();
        // getMainLayer().setVisibleHeight(40, Game.getFrameSizeInPixels());
        ball = new Circle(1);
        add(ball);
        ball.setColor(Color.BLUE);
        ball.setPosition(new Vector(300, 200));
        ball.setBodyType(BodyType.DYNAMIC);
    }

    private void hoverHolderBauen()
    {
        final int FACT = 2;
        Polygon halter = new Polygon(new Vector(0 * FACT, 50 * FACT),
                new Vector(25 * FACT, 75 * FACT),
                new Vector(50 * FACT, 75 * FACT),
                new Vector(75 * FACT, 50 * FACT),
                new Vector(75 * FACT, 100 * FACT),
                new Vector(0 * FACT, 100 * FACT));
        halter.setColor(Color.CYAN);
        halter.setBodyType(BodyType.DYNAMIC);
        Rectangle item = new Rectangle(35 * FACT, 20 * FACT);
        item.setPosition(30 * FACT, 0);
        item.setColor(Color.red);
        item.setBodyType(BodyType.DYNAMIC);
        halter.createDistanceJoint(item, halter.getCenter(), item.getCenter());
    }

    private void leashBauen()
    {
        Circle kx = new Circle(30);
        kx.setColor(Color.BLUE);
        kx.setBodyType(BodyType.DYNAMIC);
        Circle ky = new Circle(50);
        ky.setPosition(50, 0);
        ky.setColor(Color.GREEN);
        ky.setBodyType(BodyType.DYNAMIC);
        kx.createRopeJoint(ky,
                // kx.position.mittelPoint().alsVector(),
                // ky.position.mittelPoint().alsVector(), 4);
                new Vector(15, 15), new Vector(25, 25), 4);
    }

    private void wippeBauen()
    {
        basis = new Polygon(new Vector(0, 100), new Vector(100, 100),
                new Vector(50, 0));
        basis.setBodyType(BodyType.STATIC);
        basis.setColor(Color.WHITE);
        wippe = new Rectangle(500, 40);
        wippe.setBodyType(BodyType.DYNAMIC);
        wippe.setCenter(50, 0);
        wippe.setColor(Color.GRAY);
        Vector verzug = new Vector(100, 100);
        wippe.moveBy(verzug);
        basis.moveBy(verzug);
        wippe.createRevoluteJoint(basis, new Vector(50, 0).add(verzug));
    }

    private void ketteBauen(int kettenlaenge)
    {
        kette = new Rectangle[kettenlaenge];
        for (int i = 0; i < kette.length; i++)
        {
            kette[i] = new Rectangle(50, 10);
            Vector posrel = new Vector(45 * i, 30);
            kette[i].moveBy(posrel);
            kette[i].setColor(Color.GREEN);
            kette[i].setBodyType(i == 0 ? BodyType.STATIC : BodyType.DYNAMIC);
            if (i != 0)
            {
                kette[i - 1].createRevoluteJoint(kette[i],
                        new Vector(0, 5).add(posrel));
            }
        }
        Circle gewicht = new Circle(100);
        gewicht.setColor(Color.WHITE);
        gewicht.setBodyType(BodyType.DYNAMIC);
        // gewicht.setMass(40);
        Vector vektor = new Vector(45 * kette.length, 35);
        gewicht.setCenter(new Vector(vektor.getX(), vektor.getY()));
        gewicht.createRevoluteJoint(kette[kette.length - 1], vektor);
    }

    @Override
    public void onKeyDown(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_S)
        {
            schwerkraftActive = !schwerkraftActive;
            setGravity(schwerkraftActive ? new Vector(0, 10) : Vector.NULL);
        }
    }

    public static void main(String[] args)
    {
        Game.start(1000, 800, new JointDemo(null));
    }
}
