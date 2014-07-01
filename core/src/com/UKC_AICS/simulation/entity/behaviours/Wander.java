package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Emily on 30/06/2014.
 */
public class Wander extends Behaviour {

    Random rand = new Random();

    @Override
    public Vector3 act(Array<Boid> boids, Array<WorldObject> objects, Boid boid) {
        tmpVec.set(rand.nextFloat() - rand.nextFloat(), rand.nextFloat() - rand.nextFloat(), 0);


        return tmpVec.cpy();
    }
}
