package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Alignment extends Behaviour {

    @Override
    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        tmpVec.set(0, 0, 0); //will hold returnable
        tmpVec2.set(0, 0, 0); //will hold temporary value for running sum of velocity

        int num = 0;
        if (boids.size > 0) {
            for (Boid b : boids) {
                //check if the boid is the same subType
                //TODO: Multi-subType support
                if(b == null || boid == null) {
                    System.out.println("one of the boids is null!");
                }
                if (b.getSpecies() == boid.getSpecies()) {
                    tmpVec.add(b.getVelocity());
                    num++;
                }
            }
            if(num > 0) {
                tmpVec.scl(1.0f / num); //do scaling to find average boid velocity
                tmpVec.nor();
                tmpVec.scl(boid.maxSpeed);
                tmpVec.sub(boid.getVelocity());
                tmpVec.limit(boid.maxForce);
            }
        }

        return tmpVec.cpy(); //copy to avoid strange bugs.
    }

}
