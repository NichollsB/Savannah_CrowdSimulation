package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class Separation extends Behaviour {

//	@Override
//	Vector3 act(ArrayList<Boid> boids, Boid boid) {
//        for (Boid b : boids) {
//
//        }
//        return new Vector3();
//	}

    @Override
    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        tmpVec.set(0, 0, 0); //will hold returnable
        tmpVec2.set(0, 0, 0); //will hold temporary value for

        int num = 0; //holds counter for same subType boids.

        if (boids.size > 0) {
            for (Boid b : boids) {
                //no subType check here for now as it wants to stay a certain distance away from all boids.
                    tmpVec2.set(boid.getPosition());
                    tmpVec2.sub(b.getPosition());
                    tmpVec.add(tmpVec2);
                    num++;
            }

            tmpVec.scl(1.0f / num);
            tmpVec.nor();
            tmpVec.scl(boid.maxSpeed);
            tmpVec.sub(boid.getVelocity());
            tmpVec.limit(boid.maxForce);

        }


        return tmpVec.cpy();
    }

}
