package de.pirckheimer_gymnasium.engine_pi_demos.physics.single_aspects;

import de.pirckheimer_gymnasium.engine_pi.Game;
import de.pirckheimer_gymnasium.engine_pi.Vector;

/**
 * Demonstriert die Klasse
 * {@link de.pirckheimer_gymnasium.engine_pi.actor.RopeJoint} und die Methode
 * {@link de.pirckheimer_gymnasium.engine_pi.actor.Actor#createRopeJoint(de.pirckheimer_gymnasium.engine_pi.actor.Actor, Vector, Vector, double)}
 */
public class RopeJointDemo extends BaseJointScene
{
    public RopeJointDemo()
    {
        super();
        joint = a.createRopeJoint(b, new Vector(0.25, 0.25),
                new Vector(0.75, 0.75), 3);
        joint.addReleaseListener(() -> {
            System.out.println("Verbindung wurde gelöst");
        });
    }

    public static void main(String[] args)
    {
        Game.setDebug(true);
        Game.start(new RopeJointDemo());
    }
}
