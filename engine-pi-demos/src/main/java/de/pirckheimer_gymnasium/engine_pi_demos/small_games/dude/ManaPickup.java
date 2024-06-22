/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha-examples/src/main/java/ea/example/showcase/dude/ManaPickup.java
 *
 * Engine Alpha ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2019 Michael Andonie and contributors.
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
package de.pirckheimer_gymnasium.engine_pi_demos.small_games.dude;

import de.pirckheimer_gymnasium.engine_pi.actor.Animation;
import de.pirckheimer_gymnasium.engine_pi.event.CollisionEvent;
import de.pirckheimer_gymnasium.engine_pi.event.CollisionListener;

/**
 * Danke an <a href="https://sorceressgamelab.itch.io/">SorceressGameLab</a> für
 * die Assets!
 */
public class ManaPickup extends Animation
        implements CollisionListener<PlayerCharacter>
{
    private static final double SIZE = .4;

    private boolean active = true;

    public ManaPickup()
    {
        super(Animation.createFromSpritesheet(.1, "dude/gem_blue.png", 6, 1,
                SIZE, SIZE));
    }

    @Override
    public void onCollision(CollisionEvent<PlayerCharacter> collisionEvent)
    {
        if (!active)
        {
            return;
        }
        // Ich wurde aufgesammelt!
        collisionEvent.getColliding().gotItem(Item.ManaPickup);
        this.setActive(false);
        active = false;
        delay(3, () -> setActive(true));
        animateOpacity(2, 1f);
    }

    private void setActive(boolean b)
    {
        active = b;
        setOpacity(b ? 1 : 0);
    }
}
