package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 06/07/2014.
 */
public class Avoid extends Behaviour {


    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Arrive is not to be used in this manner. Try static access Arrive.act(Boid boid, Vector3 target)");
    }

    public static Vector3 act(Boid boid, Vector3 target) {
        Vector3 vec = new Vector3(boid.position).sub(target);
        vec.nor().scl(boid.maxSpeed);
        vec.sub(boid.velocity);
        vec.limit(boid.maxForce);

        return vec;
    }
}
