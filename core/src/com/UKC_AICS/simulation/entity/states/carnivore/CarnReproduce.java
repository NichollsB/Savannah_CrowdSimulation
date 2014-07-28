package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Arrive;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by James on 22/07/2014.
 */
public class CarnReproduce extends State {
    private Vector3 tempVec = new Vector3();

    public CarnReproduce(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {
        if (boid.hunger < 60 && boid.thirst < 60) {
            boid.setState(this.toString());

            Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
            Array<Boid> potentialMates = new Array<Boid>();
            Array<Boid> closeBoids = new Array<Boid>();

            for (Boid b : nearBoids) {
                //see if the boid is the same species and in the same state - should be Reproduce.
                if (boid.getSpecies() == b.getSpecies() && boid.state.equals(b.state)) {
                    potentialMates.add(b);
                }
                steering.set(boid.getPosition());
                steering.sub(b.getPosition());
                if (steering.len2() > boid.flockRadius * boid.flockRadius) {
                    nearBoids.removeValue(b, true);
                }
                //if the boid is outside the flock radius it CANT also be in the "too close" range
                else if (steering.len2() < boid.nearRadius * boid.nearRadius) {
                    closeBoids.add(b);
                }
            }
            //TODO need steering to find mates
            if(potentialMates.size > 0  ) {
                //pick the closest and go towards it!
                Boid nearest = potentialMates.pop();
                Boid other;
                while(potentialMates.size > 0) {
                    other = potentialMates.pop();
                    steering.set(boid.getPosition());
                    steering.sub(other.getPosition());

                    tempVec.set(boid.getPosition());
                    tempVec.sub(nearest.getPosition());
                    if (tempVec.len2() < steering.len2()) {
                        nearest = other;
                    }

                }
                if(tempVec.len2() < 10f && nearest.hunger < 60 && nearest.thirst < 60) {
                    System.out.println("CARNIVORE boid made a baby " + boid.getSpecies());
//                    bm.createBoid(boid); //create copy of self.
                    Boid baby = new Boid(boid);
                    baby.setAge(0);
                    bm.storeBoidForAddition(baby);
                    boid.hunger = 100;
                    boid.thirst = 100;
                    nearest.hunger = 100;
                    nearest.thirst = 100;
                    return true;
                }
                steering.set(0f,0f,0f);

                steering.add(Arrive.act(boid, nearest.getPosition()));

                boid.setAcceleration(steering);

            } else {
                //stay with the herd

                Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

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
            }

            return false;

        } else {
            return true;
        }
    }
}
