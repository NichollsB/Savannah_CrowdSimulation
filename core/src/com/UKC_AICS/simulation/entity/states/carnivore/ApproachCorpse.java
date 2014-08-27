package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.Arrive;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * @author James
 */
public class ApproachCorpse extends State {

    private Object food;
    private Random rand = new Random();

    public ApproachCorpse(StateMachine parent, BoidManager bm, Object food) {
        super(parent, bm);
        this.food = food;
    }

    @Override
    public boolean update(Boid boid) {
        if(WorldManager.checkObject(food)) {
            if (boid.hunger > 0) {

                Array<Entity> nearEntities = new Array<Entity>();
                Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
                nearEntities.addAll(nearBoids);

                float distance = boid.getPosition().cpy().sub(food.getPosition()).len2();
                if (distance < boid.sightRadius * boid.sightRadius) {
                    if (distance > 16f * 16f) {
                        steering.set(0f,0f,0f);
                        // Use arrive to get to corpse
                        Vector3 tempVec = new Vector3(0f,0f,0f);
                        tempVec.add(Collision.act(nearEntities, boid));
                        tempVec.add(Collision.act(boid));
                        if(steering.equals(tempVec)) {
                            steering.set(Arrive.act(boid, food.getPosition()));
                        }
                        else {
                            steering.set(tempVec);
                            return true;
                        }
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
        return true;
    }
}
