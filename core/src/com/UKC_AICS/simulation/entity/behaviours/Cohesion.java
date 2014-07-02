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

        if (boids.size > 0) {
            for (Boid otherBoid : boids) {
                //check if its not you and check to see if same species. TODO: multi - species herding.
                if (boid != otherBoid && otherBoid.species == boid.species) {
                    tmpVec.add(otherBoid.getPosition());
                    num++;
                }
            }
            //find the average position.
            tmpVec.scl(1.0f / num);

            tmpVec2.set(Seek.act(boid, tmpVec));
        }

        return tmpVec2.cpy();
    }
}
