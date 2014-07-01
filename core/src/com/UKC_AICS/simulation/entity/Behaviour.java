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

        tmpVec.set(0,0,0);
        tmpVec2.set(0,0,0);

        int num = 0; //hold how many same specie boids in list.
        //loop through boids and add their position to the vector

        if(boids.size>0)

        {
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

//            tmpVec2.set(seek(boid, tmpVec));
            //            tmpVec2.set(boid.getPosition()).sub(tmpVec);
        }

        return tmpVec2.cpy();
    }
    public Vector3 wander()
    {

        tmpVec.set(rand.nextFloat() - rand.nextFloat(), rand.nextFloat() - rand.nextFloat(), 0);


        return tmpVec.cpy();
    }

    public Vector3 seek(Vector3 target, Vector3 location) {
        Vector3 vec = new Vector3().set(target.sub(location));
        vec.nor();
//        vec.scl(boid.maxSpeed);
//
//        vec.sub(boid.getVelocity());


        return vec;
    }
}
