package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.Arrive;
import com.UKC_AICS.simulation.entity.behaviours.Arrive2;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.utils.Array;

/**
 * Created by James on 28/07/2014.
 */
public class ApproachCorpse extends State {

    private Object food;

    public ApproachCorpse(StateMachine parent, BoidManager bm, Object food) {
        super(parent, bm);
        this.food = food;
    }

    @Override
    public boolean update(Boid boid) {
        if(boid.hunger > 30) {

            Array<Entity> nearEntities = new Array<Entity>();
            Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
            nearEntities.addAll(nearBoids);

            float distance = boid.getPosition().cpy().sub(food.getPosition()).len2();
            if(distance < boid.sightRadius * boid.sightRadius) {
                if (distance > 16f * 16f) {
                    // Use arrive to get to corpse
                    steering.set(Arrive.act(boid, food.getPosition()));
                    steering.add(Collision.act(nearEntities,boid));
                    steering.add(Collision.act(boid));
                    boid.setAcceleration(steering);
                } else {
                    //collision/proximity check with corpse then push to Eat
                    parent.pushState(boid, new Eat(parent, bm, food));
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
