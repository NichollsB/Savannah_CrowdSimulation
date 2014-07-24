package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by James on 10/07/2014.
 */
public class Collision extends Behaviour {

    private float initialCheckRadius = 20f;
    private Vector3 tmpVec = new Vector3(0f,0f,0f);
    private Vector3 tmpVec2 = new Vector3(0f,0f,0f);
    Random rand = new Random();

    float MAX_AVOID_FORCE = 0.3f;
    float LOOK_AHEAD = 20f;
    float HALF_LOOK_AHEAD = LOOK_AHEAD/2f;


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
    public Vector3 act(Array<Entity> targets, Boid boid) {
        Array<Entity> entities = targets;  //
        // targets that are within a check range check, to be checked further
        Array<Entity> collisionThreats = new Array<Entity>();
        Vector3 adjustment = new Vector3(0f, 0f, 0f);
        boolean adjustmentSet = false;
        //iterate through initial array to find proximity threats
        for (int i = 0; i < entities.size; i++) {
            Entity target = entities.get(i);
            //check target is not self
            if (!boid.equals(target)) {
                //temporarily use adjustment vector to calculate distances
                tmpVec.set(boid.getPosition());
                tmpVec.sub(target.getPosition());
                //check if distance to target is less than boid sight range
                if (tmpVec.len() < 100f) {
                    //if close, add to the collision threats
                    collisionThreats.add(target);
                }
            }
        }
        if(collisionThreats.size > 0) {
            for (int i = 0; i < collisionThreats.size; i++) {
                Entity target = collisionThreats.get(i);
                //TODO another distance check for so checkLeft checkRight are not carried out unless within close prox
                int turn = Intersector.pointLineSide(boid.position.x, boid.position.y, (tmpVec.set((boid.getPosition())).add(boid.getVelocity())).x, (tmpVec.set((boid.getPosition())).add(boid.getVelocity())).y, target.getPosition().x, target.getPosition().y);
                if (collisionCheck(boid, target)) {
                    adjustment.set(tmpVec);
                    adjustment.nor();
                    adjustment.scl(MAX_AVOID_FORCE);
//                    adjustment.scl(0.6f);
                    adjustmentSet = true;


                } else
                if(turn == -1) {
                    if (checkLeft(boid, target)) {
                        //calculate adjustment vector here
                        //should be able to use the tmpVec and tmpVec2 used to calc true.
                        adjustment.set(tmpVec);
                        adjustment.nor();
                        adjustment.scl(MAX_AVOID_FORCE);
                        adjustmentSet = true;
//                        System.out.println("collision, close left");
                    }
                }
                else if(turn == 1) {
                    if (checkRight(boid, target)) {
                        adjustment.set(tmpVec);
                        adjustment.nor();
                        adjustment.scl(MAX_AVOID_FORCE);
                        adjustmentSet = true;
//                        System.out.println("collision, close right");
                    }
                }
                if(!adjustmentSet){
                    if (lookHalfAheadCheck(boid, target)) {
                        //TODO add side check for left or right turn
                        //calculate adjustment vector here
                        adjustment.set(tmpVec);
                        adjustment.nor();
                        adjustment.scl(MAX_AVOID_FORCE);
//
                        }
                    } else if (lookAheadCheck(boid, target)) {
                        //calculate adjustment vector here
                    adjustment.set(tmpVec);
                    adjustment.nor();
                    adjustment.scl(MAX_AVOID_FORCE);
//
                    }
//                }
            }
        }
        return adjustment;
    }

    private boolean collisionCheck(Boid boid, Entity target){
        //collision check here
        boolean collision = false;
        tmpVec.set(boid.getPosition());
        //check new position that is 2xthe current velocity ahead( 2 moves ahead not accounting for acceleration)
//        tmpVec2.set(boid.getVelocity());
//        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());

        if (tmpVec.len() < 8f) {
            collision = true;
        }
        return collision;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (sightRadius maybe?)
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean lookAheadCheck(Boid boid, Entity target){
        //collision check here
        boolean collision = false;
        tmpVec.set(boid.getPosition());
        //check new position that is 2xthe current velocity ahead( 2 moves ahead not accounting for acceleration)
        tmpVec2.set(boid.getVelocity());
        tmpVec2.scl(LOOK_AHEAD);

//        System.out.println(" ahead " + tmpVec2.len());

        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());

        if (tmpVec.len() < 16f) {
            collision = true;
        }
        return collision;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (half sightRadius maybe?). this requires more drastic course correction to loohAheadCheck due to closer proximity
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean lookHalfAheadCheck(Boid boid, Entity target) {
        //collision check here
        boolean collision = false;
        tmpVec.set(boid.getPosition());
        //check new position that is the current velocity ahead( 1 moves ahead not accounting for acceleration)
        tmpVec2.set(boid.getVelocity());
        tmpVec2.scl(HALF_LOOK_AHEAD);

//        System.out.println("half ahead " + tmpVec2.len());

        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());

        if (tmpVec.len() < 16f) {
            collision = true;
        }
        return collision;
    }

    /**
     * This will check ahead of the boid and determine whether the target will collide within a certain distance
     * (1/3 sightRadius maybe?), at 45 degree angle from true vector (checks sides)
     * @param boid  the Entity that is moving and needs its path checked for collisions
     * @param target  the possible Entity that the boid may collide with
     * @return  a boolean as to whether a collision will occur on current Vector
     */
    private boolean checkRight(Boid boid, Entity target) {
        //collision check here
        boolean collision = false;
        //need to add 0.5f velocity to current position,
        //project a new position off at +/- 45degrees from current velocity
        //then check the collision by distance of centres of boid/target
        tmpVec.set(boid.getPosition());
        tmpVec2.set(boid.getVelocity());
        tmpVec2.scl(0.8f);
        tmpVec2.rotate(25f,0f,0f,1f);
        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());
        if (tmpVec.len() < 16f) {
            collision = true;
        }
        return collision;
    }

    private boolean checkLeft(Boid boid, Entity target) {
        //collision check here
        boolean collision = false;
        //need to add 0.5f velocity to current position,
        //project a new position off at +/- 45degrees from current velocity
        //then check the collision by distance of centres of boid/target
        tmpVec.set(boid.getPosition());
        tmpVec2.set(boid.getVelocity());
        tmpVec2.scl(0.8f);
        tmpVec2.rotate(-25f,0f,0f,1f);
        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());
        if (tmpVec.len() < 16f) {
            collision = true;
        }
        return collision;
    }
}
