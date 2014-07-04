package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public abstract class Behaviour {

    //Temporary Vectors that the behaviours can use to do summing etc.
    protected Vector3 tmpVec = new Vector3();
    protected Vector3 tmpVec2 = new Vector3();


    /**
     * Boid manager will pass the behaviour the list of boids AND objects in sight and the boid in question.
     *
     * @param boids   : this list will contain relevant(line of sight wise) boids.
     * @param objects : this list will contain relevant(line of sight wise) objects.
     * @param boid    : the boid that the behaviour is being run for.
     */
    public Vector3 act(Array<Boid> boids, Array<com.UKC_AICS.simulation.entity.Object> objects, Boid boid) {
        return null;
    }

}
