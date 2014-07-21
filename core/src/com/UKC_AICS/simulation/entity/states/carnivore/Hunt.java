package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
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

        if (boid.hunger < 50) {

            Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

            //TODO check for food/corpse objects first

            Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
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

            tempVec = behaviours.get("collision").act(collisionObjects, boid);

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
                boid.setAcceleration(tempVec);
            }

//        Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
//        Array<Boid> closeBoids = new Array<Boid>();
//
//        for (Boid b : nearBoids) {
//            steering.set(boid.getPosition());
//            steering.sub(b.getPosition());
//            // Check possible prey boids are within sight radius and not predator boid
//            if (steering.len() < boid.sightRadius && !b.equals(boid)) {
//                closeBoids.add(b);
//            }
//        }
            //temporary arbitrary selection of first boid as chosen prey

                Array<Boid> rmList = new Array<Boid>();
                for (Boid target : closeBoids) {
                    if (boid.getSpecies() == target.getSpecies()) {
                        rmList.add(target);
                    }
                }
                //remove all same species/byte boids from possible target array
                closeBoids.removeAll(rmList, false);
            if (closeBoids.size > 0) {
                //TODO change this to pick closest none same species boid/ weak/ injured
                target = closeBoids.first();
                parent.pushState(boid, new Stalk(parent, bm, target));  //Pushs to stalk mode on prey target
            }
            // pop
            return false; //stop looking for food.
        } else {

            return true;
        }
    }


    public boolean inSightRange(int x, int y) {


        return true;
    }
}
