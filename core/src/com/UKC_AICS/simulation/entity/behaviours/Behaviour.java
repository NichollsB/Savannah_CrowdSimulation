package com.UKC_AICS.simulation.entity.behaviours;

import java.util.ArrayList;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector3;

public abstract class Behaviour {

	protected Vector3 tmpVec = new Vector3();
	
	/**
	 * Boid manager will pass the behaviour the list of boids in sight and the boid in question.
	 * 
	 * TODO: some behaviours will also need to know what objects are in sight.
	 * 
	 * @param list : this list will contain relevant(line of sight wise) boids.
	 * @param boid : the boid that the behaviour is being run for.
	 */
	
	abstract Vector3 act(ArrayList<Boid> list, Boid boid);
}
