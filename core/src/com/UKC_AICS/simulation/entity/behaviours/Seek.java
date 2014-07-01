package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 01/07/2014.
 */
public class Seek extends Behaviour{


    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {




        return tmpVec;
    }
    static Vector3 act(Boid boid, Vector3 target) {
        Vector3 vec = new Vector3().set(target.sub(boid.getPosition()));
        vec.nor();
        vec.scl(boid.maxSpeed);

        vec.sub(boid.getVelocity());


        return vec;
    }
}
