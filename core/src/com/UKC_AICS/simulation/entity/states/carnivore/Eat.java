package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;

/**
 * Created by Emily on 16/07/2014.
 */
public class Eat extends State {

    Entity food;
    public Eat(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    public Eat(StateMachine parent, BoidManager bm, Entity food) {
        super(parent, bm);
        this.food = food;
    }

    @Override
    public boolean update(Boid boid) {
        if(boid.hunger < 90) {
            System.out.println("EATING");
            boid.hunger += 1;
            return false;
        } else {
            return true;
        }
    }
}
