package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.states.Thirsty;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 10/07/2014.
 */
public class HerbDefault extends State {




    public HerbDefault(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }


    @Override
    public boolean update(Boid boid) {


        if(boid.thirst < 15) {
            System.out.println(boid + "\n Just posted Thirsty state "  );
            parent.pushState(boid, new Thirsty(parent, bm));
        }
        else if(boid.hunger < 15) {
            System.out.println(boid + "\n Just posted Hungry state "  );
            parent.pushState(boid, new Hungry(parent,bm));
        } else {


            Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
            Array<Boid> closeBoids = new Array<Boid>();

            /*
            * CELL  ATTEMPTS.
            */

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
            for (Entity ent : dummyObjects) {
//                if (ent.getPosition().dst2(boid.getPosition()) > boid.sightRadius) {
                steering.set(boid.position);
                steering.sub(ent.position);
                if (steering.len2() > boid.sightRadius * boid.sightRadius) {
                    dummyObjects.removeValue(ent, false);
                }
            }

            float coh = SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
            float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
            float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
            float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander();

            //do stuff
            steering.set(0f, 0f, 0f);

            steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
            steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
            steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
            steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));


//            steering.add(behaviours.get("repeller").act(nearBoids, dummyObjects, boid).scl(0.2f));
//            steering.add(behaviours.get("attractor").act(nearBoids, dummyObjects, boid).scl(0.2f));

            // NaN check
//            if (steering.x != steering.x) {
//                System.out.println("blerpy");
//            }


            //store the steering movement
            boid.setAcceleration(steering);
        }


        return false; //base state so no popping.
    }
}
