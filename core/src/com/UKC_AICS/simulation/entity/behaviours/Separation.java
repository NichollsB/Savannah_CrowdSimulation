package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Separation extends Behaviour {

	@Override
	Vector3 act(ArrayList<Boid> boids, Boid boid) {
        for (Boid b : boids) {

        }




        return new Vector3();
	}

    @Override
    Vector3 act(ArrayList<Boid> boids, ArrayList<Boid> objects, Boid boid) {
        tmpVec.set(0,0,0); //will hold returnable
        tmpVec2.set(0,0,0); //will hold temporary value for



        if(boids.size() > 0) {
            for(Boid b : boids) {
                tmVec2.
                tmpVec.add()
            }
        }


        return new Vector3();
    }

}
