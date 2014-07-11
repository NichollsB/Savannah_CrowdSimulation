package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/07/2014.
 */
public class ObjectGrid {

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

    private Map<Entity, Vector2> cells = new HashMap<Entity, Vector2>();
    private Array<Entity>[][] grid;
    public Vector3 inverseCellSize;

    /**
     * Constructor
     *
     * @param cell_size How "big" each cell is
     * @param w         How wide a space the grid is representing
     * @param h         How wide a space the grid is representing
     */
    public ObjectGrid(int cell_size, int w, int h) {
        this.cellSize = cell_size;
        cellWidth = (w / cell_size) + 1;
        cellHeight = (h / cell_size) + 1;

        grid = new Array[cellWidth][cellHeight];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Array<Entity>();
            }
        }

        inverseCellSize = new Vector3(1/cell_size, 1/cell_size, 0f);
    }

    public void addObject(Entity object) {
        Vector2 cell_pos = new Vector2((int) object.getPosition().x / cellSize, (int) object.getPosition().y / cellSize);
        cells.put(object, cell_pos);
        if (cell_pos.y == cellHeight-1 || cell_pos.x == 0 || cell_pos.y ==0 || cell_pos.x == cellWidth -1){
            System.out.print("derp");
        }
        grid[(int) cell_pos.x][(int) cell_pos.y].add(object);
    }

    public void addBoid(Array<Entity> objects) {
        for (Entity object : objects) {
            addObject(object);
        }
    }

    public Entity removeObject(Entity object) {
        Vector2 pos = cells.remove(object);
        int cellX = (int) pos.x;
        int cellY = (int) pos.y;
        Vector2 cPos = new Vector2((int) object.getPosition().x / cellSize, (int) object.getPosition().y / cellSize);
        int curCellX = (int) cPos.x;
        int curCellY = (int) cPos.y;

        //sanity check
        if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if (grid[cellX][cellY].contains(object, true)) {
                grid[cellX][cellY].removeValue(object, true);
            }
            //TESTED, dont think this is needed.
            //to make sure it hasn't been put to its new pos already //seems a bit silly????
//            if (grid[curCellX][curCellY].contains(boid, true)) {
//                grid[curCellX][curCellY].removeValue(boid, true);
//            }
        }
        return object; //in case of chaining.
    }


    public void update(Entity object) {
        Vector2 pos = cells.get(object);

        Vector2 cPos = new Vector2((int) object.getPosition().x / cellSize, (int) object.getPosition().y / cellSize);

        int oldCellX = (int) pos.x;
        int oldCellY = (int) pos.y;
        int cellX = (int) cPos.x;
        int cellY = (int) cPos.y;


        if (cellX == oldCellX && cellY == oldCellY) {
            return;
        }

        //safety check for new cell location.
        if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
            if (!grid[cellX][cellY].contains(object, true)) {


                grid[cellX][cellY].add(object);
                grid[oldCellX][oldCellY].removeValue(object, true);

                pos = cells.get(object);
                if (pos == null) {
                    cells.put(object, pos);
                } else {
                    cells.put(object, cPos);
                }
            }
        }
    }
}
