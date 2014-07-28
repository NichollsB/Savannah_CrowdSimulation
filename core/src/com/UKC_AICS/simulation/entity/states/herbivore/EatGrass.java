package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 10/07/2014.
 */
public class EatGrass extends State{
    public EatGrass(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {
        if(boid.position.x < 0 || boid.position.x > Constants.screenWidth || boid.position.y < 0 || boid.position.y > Constants.screenHeight) {
            System.out.println("I am out of bounds" + boid.position.x + " , " + boid.position.y);
        }
        byte grassAmount = WorldManager.getTileInfoAt((int) boid.position.x, (int) boid.position.y).get("grass");

        if( grassAmount >= 10 && boid.hunger > 80) {

            //Entities to check collision with
            Array<Boid> nearBoids = BoidManager.getBoidGrid().findNearby(boid.getPosition());
            Array<Entity> collisionObjects = new Array<Entity>(bm.parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y)));
            collisionObjects.addAll(nearBoids);   //add boids nearby to collision check
            steering.set(0f, 0f, 0f);

            //just add collision avoidance
            steering.add(behaviours.get("collision").act(collisionObjects, boid));  //.scl(avoid)   //Maybe have some scaling for avoidance?
            steering.nor().scl(boid.maxSpeed / 2);
            steering.sub(boid.getVelocity());
            steering.limit(boid.maxForce);

            boid.setAcceleration(steering);

            boid.setState(this.toString());
//            boid.setAcceleration(new Vector3(0f, 0f, 0f).sub(boid.velocity));
            grassAmount -= 1;
            boid.hunger -= 1;
            WorldManager.changeTileOnLayer(boid.position.x, boid.position.y, "grass", grassAmount);
        } else {
//            System.out.println(boid + "\n Just quit EATGRASS state "  );
            return true;
        }

        return false;
    }
}
