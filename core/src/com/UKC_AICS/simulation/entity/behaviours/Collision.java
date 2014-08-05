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

import java.util.HashMap;
import java.util.Random;

/**
 * Created by James on 10/07/2014.
 */
public class Collision extends Behaviour {

    private static Vector3 tmpVec = new Vector3(0f,0f,0f);
    private static Vector3 tmpVec2 = new Vector3(0f,0f,0f);

    static float MAX_AVOID_FORCE = 0.2f;
    static float LOOK_AHEAD = 20f;
    static float HALF_LOOK_AHEAD = LOOK_AHEAD/2f;
    static int tileSize = Constants.TILE_SIZE;
    static int stepsAhead = 40;

    public Vector3 act(Array<Boid> boids, Array<Entity> objects, Boid boid) {
        throw new Error("Collision is not to be used in this manner. Try static access Collision.act(Array<Entity> targets, Boid boid)");
    }

    private static Entity terrainCollision(Boid boid) {
        tmpVec.set(boid.getPosition());
        tmpVec2.set(boid.getVelocity());
        Array<Integer> cell = cellsInPath(tmpVec.x, tmpVec.y, tmpVec2.x, tmpVec2.y);
        Object cellObject = null;
        if (cell.size > 1) {
            cellObject = new Object((byte) 9, (byte) 9, new Vector3(cell.get(0)*tileSize+tileSize/2, cell.get(1)*tileSize+tileSize/2, 0f));
        }
        return cellObject;
    }

    /**
     * For collision avoidance with terrain along boid trajectory
     * @param boid that is doing the collision checking
     * @return Vector3 that is a velocity correctional change in acceleration
     */
    public static Vector3 act(Boid boid) {
        tmpVec.set(boid.getPosition());
        tmpVec2.set(boid.getVelocity());
//        tmpVec.add(tmpVec2.scl(stepsAhead));  //end position
        int mapX = (int)tmpVec.x/tileSize;
        int mapY = (int)tmpVec.y/tileSize;
        Array<Integer> cell = cellsInPath(tmpVec.x, tmpVec.y, tmpVec2.x, tmpVec2.y);
        tmpVec.set(0f,0f,0f);
        if(cell.size>0) {
            tmpVec.set(boid.getPosition());
            tmpVec.sub(new Vector3(cell.get(0) * tileSize + tileSize / 2, cell.get(1) * tileSize + tileSize / 2, 0f));
            tmpVec.nor();
            tmpVec.scl(MAX_AVOID_FORCE);
        }
//        for(int[] cell : cellList) {
//            tmpVec.set(0f,0f,0f);
//            //get tile info, check if grass
//            if (WorldManager.getTileInfoAt(cell[0], cell[1]).get("terrain") == 0) {
//                //terrain is grass -- passable, no need to do anything
//            }
//            //else if water - impassable
//            else {
//
//                tmpVec.set(boid.getPosition());
//                tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                tmpVec.nor();
//                tmpVec.scl(MAX_AVOID_FORCE);
//                // the tile is impassable,
//                // need to do collision avoidance with  the intersect of closest edge and velocity intersect
//                Vector2 intersect = new Vector2();
//                //TODO is this getting the correct line for nearest edge of cell?
//                if(mapX < cell[0]) {
//                    if(mapY == cell[1]  && Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell[0] * tileSize + tileSize, cell[1] * tileSize, cell[0] * tileSize + tileSize,
//                            cell[1] * tileSize + tileSize, intersect)) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                    else if (mapY < cell[1]) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(cell[0]*tileSize + tileSize, cell[1]*tileSize + tileSize, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                    else if(mapY > cell[1]) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(cell[0]*tileSize + tileSize, cell[1]*tileSize, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                }
//                else if(mapX > cell[0]) {
//                    if(mapY == cell[1] && Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell[0] * tileSize, cell[1] * tileSize, cell[0] * tileSize,
//                            cell[1] * tileSize + tileSize, intersect)) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                    else if (mapY < cell[1]) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(cell[0]*tileSize, cell[1]*tileSize + tileSize, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                    else if(mapY > cell[1]) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(cell[0]*tileSize, cell[1]*tileSize, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                } else if (mapX == cell[0]) {
//                    if (mapY < cell[1]&& Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell[0] * tileSize + tileSize, cell[1] * tileSize, cell[0] * tileSize + tileSize,
//                            cell[1] * tileSize + tileSize, intersect)) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                    else if(mapY > cell[1]&& Intersector.intersectLines(boid.getPosition().x, boid.getPosition().y,
//                            tmpVec.x, tmpVec.y, cell[0] * tileSize, cell[1] * tileSize, cell[0] * tileSize,
//                            cell[1] * tileSize + tileSize, intersect)) {
//                        tmpVec.set(boid.getPosition());
////                        tmpVec.sub(new Vector3(intersect.x, intersect.y, 0f));
//                        tmpVec.sub(new Vector3(cell[0]*tileSize+tileSize/2,cell[1]*tileSize+tileSize/2,0f));
//                        tmpVec.nor();
//                        tmpVec.scl(MAX_AVOID_FORCE);
//                    }
//                }
//            }
//        }


        //get tile info at position tile
        //ray cast to check when tile changes along velocity and retrieve tile info of new tiles


        return tmpVec;
    }

