package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Emily on 30/06/2014.
 */
public class Wander extends Behaviour {

    Random rand = new Random();

    /**
     *
     * @param boids   : this list will contain relevant(line of sight wise) boids.
     * @param objects : this list will contain relevant(line of sight wise) objects.
     * @param boid    : the boid that the behaviour is being run for.
     * @return a random "wander" vector between 1 and -1, with a tendency toward zero.
     */
    @Override
    public Vector3 act(Array<Boid> boids, Array<com.UKC_AICS.simulation.entity.Object> objects, Boid boid) {

        tmpVec.set(rand.nextFloat() - rand.nextFloat(), rand.nextFloat() - rand.nextFloat(), 0);
        return tmpVec.cpy();
    }
}
