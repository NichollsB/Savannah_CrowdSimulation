package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Avoid;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.behaviours.Evade;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 28/07/2014.
 */
public class Panic extends State {
    public Panic(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
        Array<Boid> predators = new Array<Boid>();
        Array<Entity> nearEntities = new Array<Entity>();
        nearEntities.addAll(nearBoids);

        for (Boid b : nearBoids) {
            if (SimulationManager.speciesData.get(b.getSpecies()).getDiet().equals("carnivore")) {
                predators.add(b);
            }
        }
        steering.set(0f,0f,0f);
        Vector3 tempVec = new Vector3(0f,0f,0f);
        tempVec.add(Collision.act(nearEntities, boid));
        tempVec.add(Collision.act(boid));
        if(steering.equals(tempVec)) {
            if (predators.size > 0) {
                for (Boid predator : predators) {
                    steering.add(Avoid.act(boid, predator.getPosition()));//.scl(predator.size/boid.size));
                }

                steering.add(Collision.act(nearEntities, boid));
                steering.add(Collision.act(boid));
            } else {
                boid.panic -= 10;
            }
        }
        else {
            steering.set(tempVec);
        }

        steering.nor().limit(boid.maxForce).scl(boid.maxSpeed);
//        steering.add(parent.behaviours.get("wander").act(nearBoids, new Array<Entity>(), boid)).scl(boid.wander);
        if (steering.isZero()) {
            boid.setAcceleration(boid.velocity);
        } else {
            boid.setAcceleration(steering);
        }


        //popping decisions.
        if(boid.panic < 30 ) {
            return true;
        } else {
            return false;
        }

    }
}
