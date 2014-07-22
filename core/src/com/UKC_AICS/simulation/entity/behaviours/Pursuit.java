package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by James on 17/07/2014.
 */
public class Pursuit extends Behaviour {


    static float STEPS_AHEAD = 3f;  //should look 3 steps ahead of prey (3f * velocity)
    static Vector3 vec = new Vector3();

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Seek is not to be used in this manner. Try static access Seek.act(Boid boid, Vector3 target)");
    }

    public static Vector3 act(Boid boid, Boid target) {

        vec.set(target.getPosition());
        vec.add(target.getVelocity().scl(STEPS_AHEAD));  //should look 3 steps ahead of prey (3f * velocity)
        vec.sub(boid.getPosition());
        vec.nor().scl(boid.maxSpeed);
        vec.sub(boid.getVelocity());
        vec.limit(boid.maxForce);

        return vec;
    }
}
