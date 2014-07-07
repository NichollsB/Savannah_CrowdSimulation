package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 06/07/2014.
 */
public class Repeller extends Behaviour
{
    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        tmpVec.set(0, 0, 0);
        tmpVec2.set(0, 0, 0);

        int num = 0;

        for (Entity ent : objects) {
            if (ent.getType() != 2) { //TODO: remove hardcoded "flee" picking from mass of objects - probably filter before passing to here.
                tmpVec2.add(ent.position);
            }
            num++;
        }

//        tmpVec2.sub(boid.getPosition());
        if (num > 0) {
            tmpVec2.scl(1f / num);
            tmpVec.set(Avoid.act(boid, tmpVec2)); // limits and .scl() are done in avoid.
        }

        return tmpVec.cpy();
    }
}
