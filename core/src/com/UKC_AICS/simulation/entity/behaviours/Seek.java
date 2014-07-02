package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 01/07/2014.
 */
public class Seek extends Behaviour {


    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        throw new Error("Seek is not to be used in this manner. Try static access Seek.act(Boid boid, Vector3 target)");
    }

    static Vector3 act(Boid boid, Vector3 target) {
        Vector3 vec = new Vector3().set(target.sub(boid.getPosition()));
        vec.nor().scl(boid.maxSpeed);
        vec.sub(boid.getVelocity());
        vec.limit(boid.maxForce);

        return vec;
    }
}
