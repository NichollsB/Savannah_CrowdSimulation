package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 01/07/2014.
 */
public class Seek extends Behaviour {


    static Vector3 vec = new Vector3();

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Seek is not to be used in this manner. Try static access Seek.act(Boid boid, Vector3 target)");
    }

    static public Vector3 act(Boid boid, Vector3 target) {
        vec.set(target.sub(boid.getPosition()));
        vec.nor().scl(boid.maxSpeed);
        vec.sub(boid.getVelocity());
        vec.limit(boid.maxForce);

        return vec;
    }
}
