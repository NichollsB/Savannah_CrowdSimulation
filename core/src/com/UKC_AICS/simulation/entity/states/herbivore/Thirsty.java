package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;

/**
 * Created by Emily on 11/07/2014.
 */
public class Thirsty extends State {


    public Thirsty(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        //am i still thirsty?
        if (boid.thirst > 60) {
            return true;
        } else {
            //search for water
            parent.pushState(boid, new Drink(parent, bm));

            return false;
        }

    }
}
