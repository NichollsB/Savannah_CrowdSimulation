package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;

/**
 * @author James
 */
public class Eat extends State {

    com.UKC_AICS.simulation.entity.Object food;
    public Eat(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    public Eat(StateMachine parent, BoidManager bm, Object food) {
        super(parent, bm);
        this.food = food;
    }

    @Override
    public boolean update(Boid boid) {
        if(food != null && boid.hunger > 0) {
            boid.setVelocity(0f, 0f, 0f);
            boid.setAcceleration(boid.getVelocity());
            //TODO add checks, make sure corpse is next to boid
            if(food.getMass() > 3f) {
                food.reduceMass(0.5f);
                boid.hunger -= 2f;
                return false;
            } else {
                //no food left on corpse
                WorldManager.removeObject(food);
                return true;
            }
        } else {
            //pop out if not hungry
            return true;
        }
    }
}
