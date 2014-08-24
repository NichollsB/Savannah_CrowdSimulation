package com.UKC_AICS.simulation.entity.behaviours;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.UKC_AICS.simulation.world.LandMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by James on 10/07/2014.
 */
public class Collision extends Behaviour {

    private static Vector3 tmpVec = new Vector3(0f,0f,0f);
    private static Vector3 tmpVec2 = new Vector3(0f,0f,0f);

    static float MAX_AVOID_FORCE = 0.06f;
    static float LOOK_AHEAD = 20f;
    static float HALF_LOOK_AHEAD = LOOK_AHEAD/2f;
    static int tileSize = Constants.TILE_SIZE;
    static int stepsAhead = 60;

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Collision is not to be used in this manner. Try static access Collision.act(Array<Entity> targets, Boid boid)");
    }



    /**
     * For collision avoidance with terrain along boid trajectory
     * @param boid that is doing the collision checking
     * @return Vector3 that is a velocity correctional change in acceleration
     */
    public static Vector3 act(Boid boid) {
        tmpVec.set(boid.getPosition());
        tmpVec2.set(boid.getVelocity());



        ArrayList<Integer> cell = cellInPath(tmpVec.x, tmpVec.y, tmpVec2.x, tmpVec2.y);

        tmpVec.set(0f,0f,0f);
//        if(cell != null) {
//            int cellX = cell.get(0)/tileSize;
//            int cellY = cell.get(1)/tileSize;
//            tmpVec.set(boid.getPosition());
//            tmpVec.add(tmpVec2.scl(cell.get(2)));
//            tmpVec.sub(new Vector3(cellX * tileSize + tileSize / 2, cellY * tileSize + tileSize / 2, 0f));
//            tmpVec.nor();
//            tmpVec.scl(MAX_AVOID_FORCE);
//        }
        if(cell != null) {
            //tile position where boid is
            int mapX = (int)(tmpVec.x/tileSize);
            int mapY = (int)(tmpVec.y/tileSize);
            //work out which point of the cell/tile is the closest to calculate collision avoidance vector.
//            tmpVec.set(boid.getPosition());
//            tmpVec.add(tmpVec2.scl(cell.get(2)));  //add the velocity*no of steps to get collision position
//            tmpVec.set(boid.getPosition());
//
//                tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize+tileSize/2,0f));
//                tmpVec.nor();
//                tmpVec.scl(MAX_AVOID_FORCE);
                // the tile is impassable,
            // need to do collision avoidance with  the intersect of closest edge and velocity intersect
            tmpVec.set(boid.getPosition());
            Vector2 intersect = new Vector2();
            //TODO is this getting the correct line for nearest edge of cell?
            if(mapX==cell.get(0) && mapY==cell.get(1)) {
                tmpVec.sub(new Vector3(mapX*tileSize+tileSize/2,mapY*tileSize+tileSize/2,0f));
                tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize+tileSize/2,0f));
                tmpVec.nor();
                tmpVec.scl(MAX_AVOID_FORCE);
            }
            else if(mapX < cell.get(0)) {
                if(mapY == cell.get(1)){ //  && Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell.get(0) * tileSize + tileSize, cell.get(1) * tileSize, cell.get(0) * tileSize + tileSize,
//                            cell.get(1) * tileSize + tileSize, intersect)) {
                    // avoid to right

//                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize+tileSize,mapY*tileSize+tileSize/2,0f)); //avoid right side of boids tile
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize+tileSize/2,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize+tileSize/2,0f)));
                }
                else if (mapY < cell.get(1)) {
                    // avoid up and right
//                        tmpVec.sub(new Vector3(cell.get(0)*tileSize + tileSize, cell.get(1)*tileSize + tileSize, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize+tileSize,mapY*tileSize+tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize+tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize+tileSize,0f)));
                }
                else if(mapY > cell.get(1)) {
                    // avoid down and right
//                        tmpVec.sub(new Vector3(cell.get(0)*tileSize + tileSize, cell.get(1)*tileSize, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize+tileSize,mapY*tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize,cell.get(1)*tileSize,0f)));
                }
            }
            else if(mapX > cell.get(0)) {
                if(mapY == cell.get(1)){ // && Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell.get(0) * tileSize, cell.get(1) * tileSize, cell.get(0) * tileSize,
//                            cell.get(1) * tileSize + tileSize, intersect)) {
                    // avoid left
//                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize,mapY*tileSize+tileSize/2,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize+tileSize/2,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize+tileSize/2,0f)));
                }
                else if (mapY < cell.get(1)) {
                    // avoid up and left
//                        tmpVec.sub(new Vector3(cell.get(0)*tileSize, cell.get(1)*tileSize + tileSize, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize,mapY*tileSize+tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize+tileSize,0f)));
                }
                else if(mapY > cell.get(1)) {
                    // avoid down and left
//                        tmpVec.sub(new Vector3(cell.get(0)*tileSize, cell.get(1)*tileSize, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize,mapY*tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize+tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize+tileSize,cell.get(1)*tileSize,0f)));
                }
            } else if (mapX == cell.get(0)) {
                if (mapY < cell.get(1)){ //&& Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell.get(0) * tileSize + tileSize, cell.get(1) * tileSize, cell.get(0) * tileSize + tileSize,
//                            cell.get(1) * tileSize + tileSize, intersect)) {
                    // avoid up
//                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize+tileSize/2,mapY*tileSize+tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize,0f)));
                }
                else if(mapY > cell.get(1)){ //&& Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell.get(0) * tileSize, cell.get(1) * tileSize, cell.get(0) * tileSize,
//                            cell.get(1) * tileSize + tileSize, intersect)) {
                    // avoid down
//                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
                    tmpVec.sub(new Vector3(mapX*tileSize+tileSize/2,mapY*tileSize,0f));
//                    tmpVec.sub(new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize+tileSize,0f));
                    tmpVec.nor();
                    tmpVec.scl(MAX_AVOID_FORCE);

//                        tmpVec.set(Avoid.act(boid,new Vector3(cell.get(0)*tileSize+tileSize/2,cell.get(1)*tileSize+tileSize,0f)));
                }
            }
        }



        //get tile info at position tile
        //ray cast to check when tile changes along velocity and retrieve tile info of new tiles

        if(!tmpVec.equals(new Vector3(0f,0f,0f))) {
            boid.setAvoidance();
            System.out.println("Avoided collision with " + cell.get(0) + ", " + cell.get(1) + ". " + cell.get(2) + " steps ahead. Distance = " + boid.velocity.len2()*cell.get(2));
            System.out.println("Correction vector of: " + tmpVec);
        }
        return tmpVec;
    }

    /**
     * Finds the first cell along the boids trajectory
     * @param x0
     * @param y0
     * @param dx_dt
     * @param dy_dt
     * @return
     */
    private static ArrayList<Integer> cellInPath(float x0, float y0, float dx_dt, float dy_dt) {
        ArrayList<Integer> cell = null;
        for(int i = 0; i < stepsAhead; i++){
            int posX = (int)(x0 + i * dx_dt);
            int posY = (int)(y0 + i * dy_dt);
            int posToCheckX = posX;
            int posToCheckY = posY;
            //TODO export to a static map wrapping function for X and Y?
            if(posX >= Constants.mapWidth) {
                posToCheckX = posX - Constants.mapWidth;
            }
            else if(posX < 0) {
                posToCheckX = Constants.mapWidth + posX;
            }
            if(posY >= Constants.mapHeight) {
                posToCheckY = posY - Constants.mapHeight;
            }
            else if(posY < 0) {
                posToCheckY = Constants.mapHeight + posY;
            }

            //TODO need a check for outofbounds, wrap check for blocked, but steer from extended map coords
            if(WorldManager.getTileInfoAt(posToCheckX, posToCheckY).get("blocked") == 1) {
                cell = new ArrayList<Integer>();
                cell.add(posX);
                cell.add(posY);
                cell.add(i);
                break;
            }
        }
        return cell;
    }


    /**
     * Checks the Array of targets for most Threatening collision with boid
     * @param boid  which is checking for collisions
     * @param targets   possible collision targets (just Entity's within sight range or all?)
     * @return  a correction Vector3 to avoid collision with most threatening
     */
    public static Vector3 act(Array<Entity> targets, Boid boid) {
        Array<Entity> entities = targets;  //
//        Object cell = (Object) terrainCollision(boid);
//        if(cell != null) {
//            entities.add(cell);
//            int cellX = (int) cell.getPosition().x/tileSize;
//            int cellY = (int) cell.getPosition().y/tileSize;
//        }
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


                }
                else if(turn == -1) {
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
        if(!adjustment.equals(new Vector3(0f,0f,0f))) {
            boid.setAvoidance();
        }
        return adjustment;
    }

    /**
     * This will check current position collision of the boid with the target.
     * @param boid the boid to be checked for collision
     * @param target the target who the boid is checking collision with
     * @return true on a collision, false if no collision
     */
    private static boolean collisionCheck(Boid boid, Entity target){
        //collision check here
        boolean collision = false;
        tmpVec.set(boid.getPosition());
        //check new position that is 2xthe current velocity ahead( 2 moves ahead not accounting for acceleration)
//        tmpVec2.set(boid.getVelocity());
//        tmpVec.add(tmpVec2);
        tmpVec.sub(target.getPosition());

        if (tmpVec.len() < 16f) {
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
    private static boolean lookAheadCheck(Boid boid, Entity target){
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
    private static boolean lookHalfAheadCheck(Boid boid, Entity target) {
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
    private static boolean checkRight(Boid boid, Entity target) {
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

    private static boolean checkLeft(Boid boid, Entity target) {
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
