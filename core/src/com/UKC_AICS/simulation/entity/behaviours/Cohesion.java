package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class Cohesion extends Behaviour {

    /**
     * Cohesion is the attraction toward the middle of the flock/herd/group
     */
    @Override
    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(0, 0, 0);
        tmpVec2.set(0, 0, 0);

        int num = 0; //hold how many same specie boids in list.
        //loop through boids and add their position to the vector

        if (boids.size >= 1 ) {
            for (Boid otherBoid : boids) {

                //check if its not you and check to see if same species. TODO: multi - species herding.
                if (boid != otherBoid && otherBoid.species == boid.species) {
                    tmpVec2.add(otherBoid.getPosition());
                    num++;
                }
            }
            //find the average position.
            if(num > 0) {

                tmpVec2.scl(1.0f / num);
                tmpVec.set(Seek.act(boid, tmpVec2)); // limits and .scl() are done in seek.
            }
        }

        if (tmpVec.x != tmpVec.x) {
            System.out.println("blerpy");
        }
        return tmpVec.cpy();
    }
}
