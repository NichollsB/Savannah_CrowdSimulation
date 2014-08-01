package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 01/08/2014.
 */
public class Flee extends Behaviour {

    private static Vector3 vec = new Vector3();

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Flee is not to be used in this manner. Try static access Flee.act(Boid boid, Vector3 target)");
    }

    static public Vector3 act(Boid boid, Vector3 target) {
        vec.set(boid.getPosition());
        vec.sub(target);
        vec.nor().scl(boid.maxSpeed);
//        vec.sub(boid.getVelocity());
//        vec.limit(boid.maxForce);

        return vec;
    }
}
