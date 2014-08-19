package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.behaviours.Seek;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 16/07/2014.
 */

public class Stalk extends State {

    private Boid target;

    public Stalk(StateMachine parent, BoidManager bm, Boid target) {
        super(parent, bm);
        this.target = target;
    }

    @Override
    public boolean update(Boid boid) {
        //check boid still exists
        if( target != null && boid.hunger > boid.hungerLevel/2) {
            if (parent.checkBoid(target)) {
                float distance = boid.getPosition().cpy().sub(target.getPosition()).len2();
                //check still within sight
                if (distance < boid.sightRadius * boid.sightRadius) {
                    Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
                    Array<Entity> collisionObjects = new Array<Entity>(bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y)));
                    collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
                    collisionObjects.removeValue(target,false); //remove target from collision avoidance

                    Vector3 tv = new Vector3(0f, 0f, 0f);
                    Vector3 targetPos = new Vector3(0f, 0f, 0f);
                    targetPos.set(target.getPosition().cpy());
                    tv.set(target.getVelocity().cpy());
                    tv.scl(-1f);
                    tv.nor().scl(10f);
                    targetPos.add(tv);

                    steering.set(Seek.act(boid, targetPos));
//                    System.out.println("Target stalked: " + target.getSpecies() + " species, " + target.position.x + ", " + target.position.y);

                    //Add collision avoidance
                    steering.add(Collision.act(collisionObjects, boid));
                    steering.add(Collision.act(boid));

                    boid.setAcceleration(steering);
                    //Check is boid is still in list.  If not pop to hunt (for corpse)
                    //check if prey is close enought to be chased down
                    if (distance < 200f * 200f) {
                        parent.pushState(boid, new GoForKill(parent, bm, target));
                    }
                }
                //TODO Stalk steering behaviour --> follow leader
                return false;
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
