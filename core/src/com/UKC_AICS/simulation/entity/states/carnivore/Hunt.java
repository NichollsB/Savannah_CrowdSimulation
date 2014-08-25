package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.Behaviour;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.behaviours.Seek;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 16/07/2014.
 */
public class Hunt extends State {

    private Boid target;
    private Vector3 tempVec = new Vector3();


    public Hunt(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        //check still hungry
        if (boid.hunger > boid.hungerLevel/2) {

            Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

            Array<Entity> foodCorpse = new Array<Entity>(dummyObjects);
            //TODO check for food/corpse objects first

            Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid); //.getPosition());
            Array<Boid> closeBoids = new Array<Boid>();

            for (Boid b : nearBoids) {
                steering.set(boid.getPosition());
                steering.sub(b.getPosition());
                // Check possible prey boids are within sight radius and not predator boid
                if (steering.len() < boid.sightRadius && !b.equals(boid)) {
                    closeBoids.add(b);
                }
            }
            //TODO add standard/hunting steering movement
            //TODO add object/boid collision

            Array<Entity> collisionObjects = new Array<Entity>(dummyObjects);
            collisionObjects.addAll(nearBoids);   //add boids nearby to collision check

            tempVec = Collision.act(collisionObjects, boid);
//            tempVec = behaviours.get("collision").act(collisionObjects, boid);
            tempVec.add(Collision.act(boid));


            steering.set(0f, 0f, 0f);
            boid.setAcceleration(steering);

            // Check if collision avoidance is required.  True if no collisions
            if (tempVec.equals(steering)) {
                //find objects nearby

                for (Entity dummyObject : dummyObjects) {
                    Entity ent = dummyObject;
                    steering.set(boid.position);
                    steering.sub(ent.position);

                    if (steering.len2() > boid.sightRadius * boid.sightRadius) {
                        dummyObjects.removeValue(ent, false);
                    }
                }


                steering.set(0f, 0f, 0f);

                //eat or add seek steering to get to corpse
                Entity closestCorpse = null;
                float distanceCorpse = Float.MAX_VALUE;
                for(Entity food : foodCorpse) {
                    if(food.getType() == 0 ){ //&& food.getSubType() == 0) {
                        float distance = boid.getPosition().cpy().sub(food.getPosition()).len2();
                        if(distance < distanceCorpse) {
                            closestCorpse = food;
                            distanceCorpse = distance;
                        }
                    }
                }
                if(distanceCorpse < 16f * 16f) {
                    parent.pushState(boid, new Eat(parent, bm, (Object) closestCorpse));
                    return false;
                } else if (distanceCorpse < boid.sightRadius * boid.sightRadius) {
                    parent.pushState(boid, new ApproachCorpse(parent, bm, (Object) closestCorpse));
                    return false;
                }

                float coh = SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
                float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
                float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
                float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander();

                steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
                steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
                steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
                steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

//                steering.add(behaviours.get("repeller").act(nearBoids, dummyObjects, boid).scl(0.5f));
//                steering.add(behaviours.get("attractor").act(nearBoids, dummyObjects, boid).scl(0.5f));

                boid.setAcceleration(steering);

            } else {
                //collision avoidance
                boid.setAcceleration(tempVec);
            }

            Array<Boid> rmList = new Array<Boid>();
            int sameSpecies = 0;
            for (Boid target : closeBoids) {
                if (SimulationManager.speciesData.get(boid.getSpecies()).getDiet().equals("carnivore")) {
                    sameSpecies++;
                    rmList.add(target);
                }
            }
            //remove all same species/byte boids from possible target array
            closeBoids.removeAll(rmList, false);
            while (closeBoids.size > 0) {
                //TODO change this to pick closest none same species boid/ weak/ injured
                target = closeBoids.pop();
                if(target.size > boid.size*1.3) {
                    if(boid.size + (sameSpecies * boid.size/3) > target.size) {
                        parent.pushState(boid, new Stalk(parent, bm, target));
                        return false;//Pushs to stalk mode on prey target
                    }else {
                        continue; //try another boid.
                    }
                } else {
                    parent.pushState(boid, new Stalk(parent, bm, target));
                    return false;//Pushs to stalk mode on prey target
                }
            }
            return false;
        } else {
            //pop out if not hungry
            return true;
        }
    }


    public boolean inSightRange(int x, int y) {


        return true;
    }
}
