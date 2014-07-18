package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 15/07/2014.
 */
public class Arrive extends Behaviour {

    static Vector3 vec = new Vector3();

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Arrive is not to be used in this manner. Try static access Arrive.act(Boid boid, Vector3 target)");
    }

    static Vector3 act(Boid boid, Vector3 target) {
        vec.set(target.sub(boid.getPosition()));
        float dist = vec.len();
        if(dist > 0 ) {
            float speed = boid.maxSpeed * (dist / boid.sightRadius);
            speed = Math.min(boid.maxSpeed, speed);
            vec.nor().scl(speed);
            vec.sub(boid.getVelocity());
            vec.limit(boid.maxForce);
        }
        return vec;
    }
}
