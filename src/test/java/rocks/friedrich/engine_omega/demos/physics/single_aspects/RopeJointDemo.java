package rocks.friedrich.engine_omega.demos.physics.single_aspects;

import rocks.friedrich.engine_omega.Game;
import rocks.friedrich.engine_omega.Vector;

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
