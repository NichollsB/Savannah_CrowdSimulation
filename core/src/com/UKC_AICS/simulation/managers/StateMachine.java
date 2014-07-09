package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;

/**
 * Created by Emily on 09/07/2014.
 */
public class StateMachine extends Manager {
    @Override
    public void update() {
        throw new Error("This is not updated all together, its updated per boid using update(Boid boid)");
    }

    public void update(Boid boid) {
        //do something for the boid.

            

    }


}
