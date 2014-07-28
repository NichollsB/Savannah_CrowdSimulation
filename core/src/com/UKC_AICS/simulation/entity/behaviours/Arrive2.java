package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 15/07/2014.
 */
public class Arrive2 extends Behaviour {

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Arrive is not to be used in this manner. Try static access Arrive.act(Boid boid, Vector3 target)");
    }

    public static Vector3 act(Boid boid, Entity target) {
    //TODO re-write arrival behaviour
        Vector3 vec = new Vector3();
        // distance between boid and target --> squared
        float distance = boid.getPosition().cpy().sub(target.getPosition()).len2();

        // check if within slowing distance
        if(distance < boid.nearRadius * boid.nearRadius) {

        }

        //Velocity to slow down approach to target position

        return vec;
    }
}
