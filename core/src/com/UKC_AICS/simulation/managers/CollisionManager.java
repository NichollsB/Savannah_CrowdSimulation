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

//    public Boid checkCollision(Circle newCircle, Array<Boid> checkBoids, Boid boid){
//        boolean collision = false;
//        Circle newPos = newCircle;
//        Vector3 newVec = new Vector3(newPos.x, newPos.y, 0f);
//        Array<Boid> boids = checkBoids;
//        Array<Boid> overlap = new Array<Boid>();
//        for (int i = 0; i < checkBoids.size; i++) {
//             Boid check = boids.get(i);
//            if(boid != check) {
//                if (newCircle.overlaps(check.getCircle())) {
//                    collision = true;
//                    overlap.add(check);
//                }
//            }
//        }
//        Boid closest = null;
//        for (int i = 0; i < overlap.size; i++) {
//             if(closest == null) {
//                 closest = overlap.get(i);
//             }
//            else if((newVec.cpy().sub(overlap.get(i).getPosition()).len() < ((newVec.cpy()).sub(closest.getPosition())).len())){
//                closest = overlap.get(i);
//            }
//        }
//        return closest;
//    }
//
//    public void checkObjectCollisions(Array<Entity> dummyObjects, Boid boid) {
//        for (int i = 0; i < dummyObjects.size; i++) {
//             if(boid.circle.overlaps(dummyObjects.get(i).circle)) {
//
//             }
//        }
//    }


    /**
     * Checks the Array of targets for mostTreatening collision with boid
     * @param boid  which is checking for collisions
     * @param targets   possible collision targets (just Entity's within sight range or all?)
     * @return  a correction Vector3 to avoid collision with most threatening
     */
    public Vector3 checkCollision(Entity boid, Array<Entity> targets) {
        Array<Entity> entities = targets;
        Array<Entity> collisionThreats = new Array<Entity>();
        Vector3 adjustment = new Vector3(0f, 0f, 0f);
        for (int i = 0; i < entities.size; i++) {
            Entity target = entities.get(i);

            if (lookAheadCheck(boid, target)) {
                //calculate adjustment vector here

            } else if (lookHalfAheadCheck(boid, target)) {
                //calculate adjustment vector here

            } else if (sideCheck(boid, target)) {
                //calculate adjustment vector here

            }
        }
        return adjustment;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (sightRadius maybe?)
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean lookAheadCheck(Entity boid, Entity target){
        //collision check here
        return false;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (half sightRadius maybe?). this requires more drastic course correction to loohAheadCheck due to closer proximity
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean lookHalfAheadCheck(Entity boid, Entity target) {
        //collision check here
        return false;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (1/3 sightRadius maybe?), at 45 degree angle from true vector (checks sides)
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean sideCheck(Entity boid, Entity target) {
        //collision check here
        return false;
    }


}
