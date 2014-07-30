package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.Collision;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 10/07/2014.
 */
public class Hungry extends State {

    public Hungry(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {
        //search for food.

        if (boid.hunger < 40) {
            // pop
            return true; //stop looking for food.
        } else {
            boid.setState(this.toString());

            if (WorldManager.getTileInfoAt((int) boid.position.x, (int) boid.position.y).get("grass") >= 20) {
//                System.out.println(boid + "\n Just posted EATGRASS state "  );
                parent.pushState(boid, new EatGrass(parent, bm));
                boid.setAcceleration(new Vector3(boid.velocity).scl(0.01f));
            }
            //TODO add some steering to find food if there is none on tile
            Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
            Array<Boid> closeBoids = new Array<Boid>();

            /*
            * CELL  ATTEMPTS.
            *
            *
            */
            //find objects nearby
            Array<Entity> dummyObjects = bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));

            Array<Entity> collisionObjects = new Array<Entity>(dummyObjects);
            collisionObjects.addAll(nearBoids);   //add boids nearby to collision check


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



//            float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander() / 2;
            float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
            float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();

            steering.set(0f, 0f, 0f);

            steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
            steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
//            steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

            //Add collision avoidance
//            steering.add(Collision.act(collisionObjects, boid));
            steering.add(behaviours.get("collision").act(collisionObjects, boid));

            steering.nor().scl(boid.maxSpeed / 2);
            steering.sub(boid.getVelocity());
            steering.limit(boid.maxForce);

            boid.setAcceleration(steering);

            return false; //keep in this state.
        }
    }


}
