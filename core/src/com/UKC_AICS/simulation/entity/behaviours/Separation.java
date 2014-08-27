package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * @author Emily
 *         Seperation accepts a list of the boids considered "Nearest" so within their "near radius"
 *         and then returns a vector moving to a position that is considered further away from them.
 */

public class Separation extends Behaviour {

    @Override
    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        tmpVec.set(0, 0, 0); //will hold returnable
        tmpVec2.set(0, 0, 0); //will hold temporary value for

        int num = 0; //holds counter for boids.

        if (boids.size > 0) {
            for (Boid b : boids) {
                //no subType check here for now as it wants to stay a certain distance away from all boids.
                if (b != boid) {
                    tmpVec2.set(boid.getPosition());
                    tmpVec2.sub(b.getPosition());
                    tmpVec.add(tmpVec2);
                    num++;
                }
            }

            if (num > 0) {
                tmpVec.scl(1.0f / num);
                tmpVec.nor();
                tmpVec.scl(boid.maxSpeed);
                tmpVec.sub(boid.getVelocity());
                tmpVec.limit(boid.maxForce);
            }

        }


        return tmpVec.cpy();
    }

}
