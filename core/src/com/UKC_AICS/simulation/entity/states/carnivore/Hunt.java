package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.Gdx;

/**
 * Created by Emily on 16/07/2014.
 */
public class Hunt extends State {

    public Hunt(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        if (boid.hunger > 60) {
            // pop
            return true; //stop looking for food.
        } else {


            return false;
        }
    }


    public boolean inSightRange(int x, int y) {


        return true;
    }
}
