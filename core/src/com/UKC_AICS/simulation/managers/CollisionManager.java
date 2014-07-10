package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by James on 10/07/2014.
 */
public class CollisionManager {

    public CollisionManager(){}

    public Boid checkCollision(Circle newCircle, Array<Boid> checkBoids, Boid boid){
        boolean collision = false;
        Circle newPos = newCircle;
        Vector3 newVec = new Vector3(newPos.x, newPos.y, 0f);
        Array<Boid> boids = checkBoids;
        Array<Boid> overlap = new Array<Boid>();
        for (int i = 0; i < checkBoids.size; i++) {
             Boid check = boids.get(i);
            if(boid != check) {
                if (newCircle.overlaps(check.getCircle())) {
                    collision = true;
                    overlap.add(check);
                }
            }
        }
        Boid closest = null;
        for (int i = 0; i < overlap.size; i++) {
             if(closest == null) {
                 closest = overlap.get(i);
             }
            else if((newVec.cpy().sub(overlap.get(i).getPosition()).len() < ((newVec.cpy()).sub(closest.getPosition())).len())){
                closest = overlap.get(i);
            }
        }
        return closest;
    }

    public void checkObjectCollisions(Array<Entity> dummyObjects, Boid boid) {
        for (int i = 0; i < dummyObjects.size; i++) {
             if(boid.circle.overlaps(dummyObjects.get(i).circle)) {

             }
        }
    }
}
