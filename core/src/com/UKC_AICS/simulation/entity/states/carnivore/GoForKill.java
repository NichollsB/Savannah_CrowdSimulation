package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.Pursuit;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 16/07/2014.
 * Predator
 */
public class GoForKill extends State {

    private Boid target;

    public GoForKill(StateMachine parent, BoidManager bm, Boid target) {
        super(parent, bm);
        this.target = target;
    }

    @Override
    public boolean update(Boid boid) {
        //check boid still exists
        if(boid.hunger > 60) {
            if (parent.checkBoid(target)) {

                //distance between boid and target
                float distance = boid.getPosition().cpy().sub(target.getPosition()).len2();

                //check if target is collided  and killed
                if (distance <= 16f * 16f) {
                    //Target has been killed.
                    Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f));
                    bm.storeBoidForRemoval(target, food);
                    boid.setVelocity(0f, 0f, 0f);
                    boid.setAcceleration(boid.getVelocity());
                    parent.pushState(boid, new Eat(parent, bm, food));
                    return false;

                } //check target is still within sight range
                else if (distance < boid.sightRadius * boid.sightRadius) {
                    Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
                    Array<Entity> collisionObjects = new Array<Entity>(bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y)));
                    collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
                    collisionObjects.removeValue(target,false);   //remove target from collision avoidance

                    //pursue prey
                    steering.set(0f, 0f, 0f);
                    steering.add(Pursuit.act(boid, target));

                    //Add collision avoidance
                    steering.add(behaviours.get("collision").act(collisionObjects, boid));

                    boid.setAcceleration(steering);

//                    System.out.println("Kill chase: " + target.getSpecies() + " species, " + target.position.x + ", " + target.position.y);
                    return false;
                }
                else {
                    //pop out if target is out of sight
                    return true;
                }


            } else {
                //pop out if target is dead
                return true;
            }
        } else {
            //pop out if not hungry
            return true;
        }
    }
}