    /**
     *
     * @param x0  x coord of boid starting position
     * @param y0  y coord of boid starting position
     * @param dx_dt  x part of boid velocity
     * @param dy_dt  y part of boid velocity
     * @return  array of array of int coords of cells intersected by trajectory line
     */
    private static Array<Integer> cellsInPath(float x0, float y0, float dx_dt, float dy_dt) {
        Array<int[]> cellList = new Array<int[]>();
        Array<Integer> cell = new Array<Integer>();
        //calc change in x and y from boid start position to end position
        float dx = dx_dt * stepsAhead;
        float dy = dy_dt * stepsAhead;

        int x = (int)x0/tileSize;
        int y = (int)y0/tileSize;

        //position local to current cell
        float xLocal = x0 % tileSize;
        float yLocal = y0 % tileSize;

//        //change in x and y per unit time
//        float dx_dt = dx/stepsAhead;
//        float dy_dt = dy/stepsAhead;


//        //change in time per unit x and y axis
//        float dt_dx = 1f / dx;
//        float dt_dy = 1f / dy;
//        float t = 0;
//        int n = 1;
//        int x_inc, y_inc;
//        double t_next_vertical, t_next_horizontal;

        //each i will be a step in time, slowly increment the x and y and check if they cross cell boundaries
        //if so, add new cell to cellList
        for(int i = 0; i < stepsAhead; i++) {
            //increment x and y
            xLocal += dx_dt;
            yLocal += dy_dt;
            boolean stepped = false;

            if(xLocal < tileSize && xLocal > 0 ) {
                //no cell change
            }
            else if(xLocal > tileSize) {
                // has stepped right one cell
                x++;
                xLocal -= tileSize;
                stepped = true;
            }
            else if(xLocal < 0) {
                //  has stepped left one cell
                x--;
                xLocal = tileSize + xLocal;
                stepped = true;
            }

            if(yLocal < tileSize && yLocal > 0) {
                //no y axis cell change
            }
            else if(yLocal > tileSize) {
                // has stepped up one cell
                y++;
                yLocal -= tileSize;  //set new local position to location in new cell
                stepped = true;
            }
            else if(yLocal < 0) {
                //  has stepped down one cell
                y--;
                yLocal = tileSize + yLocal;
                stepped = true;
            }
//            if(stepped) {
//                if(WorldManager.getTileInfoAt(x, y).get("terrain") == 1) {
//                    int[] cell = {x, y};
//                    cellList.add(cell);
//                }
//            }
            if(stepped) {
                if(WorldManager.getTileInfoAt(x, y).get("terrain") == 1) {
                    cell.add(x);
                    cell.add(y);
                }
            }
        }
        return cell;
//        return cellList;
    }


    /**
     * Checks the Array of targets for mostTreatening collision with boid
     * @param boid  which is checking for collisions
     * @param targets   possible collision targets (just Entity's within sight range or all?)
     * @return  a correction Vector3 to avoid collision with most threatening
     */
    public static Vector3 act(Array<Entity> targets, Boid boid) {
        Array<Entity> entities = targets;  //
        Object cell = (Object) terrainCollision(boid);
        if(cell != null) {
            entities.add(cell);
        }
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
