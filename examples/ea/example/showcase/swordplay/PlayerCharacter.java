package ea.example.showcase.swordplay;

import ea.FrameUpdateListener;
import ea.Scene;
import ea.Vector;
import ea.actor.Actor;
import ea.actor.Animation;
import ea.actor.StatefulAnimation;
import ea.animation.Interpolator;
import ea.animation.ValueAnimator;
import ea.animation.interpolation.ReverseEaseFloat;
import ea.collision.CollisionEvent;
import ea.collision.CollisionListener;
import ea.example.showcase.jump.Enemy;

public class PlayerCharacter extends StatefulAnimation implements CollisionListener<Actor>, FrameUpdateListener {

    private static final float MAX_SPEED = 5000;
    public static final int JUMP_FORCE = +2000;
    public static final int SMASH_FORCE = -15000;
    public static final int BOTTOM_OUT = -500;

    /**
     * Beschreibt die drei Zustände, die ein Character bezüglich seiner horizontalen Bewegung haben kann.
     */
    public enum HorizontalMovement {
        LEFT, RIGHT, IDLE;

        public float getTargetXVelocity() {
            switch (this) {
                case LEFT:
                    return -MAX_SPEED;
                case RIGHT:
                    return MAX_SPEED;
                case IDLE:
                    return 0;
                default:
                    throw new IllegalStateException("Illegal enum state");
            }
        }
    }

    private HorizontalMovement horizontalMovement = HorizontalMovement.IDLE;
    private Vector smashForce = Vector.NULL;

    public PlayerCharacter(Scene scene) {
        super(scene, 64, 64);

        // Alle einzuladenden Dateien teilen den Großteil des Paths (Ordner sowie gemeinsame Dateipräfixe)
        String basePath = "game-assets/jump/spr_m_traveler_";

        addState("idle", Animation.createFromAnimatedGif(scene, basePath + "idle_anim.gif"));
        addState("walking", Animation.createFromAnimatedGif(scene, basePath + "walk_anim.gif"));
        addState("running", Animation.createFromAnimatedGif(scene, basePath + "run_anim.gif"));
        addState("jumpingUp", Animation.createFromAnimatedGif(scene, basePath + "jump_1up_anim.gif"));
        addState("midair", Animation.createFromAnimatedGif(scene, basePath + "jump_2midair_anim.gif"));
        addState("falling", Animation.createFromAnimatedGif(scene, basePath + "jump_3down_anim.gif"));
        addState("landing", Animation.createFromAnimatedGif(scene, basePath + "jump_4land_anim.gif"));
        addState("smashing", Animation.createFromAnimatedGif(scene, basePath + "jump_4land_anim.gif"));

        setStateTransition("midair", "falling");
        setStateTransition("landing", "idle");

        physics.setFriction(0);
        physics.setElasticity(0);

        scene.add(this);
        physics.setMass(65);
        addCollisionListener(this);
    }

    /**
     * Wird ausgeführt, wenn ein Sprungbefehl (W) angekommen ist.
     */
    public void tryJumping() {
        if (physics.testStanding()) {
            physics.applyImpulse(new Vector(0, JUMP_FORCE));
            setState("jumpingUp");
        }
    }

    public void setHorizontalMovement(HorizontalMovement state) {
        this.horizontalMovement = state;
    }

    public HorizontalMovement getHorizontalMovement() {
        return this.horizontalMovement;
    }

    public void smash() {
        if (getCurrentState().equals("falling")) {
            setState("smashing");
            smashForce = new Vector(0, SMASH_FORCE);
        }
    }

    /**
     * Wird frameweise aufgerufen: Checkt den aktuellen state des Characters und macht ggf. Änderungen
     */
    @Override
    public void onFrameUpdate(int frameDuration) {
        Vector velocity = physics.getVelocity();

        // kümmere dich um die horizontale Bewegung
        float desiredVelocity = horizontalMovement.getTargetXVelocity();
        float impulse = desiredVelocity - velocity.x;
        physics.applyForce(new Vector(impulse, 0));

        switch (getCurrentState()) {
            case "jumpingUp":
                if (velocity.y > 0) setState("midair");
                break;
            case "idle":
            case "running":
            case "walking":
                //if(standing) {
                if (velocity.y > 0.1f) {
                    setState("midair");
                } else if (Math.abs(velocity.x) > 550f) {
                    changeState("running");
                } else if (Math.abs(velocity.x) > 10f) {
                    changeState("walking");
                } else {
                    changeState("idle");
                }
                //}
                break;
        }

        if (velocity.x > 0) {
            setFlipHorizontal(false);
        } else if (velocity.x < 0) {
            setFlipHorizontal(true);
        }

        physics.applyForce(smashForce);

        if (position.getY() < BOTTOM_OUT) {
            position.set(0, 0);
            physics.setVelocity(Vector.NULL);
            setState("falling");
        }
    }

    @Override
    public void onCollision(CollisionEvent<Actor> collisionEvent) {
        if (collisionEvent.getColliding() instanceof Enemy) {
            return;
        }

        boolean falling = getCurrentState().equals("falling");
        boolean smashing = getCurrentState().equals("smashing");

        if ((falling || smashing) && physics.testStanding()) {
            setState("landing");
            smashForce = Vector.NULL;

            if (smashing) {
                Interpolator<Float> interpolator = new ReverseEaseFloat(0, -0.01f * physics.getVelocity().y);
                FrameUpdateListener valueAnimator = new ValueAnimator<>(100, y -> getScene().getCamera().setOffset(new Vector(0, y)), interpolator);
                getScene().addFrameUpdateListener(valueAnimator);
            }
        }
    }
}
