package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Cohesion extends Behaviour {

	/**
	 * Cohesion is the attraction toward the middle of the flock/herd/group
	 * 
	 * 
	 */
	
//	@Override
//	Vector3 act(ArrayList<Boid> boids, Boid boid) {
//		tmpVec.set(0, 0, 0);
//		//loop through boids and add their position to the vector
//		for (Boid otherBoid : boids) {
//			//check to see if same species. TODO: multi - species herding.
//			if(otherBoid.species == boid.species)
//				//added because its already been checked if it's in sight
//				tmpVec.add(otherBoid.position);
//		}
//
//		//remove boid in questions position.
//		//tmpVec2.sub(boid.position);
//
//		//find the average position.
//		tmpVec.scl(1f / ( boids.size() ) );
//
//		return tmpVec.cpy();
//	}


    @Override
    Vector3 act(ArrayList<Boid> boids, ArrayList<Boid> objects, Boid boid) {
        tmpVec.set(0, 0, 0);

        int num = 0; //hold how many same specie boids in list.
        //loop through boids and add their position to the vector
        for (Boid otherBoid : boids) {
            //check to see if same species. TODO: multi - species herding.
            if (otherBoid.species == boid.species) {
                //added because its already been checked if it's in sight
                tmpVec.add(otherBoid.position);
                num++;
            }
        }


        //find the average position.
        tmpVec.scl(1.0f /  num );

        return tmpVec.cpy();
    }
}
