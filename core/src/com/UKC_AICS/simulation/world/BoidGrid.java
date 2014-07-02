package com.UKC_AICS.simulation.world;

import com.UKC_AICS.simulation.entity.*;
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
    private Array<Boid> [] [] grid;
    /**
     * Constructor
     *
     * @param cell_size     How "big" each cell is
     * @param w             How wide a space the grid is representing
     * @param h             How wide a space the grid is representing
     */
    public BoidGrid (int cell_size, int w, int h) {
        this.cellSize = cell_size;
        cellWidth = (w/cell_size)+1;
        cellHeight = (h/cell_size)+1;

        grid = new Array[cellWidth][cellHeight];

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0 ; j < grid[i].length; j++) {
                grid[i][j] = new Array<Boid>();
            }
        }
    }


    public void addBoid(Boid boid) {
        Vector2 cell_pos = new Vector2((int)boid.getPosition().x/cellSize,(int)boid.getPosition().y/cellSize);
        cells.put(boid, cell_pos);
        grid[(int)cell_pos.x][(int)cell_pos.y].add(boid);
    }
    public void addBoid(Array<Boid> boids) {
        for(Boid boid : boids) {
            addBoid(boid);
        }
    }

    public Boid removeBoid(Boid boid) {
        Vector2 pos = cells.remove(boid);
        int cellX = (int)pos.x;
        int cellY = (int)pos.y;
        Vector2 cPos = new Vector2((int)boid.getPosition().x/cellSize,(int)boid.getPosition().y/cellSize);
        int curCellX = (int)cPos.x;
        int curCellY = (int)cPos.y;

        //sanity check
        if( cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if(grid[cellX][cellY].contains(boid, true)) {
                grid[cellX][cellY].removeValue(boid,true);
            }
            //TODO: Test if this is needed.
            //to make sure it hasn't been put to its new pos already //seems a bit silly????
            if(grid[curCellX][curCellY].contains(boid, true)) {
                grid[curCellX][curCellY].removeValue(boid, true);
            }
        }
        return boid; //in case of chaining.
    }


    public void update(Boid boid) {
        Vector2 pos = cells.get(boid);

        Vector2 cPos = new Vector2((int)boid.getPosition().x/cellSize,(int)boid.getPosition().y/cellSize);

        if(pos == cPos) { return; }

        int oldCellX = (int)pos.x;
        int oldCellY = (int)pos.y;
        int cellX = (int)cPos.x;
        int cellY = (int)cPos.y;


        if (cellX == oldCellX && cellY == oldCellY) {
            return;
        }

        //safety check for new cell location.
        if( cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if(!grid[cellX][cellY].contains(boid, true)) {
                grid[cellX][cellY].add(boid);

                grid[oldCellX][oldCellY].removeValue(boid, true);
                pos = cells.get(boid);
                if(pos == null) {
                    cells.put(boid, cPos);
                } else {
                    cells.put(boid, cPos);
                }
            }
        }
    }


    private Array<Boid> nearby = new Array<Boid>();


    public Array<Boid> findNearby(int x, int y, int z) {
        return findNearby(new Vector3(x,y,z));
    }

    public Array<Boid> findNearby(Vector3 pos) {

        //TODO: wrap-around looking
        //TODO: account for potential variable "look" ranges. right now fixed to one cell distance around current one.
        nearby.clear();

        int cellX = (int) pos.x/cellSize;
        int cellY = (int) pos.y/cellSize;

        int _size = 0;


        if (cellX >= 0 && cellX < grid.length) {

            if(cellY >=0 && cellY < grid[cellX].length) {

                // middle
                _size = grid[cellX][cellY].size;
                for (int i=0; i< _size; i++){
                    nearby.add(grid[cellX][cellY].get(i));
                }
            }
            if (cellY+1 < grid[cellX].length) {
                _size = grid[cellX][cellY + 1].size;
                for (int i=0; i< _size; i++){
                    nearby.add(grid[cellX][cellY + 1].get(i));
                }
            }
            // bottom
            if (cellY-1 >= 0 && cellY-1 < grid[cellX].length) {
                _size = grid[cellX][cellY - 1].size;
                for (int i=0; i< _size; i++){
                    nearby.add(grid[cellX][cellY - 1].get(i));
                }
            }

            // right column
            if (cellX+1 < grid.length) {
                if (cellY >= 0 && cellY < grid[cellX + 1].length) {
                    // middle right
                    _size = grid[cellX + 1][cellY].size;
                    for (int i=0; i< _size; i++){
                        nearby.add(grid[cellX + 1][cellY].get(i));
                    }
                    if (cellY+1 < grid[cellX + 1].length) {
                        // top right
                        _size = grid[cellX + 1][cellY + 1].size;
                        for (int i=0; i< _size; i++){
                            nearby.add(grid[cellX + 1][cellY + 1].get(i));
                        }
                    }
                }
                if (cellY-1 >= 0 && cellY-1 < grid[cellX + 1].length) {
                    // bottom right
                    _size = grid[cellX + 1][cellY - 1].size;
                    for (int i=0; i< _size; i++){
                        nearby.add(grid[cellX + 1][cellY - 1].get(i));
                    }
                }
            }
        }

        // left column
        if (cellX-1 >= 0 && cellX-1 < grid.length) {
            if (cellY >= 0 && cellY < grid[cellX - 1].length) {
                // center left
                _size = grid[cellX - 1][cellY].size;
                for (int i=0; i< _size; i++){
                    nearby.add(grid[cellX - 1][cellY].get(i));
                }

                // top left
                if (cellY+1 < grid[cellX - 1].length) {
                    _size = grid[cellX - 1][cellY + 1].size;
                    for (int i=0; i< _size; i++){
                        nearby.add(grid[cellX - 1][cellY + 1].get(i));
                    }
                }
            }

            if (cellY-1 >= 0 && cellY-1 < grid[cellX - 1].length) {
                // bottom left
                _size = grid[cellX - 1][cellY - 1].size;
                for (int i=0; i< _size; i++){
                    nearby.add(grid[cellX - 1][cellY - 1].get(i));
                }
            }
        }
        return nearby;
    }

    public Array<Boid> findBoidsInCell(int cellX, int cellY) {
        
    }
}
