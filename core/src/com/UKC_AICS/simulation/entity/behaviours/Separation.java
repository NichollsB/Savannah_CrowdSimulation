package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Separation extends Behaviour {

//	@Override
//	Vector3 act(ArrayList<Boid> boids, Boid boid) {
//        for (Boid b : boids) {
//
//        }
//        return new Vector3();
//	}

    @Override
    Vector3 act(ArrayList<Boid> boids, ArrayList<Boid> objects, Boid boid) {
        tmpVec.set(0,0,0); //will hold returnable
        tmpVec2.set(0,0,0); //will hold temporary value for

        int num = 0; //holds counter for same species boids.

        if(boids.size() > 0) {
            for(Boid b : boids) {
                if(boid.getSpecies() == b.getSpecies()) {
                    tmpVec2.set(b.getPosition()).sub(boid.getPosition());
                    tmpVec.add(tmpVec2);
                }
            }
            tmpVec.scl(1.0f/num);
        }


        return tmpVec.cpy();
    }

}
