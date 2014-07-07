package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 04/07/2014.
 */
public class Attractor extends Behaviour {

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        tmpVec.set(0,0,0);
        tmpVec2.set(0,0,0);

        int num = 0;

        for (Entity ent : objects) {
            if(ent.getType() == 2) {
                tmpVec2.add(ent.getPosition());
            }
            num++;
        }

//        tmpVec2.sub(boid.getPosition());
        if(num > 0) {
            tmpVec2.scl(1f/num);
            tmpVec.set(Seek.act(boid, tmpVec2)); // limits and .scl() are done in seek.
        }

        return tmpVec.cpy();
    }
}
