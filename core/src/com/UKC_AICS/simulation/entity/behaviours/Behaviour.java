package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public abstract class Behaviour {

    //Temporary Vectors that the behaviours can use to do summing etc.
    protected Vector3 tmpVec = new Vector3();
    protected Vector3 tmpVec2 = new Vector3();
    protected static Vector3 statVec = new Vector3();


    /**
     * Boid manager will pass the behaviour the list of boids AND objects in sight and the boid in question.
     *  @param boids   : this list will contain relevant(line of sight wise) boids.
     * @param objects : this list will contain relevant(line of sight wise) objects.
     * @param boid    : the boid that the behaviour is being run for.
     */
    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("The Behaviour is calling the default - make sure to check you've got the right act() call");
//        return null;
    }
    public Vector3 act(Array<Entity> objects, Boid boid) {
        return null;
    }

    public Vector3 act(Boid boid, Boid target) {
        return null;
    }

}
