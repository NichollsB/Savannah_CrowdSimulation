package com.UKC_AICS.simulation.entity.states;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;

/**
 * Created by Emily on 11/07/2014.
 */
public class Drink extends State {
    public Drink(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        if(boid.thirst > 80) { //and check there is enough water is still there to drink from.
            boid.setState(this.toString());
//            System.out.println(boid + "\n Just quit DRINK state ");
            return true;
        } else {

            // drink from tile
            boid.thirst += 0.1;
            return false;
        }
    }
}
