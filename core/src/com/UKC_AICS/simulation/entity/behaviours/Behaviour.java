package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public abstract class Behaviour {

    protected Vector3 tmpVec = new Vector3();
    protected Vector3 tmpVec2 = new Vector3();

//
//	/**
//	 * Boid manager will pass the behaviour the list of boids in sight and the boid in question.
//	 *
//	 * @param boids : this list will contain relevant(line of sight wise) boids.
//	 * @param boid : the boid that the behaviour is being run for.
//	 */
//
//    abstract Vector3 act(ArrayList<Boid> boids, Boid boid);




    /**
     * Boid manager will pass the behaviour the list of boids AND objects in sight and the boid in question.
     *
     * @param boids : this list will contain relevant(line of sight wise) boids.
     * @param objects : this list will contain relevant(line of sight wise) objects.
     * @param boid : the boid that the behaviour is being run for.
     */
    abstract public Vector3 act(ArrayList<Boid> boids, ArrayList<WorldObject> objects, Boid boid);
}
