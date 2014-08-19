package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Emily on 30/06/2014.
 */
public class QuadTree {

    private static final int MAX_OBJECTS = 15;
    private static final int MAX_LEVELS = 4;


    private int level;
    private Array<Entity> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;


    /**
     *
     * Create a quad tree
     *
     * @param level number of "levels" of splitting allowed.
     * @param rectangle dimensions that this quadtree covers
     */
    public QuadTree(int level, Rectangle rectangle) {
        this.level = level;
        bounds = rectangle;
        nodes = new QuadTree[4];
        objects = new Array<Entity>();
    }

    /**
     * recursively clears tree of objects and subtrees.
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if ( nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }


    /**
     * Splits the node into 4 subnodes
     */
    public void split() {
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        //create the smaller subtrees.
        nodes[0] = new QuadTree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    /**
     * tries to insert the object to this node. if node contains too many objects and
     * its not at max levels it splits the quadtree.
     *
     * @param obj - the item to add.
     */
    public void insert(Entity obj) {

        if(nodes[0] != null) {
            int index = getIndex(obj);

            if (index != -1) {
                nodes[index].insert(obj);
                return;
            }
        }

        objects.add(obj);

        if( (objects.size > MAX_OBJECTS) && (level < MAX_LEVELS) ) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;

            int index;
            //go through and move the objects that will fit in the smaller nodes
            while(i < objects.size) {
                index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.removeIndex(i));
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * helper method to decide where in the quadtree the object can fit.
     * @param obj the object we are finding a home for.
     * @return returns the index of the node it can fit in, -1 means the object cannot fit completely within a child node and is part of the parent.
     */
    public int getIndex(Entity obj) {
        int index = -1;

        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

//        newOrigin.x += halfDimension.x * ((i & 4) == 4? 0.5f : -0.5f);
//        newOrigin.y += halfDimension.y * ((i & 2) == 2? 0.5f : -0.5f);

        //object is above the horizontal point.
        boolean top = (obj.getPosition().y > verticalMidpoint);
        //&& obj.getPosition().y + obj.getHeight() < verticalMidpoint); //for when we have size

        if (obj.getPosition().x > horizontalMidpoint) {
            if(top) {
                index = 1;
            } else {
                index = 2;
            }
        } else {
            if(top) {
                index = 0;
            } else {
                index = 3;
            }
        }

        return index;
    }
    /**
     * helper method to decide where in the quadtree the object can fit.
     * @param rect the object we are finding a home for.
     * @return returns the index of the node it can fit in, -1 means the object cannot fit completely within a child node and is part of the parent.
     */
    public int getIndex(Rectangle rect) {
        int index = -1;

        if(bounds.contains(rect)) {
            return index;
        } else {
            double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
            double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

            //        newOrigin.x += halfDimension.x * ((i & 4) == 4? 0.5f : -0.5f);
            //        newOrigin.y += halfDimension.y * ((i & 2) == 2? 0.5f : -0.5f);

            //object can completely fit in top quadrants.
            boolean top = (rect.getY() > verticalMidpoint) && ((rect.getY() + rect.getHeight()) < verticalMidpoint); //for when we have size

            if (rect.getX() < horizontalMidpoint && rect.getX() + rect.getWidth() < horizontalMidpoint) {
                //left
                if (top) {
                    //top-left
                    index = 0;
                } else {
                    //bottom-left
                    index = 1;
                }
            } else {
                //right
                if (top) {
                    //top-right
                    index = 3;
                } else {
                    //bottom-right
                    index = 2;
                }
            }
            return index;
        }
    }


    /**
     * helper method to decide where in the quadtree the object can fit.
     * @param point the containing point we are finding a location for in the tree
     * @return returns the index of the node it can fit in, -1 means the object cannot fit completely within a child node and is part of the parent.
     */
    public int getIndex(Vector2 point) {
        int index = -1;

//        if(bounds.contains(point)) {
//            return index;
//        } else {
            double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
            double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

            //        newOrigin.x += halfDimension.x * ((i & 4) == 4? 0.5f : -0.5f);
            //        newOrigin.y += halfDimension.y * ((i & 2) == 2? 0.5f : -0.5f);

            //object can completely fit in top quadrants.
            boolean top = (point.y > verticalMidpoint);

            if (point.x < horizontalMidpoint) {
                //left
                if (top) {
                    //top-left
                    index = 0;
                } else {
                    //bottom-left
                    index = 1;
                }
            } else {
                //right
                if (top) {
                    //top-right
                    index = 3;
                } else {
                    //bottom-right
                    index = 2;
                }
            }
            return index;
//        }
    }


    /**
     * filters all objects (boids and worldObjects) for "possible" collisions and returns them.
     * @param point object that is looking for collision candidates.
     * @return list of objects that are potential colliders
     */
    public Array<Entity> retrieveColliders(Array<Entity> returnObjects, Vector2 point) {
        int index = getIndex(point);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveColliders(returnObjects, point);
        }

        Entity obj;
        for (int i = 0; i < objects.size; i++) {
            obj = objects.get(i);
            if(obj.getType() == 1 && !returnObjects.contains( obj, true) ){
                returnObjects.add(obj);
            }
        }
        return returnObjects;
    }

    /**
     *  returns a list of objects from the quadtree division area.
     * @param returnObjects
     * @param point
     * @return
     */
    public Array<Entity> retrieveObjects(Array<Entity> returnObjects, Vector2 point) {
        int index = getIndex(point);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveColliders(returnObjects, point);
        }

        Entity obj;
        for (int i = 0; i < objects.size; i++) {
            obj = objects.get(i);
            if(obj.getType() != 1 && !returnObjects.contains( obj, true) ){
                returnObjects.add(obj);
            }
        }
        return returnObjects;
    }


