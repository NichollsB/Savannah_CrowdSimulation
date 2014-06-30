package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Alignment extends Behaviour {

//	/**
//	 * @return orientation change required to align with the groups heading.
//	 */
//	@Override
//	Vector3 act(ArrayList<Boid> boids, Boid boid) {
//        tmpVec.set(0,0,0); //will hold returnable
//        tmpVec2.set(0,0,0); //will hold temporary value for
//
//
//
//        if(boids.size() > 0) {
//            for(Boid b : boids) {
//                tmpVec2.set(b.getOrientation()).sub(boid.getOrientation());
//                tmpVec.add(tmpVec2);
//            }
//            tmpVec2.scl(1.0f/boids.size());
//        }
//
//        return tmpVec2;
//	}

    @Override
    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(0,0,0); //will hold returnable
        tmpVec2.set(0,0,0); //will hold temporary value for

    int num = 0;
        if(boids.size > 0) {
            for(Boid b : boids) {
                if(b.getSpecies() == boid.getSpecies()) {
                    tmpVec2.set(b.getOrientation()).sub(boid.getOrientation());
                    tmpVec.add(tmpVec2);
                    num++;
                }
            }
            tmpVec.scl(1.0f/num);
        }

        return tmpVec.cpy();
    }

}
