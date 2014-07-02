package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Emily on 01/07/2014.
 */
public class Behaviour {

    private Vector3 tmpVec;
    private Vector3 tmpVec2;

    private final Random rand = new Random();

    public Vector3 cohesion(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(0, 0, 0);
        tmpVec2.set(0, 0, 0);

        int num = 0; //hold how many same specie boids in list.
        //loop through boids and add their position to the vector

        if(boids.size > 0) {
            for (Boid otherBoid : boids) {
                if (boid != otherBoid) {
                    //check to see if same species. TODO: multi - species herding.
                    if (otherBoid.species == boid.species) {
                        //added because its already been checked if it's in sight
                        tmpVec.add(otherBoid.getPosition());
                        num++;
                    }

                }
            }
            //find the average position.
            tmpVec.scl(1.0f / num);

            //subtract the boids position to find the "difference"
            //tmpVec.sub(boid.getPosition());
            tmpVec2.set(seek(boid, tmpVec));
            //            tmpVec2.set(boid.getPosition()).sub(tmpVec);
//            tmpVec2.nor();

        }

        return tmpVec2.cpy();
    }
    public Vector3 alignment(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(0,0,0); //will hold returnable
        tmpVec2.set(0,0,0); //will hold temporary value for

        int num = 0;
        if(boids.size > 0) {
            for(Boid b : boids) {
                if(b.getSpecies() == boid.getSpecies()) {
                    tmpVec2.set(b.getVelocity());  //.sub(boid.getVelocity()); //the subtration *can* go here, but its probably more fficient to scale it once than subtract many times. .
                    tmpVec.add(tmpVec2);
                    num++;
                }
            }
//            tmpVec.sub(boid.getVelocity());
            tmpVec.scl(1.0f/num); //do scaling to find average boid velocity
            tmpVec.nor();
            tmpVec.scl(boid.maxSpeed);
            tmpVec.sub(boid.getVelocity());
            tmpVec.limit(boid.maxForce);
//            tmpVec.nor();
        }

        return tmpVec.cpy();
    }

    public Vector3 wander()
    {

        tmpVec.set(rand.nextFloat() - rand.nextFloat(), rand.nextFloat() - rand.nextFloat(), 0);


        return tmpVec.cpy();
    }

    public Vector3 seek(Boid boid, Vector3 target) {
        Vector3 vec = new Vector3().set(target.sub(boid.getPosition()));
        vec.nor();
        vec.scl(boid.maxSpeed);

        vec.sub(boid.getVelocity());
        vec.limit(boid.maxForce);


        return vec;
    }
}
