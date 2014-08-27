package com.UKC_AICS.simulation.entity.states.carnivore;

import java.util.Arrays;

import EvolutionaryAlgorithm.EA2;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Arrive;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
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
    private EA2 ea;
    public CarnReproduce(StateMachine parent, BoidManager bm, EA2 ea) {
        super(parent, bm);
        this.ea = ea;
    }

    @Override
    public boolean update(Boid boid) {
        if (boid.hunger < boid.hungerLevel/2 && boid.thirst < boid.thirstLevel/2) {

            boid.setState(this.toString());
            Array<Entity> nearObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));
            Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
            Array<Boid> potentialMates = new Array<Boid>();
            Array<Boid> closeBoids = new Array<Boid>();
            Float[] chromosome = new Float[EA2.getChromosomeLength()];
            
            for (Boid b : nearBoids) {
                //see if the boid is the same species and in the same state - should be Reproduce AND not self
                if (boid.getSpecies() == b.getSpecies() && boid.state.equals(b.state) && !boid.equals(b)) {
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
                // POSSIBLE MATES = POPULATION
               // System.out.print("pm " + potentialMates);
               // System.out.print("EA ON" + ea.getEaOn());
                if(ea.getEaOn()){
                	chromosome = ea.createBaby(boid,potentialMates);
                	//System.out.println("Baby Gene "+Arrays.toString(chromosome));
                }
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
                if(tempVec.len2() < 10f){ // && nearest.hunger < nearest.hungerLevel/2 && nearest.thirst < nearest.hungerLevel/2) {
                   // System.out.println("CARNIVORE boid made a baby " + boid.getSpecies());
                    Boid baby = new Boid(boid);
                    baby.setAge(0);
                   // System.out.println(Arrays.toString(chromosome));
                    if(chromosome[0]!=null){
                    	//System.out.println("not empty");
                    	baby.setChromosome(chromosome);
                    }
                    bm.storeBoidForAddition(baby);
                    boid.hunger = 100;
                    boid.thirst = 100;
                    nearest.hunger = 100;
                    nearest.thirst = 100;
                    return true;
                }


                //Collision avoidance arrays
                Array<Entity> collisionObjects = new Array<Entity>(nearObjects);
                collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
                Vector3 tempVec = new Vector3(0f,0f,0f);
                tempVec.add(Collision.act(collisionObjects, boid));
                tempVec.add(Collision.act(boid));

                if(steering.equals(tempVec)) {
                    steering.add(Arrive.act(boid, nearest.getPosition()));
//                steering.add(Collision.act(boid));
                }
                else {
                    steering.set(tempVec);
                }
                boid.setAcceleration(steering);
            } else {
                //stay with the herd

                Array<Entity> dummyObjects = new Array<Entity>(nearObjects);
                for (Entity dummyObject : dummyObjects) {
                    Entity ent = dummyObject;
                    steering.set(boid.position);
                    steering.sub(ent.position);

                    if (steering.len2() > boid.sightRadius * boid.sightRadius) {
                        dummyObjects.removeValue(ent, false);
                    }
                }
                Array<Entity> collisionObjects = new Array<Entity>(dummyObjects);
                collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
                Vector3 tempVec = new Vector3(0f,0f,0f);
                tempVec.add(Collision.act(collisionObjects, boid));
                tempVec.add(Collision.act(boid));

                steering.set(0f, 0f, 0f);

                if(steering.equals(tempVec)) {
                    float coh = SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
                    float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
                    float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
                    float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander();

                    steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
                    steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
                    steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
                    steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));
                }
                else {
                    steering.set(tempVec);
                }
//                steering.add(Collision.act(collisionObjects, boid));
//                steering.add(Collision.act(boid));

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