    public Array<Boid> retrieveBoids(Array<Boid>returnObjects,  Entity obj) {

        return returnObjects;
    }


    /**
     *
     * casts the contents of the quadtree into boids.
     *
     * @param returnObjects
     * @param rect
     * @return
     */
    public Array<Boid> retrieveBoids(Array<Boid>returnObjects,  Rectangle rect) {
        int index = getIndex(rect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveBoids(returnObjects, rect);
        }

        Entity obj;
        for (int i = 0; i < objects.size; i++) {
            obj = objects.get(i);
            if( obj.getType() == 1  ){
                returnObjects.add((Boid)obj);
            }
        }
        return returnObjects;
    }

    public Array<Boid> retrieveBoids(Array<Boid>returnObjects,  Vector2 point) {

        int index = getIndex(point);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveBoids(returnObjects, point);
        }

        Entity obj;
        for (int i = 0; i < objects.size; i++) {
            obj = objects.get(i);
            if(obj.getType() == 1 && !returnObjects.contains( (Boid)obj, true) ){
                returnObjects.add((Boid)obj);
            }
        }
        return returnObjects;
    }

    public Array<Boid> retrieveBoidsInRadius( Vector3 pos, float range ) {
        Array<Boid> returnObjects = new Array<Boid>();
        Rectangle rect = new Rectangle(pos.x-range/2,pos.y-range/2, range, range);

        int index = getIndex(rect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveBoids(returnObjects, rect);
        } else if (index == -1 && nodes[0] != null) {

                //find boids that the centre of the boids range sits in.
                retrieveBoids(returnObjects, rect.getCenter(new Vector2()));

                //find boids for each "corner" of the rectangle range.
                retrieveBoids(returnObjects, new Vector2(rect.x, rect.y));
                retrieveBoids(returnObjects, new Vector2(rect.x + rect.width, rect.y));
                retrieveBoids(returnObjects, new Vector2(rect.x, rect.y + rect.height));
                retrieveBoids(returnObjects, new Vector2(rect.x + rect.width, rect.y + rect.height));


        }

        for(Entity obj : objects) {
            if(obj.getType() == 1){
                returnObjects.add((Boid)obj);
            }
        }

        return returnObjects;
    }
}
