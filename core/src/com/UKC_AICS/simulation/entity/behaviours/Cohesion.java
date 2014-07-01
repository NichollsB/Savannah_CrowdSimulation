package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


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
    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(0, 0, 0);
        tmpVec2.set(0, 0, 0);

        int num = 0; //hold how many same specie boids in list.
        //loop through boids and add their position to the vector

        if(boids.size > 0) {
            for (Boid otherBoid : boids) {
                //check to see if same species. TODO: multi - species herding.
                if (otherBoid.species == boid.species) {
                    //added because its already been checked if it's in sight
                    tmpVec.add(otherBoid.getPosition());
                    num++;
                }
            }

            //find the average position.
            tmpVec.scl(1.0f / num);

            //subtract the boids position to find the "difference"
            //tmpVec.sub(boid.getPosition());

            tmpVec2.set(Seek.act(boid, tmpVec));
//            tmpVec2.set(boid.getPosition()).sub(tmpVec);
        }

        return tmpVec2.cpy();
    }
}
