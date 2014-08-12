package com.UKC_AICS.simulation.entity.states;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

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
        if ( boid.thirst < 45) {
            return true;  //not thirsty return to default
        } else {
            boid.setState(this.toString());
            if(boid.position.x < 0 || boid.position.x > Constants.mapWidth || boid.position.y < 0 || boid.position.y > Constants.mapHeight) {
                System.out.println("I am out of bounds" + boid.position.x + " , " + boid.position.y);
            }
            //search for water
            byte waterAmount = WorldManager.getTileInfoAt((int) boid.position.x, (int) boid.position.y).get("water");
            if(waterAmount >= 10) {
                parent.pushState(boid, new Drink(parent, bm));
                boid.setVelocity(0f, 0f, 0f);
                boid.setAcceleration(boid.getVelocity());
            }
            else {

                Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
                Array<Boid> closeBoids = new Array<Boid>();

                for (Boid b : nearBoids) {
                    steering.set(boid.getPosition());
                    steering.sub(b.getPosition());
                    if (steering.len() > boid.flockRadius) {
                        nearBoids.removeValue(b, true);
                    }
                    //if the boid is outside the flock radius it CANT also be in the "too close" range
                    else if (steering.len() < boid.nearRadius) {
                        closeBoids.add(b);
                    }
                }

                //find objects nearby
                Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

                //Entities to check collision with
                Array<Entity> collisionObjects = new Array<Entity>(dummyObjects);
                collisionObjects.addAll(nearBoids);   //add boids nearby to collision check

                float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander() / 2;
//                float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
                float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();

                steering.set(0f, 0f, 0f);

                //just add collision avoidance
                steering.add(Collision.act(collisionObjects, boid));  //.scl(avoid)   //Maybe have some scaling for avoidance?
                steering.add(Collision.act(boid));

//                steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
                steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
                steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

                steering.nor().scl(boid.maxSpeed / 2);
                steering.sub(boid.getVelocity());
                steering.limit(boid.maxForce);

                boid.setAcceleration(steering);
            }
            return false;
        }

    }
}
