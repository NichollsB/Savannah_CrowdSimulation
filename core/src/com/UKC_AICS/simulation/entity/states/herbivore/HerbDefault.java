package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.entity.states.Thirsty;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 10/07/2014.
 */
public class HerbDefault extends State {


    private Vector3 tempVec = new Vector3();

    public HerbDefault(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }


    @Override
    public boolean update(Boid boid) {

//
//            //find objects nearby
//            Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));
//            for (Entity ent : dummyObjects) {
////                if (ent.getPosition().dst2(boid.getPosition()) > boid.sightRadius) {
//                steering.set(boid.position);
//                steering.sub(ent.position);
//                if (steering.len2() > boid.sightRadius * boid.sightRadius) {
//                    dummyObjects.removeValue(ent, false);
//                }
//            }


        if (boid.thirst > 85) {
//            System.out.println(boid + "\nJust posted Thirsty state ");
            parent.pushState(boid, new Thirsty(parent, bm));
        } else if (boid.hunger > 75) {
//            System.out.println(boid + "\nJust posted Hungry state ");
            parent.pushState(boid, new Hungry(parent, bm));
        } else if (boid.age > SimulationManager.speciesData.get(boid.getSpecies()).getMaturity() && boid.hunger < 35 && boid.thirst < 35) {
//            System.out.println(boid + "\nJust posted Reproduce state ");
            parent.pushState(boid, new Reproduce(parent, bm));
//        } else if (boid.age > 10 && boid.hunger > 70 && boid.thirst > 70) {
////            System.out.println(boid + "\nJust posted Reproduce state ");
//            parent.pushState(boid, new Reproduce(parent, bm));
        } else {
            boid.setState(this.toString());

//            old boid getter
//            Array<Boid> nearBoids = new Array<Boid>(BoidManager.getBoidGrid().findNearby(boid.getPosition()));

            Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);

            Array<Boid> closeBoids = new Array<Boid>();
            Array<Boid> predators = new Array<Boid>();

            for (Boid b : nearBoids) {
                if (SimulationManager.speciesData.get(b.getSpecies()).getDiet().equals("carnivore")) {
                    predators.add(b);
                } else {
                    steering.set(boid.getPosition());
                    steering.sub(b.getPosition());
                    if (steering.len() > boid.flockRadius) {
                        nearBoids.removeValue(b, true);
                    }
                    //if the boid is outside the flock radius it CANT also be in the "too close" range
                    else if (steering.len() < boid.nearRadius * boid.nearRadius) {
                        closeBoids.add(b);
                    }
                }
            }

            if(predators.size > 0) {
                for(Boid predator : predators) {
                    boid.panic += 10;
                }
                if (boid.panic > 30) {
                    parent.pushState(boid, new Panic(parent, bm));
                }
            } else if (boid.panic > 0 && predators.size == 0) {
                boid.panic -= 10;
            }

            //store the steering movement
            boid.setAcceleration(steering);  //Resets acceleration to 0f,0f,0f
            Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

            //Collision avoidance arrays
            Array<Entity> collisionObjects = new Array<Entity>(dummyObjects);
            collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
//            tempVec = Collision.act(collisionObjects, boid);
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

                float coh = boid.cohesion; //SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
                float sep = boid.separation; //SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
                float ali = boid.alignment; //SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
                float wan = boid.wander; //SimulationManager.speciesData.get(boid.getSpecies()).getWander();

//                float coh = SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
//                float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
//                float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
//                float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander();

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
        }
        return false; //base state so no popping.
    }
}
