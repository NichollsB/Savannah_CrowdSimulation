package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emily on 02/07/2014.
 */
public class BoidGrid {

    //private static int CELL_SIZE;

    /**
     * size of a cell.
     */
    public int cellSize;

    /**
     * width of grid in cells.
     */
    public int cellWidth = 0;

    /**
     * height of grid in cells.
     */
    public int cellHeight = 0;

    private Map<Boid, Vector2> cells = new HashMap<Boid, Vector2>();
    private Array<Boid>[][] grid;
    public  Vector3 inverseCellSize;

    /**
     * Constructor
     *
     * @param cell_size How "big" each cell is
     * @param w         How wide a space the grid is representing
     * @param h         How wide a space the grid is representing
     */
    public BoidGrid(int cell_size, int w, int h) {
        this.cellSize = cell_size;
        cellWidth = (w / cell_size) + 1;
        cellHeight = (h / cell_size) + 1;

        grid = new Array[cellWidth][cellHeight];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Array<Boid>();
            }
        }

        inverseCellSize = new Vector3(1/cell_size, 1/cell_size, 0f);
    }


    public void addBoid(Boid boid) {
        Vector2 cell_pos = new Vector2((int) boid.getPosition().x / cellSize, (int) boid.getPosition().y / cellSize);
        cells.put(boid, cell_pos);

        if (cell_pos.y > cellHeight-1 || cell_pos.x < 0 || cell_pos.y < 0 || cell_pos.x > cellWidth -1){
            System.out.print("derp");
        }
        grid[(int) cell_pos.x][(int) cell_pos.y].add(boid);
    }

    public void addBoid(Array<Boid> boids) {
        for (Boid boid : boids) {
            addBoid(boid);
        }
    }

    public Boid removeBoid(Boid boid) {
        Vector2 pos = cells.remove(boid);
        int cellX = (int) pos.x;
        int cellY = (int) pos.y;
        Vector2 cPos = new Vector2((int) boid.getPosition().x / cellSize, (int) boid.getPosition().y / cellSize);
        int curCellX = (int) cPos.x;
        int curCellY = (int) cPos.y;

        //sanity check
        if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if (grid[cellX][cellY].contains(boid, true)) {
                grid[cellX][cellY].removeValue(boid, true);
            }
            //TESTED, dont think this is needed.
            //to make sure it hasn't been put to its new pos already //seems a bit silly????
            if (grid[curCellX][curCellY].contains(boid, true)) {
                grid[curCellX][curCellY].removeValue(boid, true);
            }
        }
        return boid; //in case of chaining.
    }
    
  
    public void update(Boid boid) {
        Vector2 pos = cells.get(boid);

        Vector2 cPos = new Vector2((int) boid.getPosition().x / cellSize, (int) boid.getPosition().y / cellSize);

        int oldCellX = (int) pos.x;
        int oldCellY = (int) pos.y;
        int cellX = (int) cPos.x;
        int cellY = (int) cPos.y;


        if (cellX == oldCellX && cellY == oldCellY) {
            return;
        }

        //safety check for new cell location.
        if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if (!grid[cellX][cellY].contains(boid, true)) {


                grid[cellX][cellY].add(boid);
                grid[oldCellX][oldCellY].removeValue(boid, true);

                pos = cells.get(boid);
                if (pos == null) {
                    cells.put(boid, pos);
                } else {
                    cells.put(boid, cPos);
                }
            }
        }
    }


    private Array<Boid> nearby = new Array<Boid>();


    public Array<Boid> findNearby(int x, int y, int z) {
        return findNearby(new Vector3(x, y, z));
    }


    /**
     *  this will return boids from the cell the position is in and the cells adjacent to it.
     * @param pos location that you want to find boids from.
     * @return list of boids.
     */
    public Array<Boid> findNearby(Vector3 pos) {
        nearby.clear();

        int cellX = (int) pos.x / cellSize;
        int cellY = (int) pos.y / cellSize;


        if ( cellX < 0 || cellX >= grid.length ||
                cellY < 0 || cellY >= grid[0].length) {
            System.out.println("out of bounds  \t"   + cellX + "/" + grid.length + "\t" + cellY  + "/" + grid.length);
        }
        findBoidsInCell(nearby, cellX,        cellY);
        findBoidsInCell(nearby, cellX + 1,    cellY);
        findBoidsInCell(nearby, cellX + 1,    cellY + 1);
        findBoidsInCell(nearby, cellX,        cellY + 1);

        findBoidsInCell(nearby, cellX - 1,    cellY  + 1);
        findBoidsInCell(nearby, cellX - 1,    cellY);
        findBoidsInCell(nearby, cellX - 1,    cellY - 1);

        findBoidsInCell(nearby, cellX,        cellY - 1);
        findBoidsInCell(nearby, cellX + 1,    cellY - 1);

        return nearby;
    }


    public Array<Boid> findInSight(Boid boid) {
        nearby.clear();

        int startX = (int)boid.position.x;
        int startY = (int)boid.position.y;

        int sightRadLengthInCells = (int) boid.sightRadius / cellSize; //floored

        int startCellX = startX/cellSize - sightRadLengthInCells;
        int endCellX = startCellX + sightRadLengthInCells * 2;

        int startCellY = startY/cellSize - sightRadLengthInCells;
        int endCellY = startCellY + sightRadLengthInCells * 2;
        for(int i = startCellX; i < endCellX; i++) {
            for (int j = startCellY; j < endCellY; j++) {
                findBoidsInCellNew(nearby, i, j);
            }
//            for (int j = startCellY - sightRadLengthInCells; j < startCellY; j++) {
//                findBoidsInCell(nearby, i, j);
//            }
        }
//        for(int i = startCellX - sightRadLengthInCells; i < startCellX; i++) {
//            for (int j = startCellY; j < endCellY; j++) {
//                findBoidsInCell(nearby, i, j);
//            }
//            for (int j = startCellY - sightRadLengthInCells; j < startCellY; j++) {
//                findBoidsInCell(nearby, i, j);
//            }
//        }


        return nearby;
    }

    private Array<Boid> findBoidsInCellNew(Array<Boid> bnearby, int inCellX, int inCellY) {
        int cellX = inCellX, cellY = inCellY;

        //do wrap arounds
        if ( inCellX < 0) {
            cellX = cellX + grid.length;
        } else if ( cellX >= grid.length) {
            cellX = cellX - grid.length;
        }

        if(cellX > grid.length || cellX < 0) {
            System.out.println("cellx for boid grid find new cell is out");
        }
        if ( inCellY < 0) {
            cellY = cellY  + grid[cellX].length;
        } else if ( cellY >= grid[cellX].length) {
            cellY = cellY - grid[cellX].length;
        }


//        try {
            for (int i = 0; i < grid[cellX][cellY].size; i++) {
                //            if(!bnearby.contains(grid[cellX][cellY].get(i),true)) {
                bnearby.add(grid[cellX][cellY].get(i));
                //            }
            }
//        }catch (ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }

        return bnearby;
    }

    /**
     *
     * return all boids in the cell by adding them to the array given (bnearby) 
     *
     * @param bnearby array to add the boids to.
     * @param cellX cell index x
     * @param cellY cell index y
     * @return all boids in cell location.
     */
    public Array<Boid> findBoidsInCell(Array<Boid> bnearby, int cellX, int cellY) {


        //do wrap arounds
        if ( cellX < 0) {
            cellX = grid.length - 1;
        } else if ( cellX >= grid.length) {
            cellX = 0;
        }

        if ( cellY < 0) {
            cellY = grid[cellX].length - 1;
        } else if ( cellY >= grid[cellX].length) {
            cellY = 0;
        }

        //dont do wrap arounds
//        if ( cellX < 0 || cellX >= grid.length ||
//                cellY < 0 || cellY >= grid[0].length) {
//            return bnearby;
//        }

        for (int i = 0; i < grid[cellX][cellY].size; i++) {
//            if(!bnearby.contains(grid[cellX][cellY].get(i),true)) {
                bnearby.add(grid[cellX][cellY].get(i));
//            }
        }

        return bnearby;
    }


    public Boid findBoidAt(int screenX, int screenY) {

        int cellX = screenX / cellSize;
        int cellY = screenY / cellSize;

        Array<Boid> boids = new Array<Boid>();
        findBoidsInCell(boids, cellX, cellY);

        for (Boid boid : boids) {
            if(boid.bounds.contains(screenX,screenY)) {
                return boid;
            }
        }

        return null;
    }
}
