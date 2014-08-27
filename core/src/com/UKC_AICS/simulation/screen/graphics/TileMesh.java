package com.UKC_AICS.simulation.screen.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Arrays;


/**
 * Class for creating and governing a Mesh representing the simulation environment.
 * Each mesh is a graphical representation of a layer from the {@link com.UKC_AICS.simulation.world.LandMap#information_layers information layers}.
 * The mesh is formed from a number of rectangular quad style triangle pairs, each representing an element in this byte[][]
 * layer array. The values contained in the array used to vary the each mesh quads opacity.
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public class TileMesh {

    /**
     *
     */
	 private byte[][] grid;//contains values of grid
    //contains x,y position of each cell adjaxcent to corresponding corner
    //Key corresponds to corner in gridCorners, int[][] array holds x,y positions of cells in the grid array
    private ObjectMap<Integer, int[][]> cornersCells = new ObjectMap<Integer, int[][]>();

    private float[] verts;

    private int vertexComponents;
    private int idx = 0;


    private static final Color defaultCol = Color.WHITE;
    public Color color = defaultCol;
    public Texture texture;

    private static final int cellSize = 16;
    private static final int vertComponents = 3;

    private boolean updateMesh = false;

    public Mesh mesh;

    private int[][][] cornersVerts;
    private boolean usesTexture = false;

    private int textureComponents = 2;

    private byte updateTally[][];

    private float texIncrementX;
    private float texIncrementY;
    private int texCellsX;
    private int texCellsY;
    private float maxTexCellsX;
    private float maxTexCellsY;
    boolean repeatTexX;

    //ALPHA BUFFERS - Min/Max values
    private float lowerAlphaCutoff = 0;
    private float upperAlphaCutoff = 1;
    private float upperAlpha = 1;
    private float lowerAlpha = 0;
    private int gridRange = 100;
    private boolean created = false;

    /**
     * Initialise the class
     * @param lowerCutoff The lower limit of the values used to vary the mesh opacity. Values below this limit will result
     *                    in the opacity of a corner being set to the minimum alpha value.
     * @param upperCutoff The upper limit of the values used to vary the mesh opacity. Values above this limit will result
     *                    in the opacity of a corner being set to the minimum alpha value.
     * @param alphaMin The minimum alpha value that may be assigned. Values beneath the lowerCutoff will
     *                 be set to this value
     * @param alphaMax The maximum alpha value that may be assigned. Values above the upperCutoff will
     *                 be set to this value
     * @param valueRange The range of values that may be assigned to the mesh
     */
     public TileMesh(float lowerCutoff, float upperCutoff, float alphaMin, float alphaMax, int valueRange){
        this.lowerAlphaCutoff = lowerCutoff;
        this.upperAlphaCutoff = upperCutoff;
        this.lowerAlpha = alphaMin;
        this.upperAlpha = alphaMax;
        this.gridRange = valueRange;
    }


    /**
     * Default constructor for creating the class with default values
     */
    public TileMesh(){}


    /**
     * Initialises a mesh with a single triangle pair at given point, and with given dimensions, and a specified color
     * @param startX Mesh x coordinate
     * @param startY Mesh y coordinate
     * @param width Mesh width
     * @param height Mesh height
     * @param c Mesh color
     */
    public void createMesh(int startX, int startY, int width, int height, Color c){

        mesh = new Mesh(true, 6, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Color, 4, "a_color"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));
        vertexComponents = 2 + 4 + 2;
        verts = new float[vertexComponents*6];
        color = c;
        cornersVerts = new int[2][2][6];
        for(int[][] array : cornersVerts){
            for(int[]inner : array){
                Arrays.fill(inner, -1);
            }
        }
        drawTrianglePair(0, 0, startX, startY, width, height, 0, 1, 1, -1, color);
    }

    /**
     * Create a mesh representing a given grid[][] of values, at a given position, and with a specified dimensions
     * for each cell in the mesh.
     * @param grid Array of values this mesh will represent
     * @param startX x position
     * @param startY y position
     * @param cellWidth width of each cell in the mesh
     * @param cellHeight height of each cell in the mesh
     * @param c Color of the mesh
     */
    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight, Color c){
        usesTexture = true;
        this.color = c;
        this.texIncrementY = 1;
        this.texIncrementX = 1;
        createMesh(grid, startX, startY, cellWidth, cellHeight);
    }

    /**
     * Create a mesh representing a given grid[][] of values, at a given position, and with a specified dimensions
     * for each cell in the mesh. With a specified texture obtained from a AtlasRegion
     * @param grid Array of values this mesh will represent
     * @param startX x position
     * @param startY y position
     * @param cellWidth width of each cell in the mesh
     * @param cellHeight height of each cell in the mesh
     * @param atlasRegion AtlasRegion to apply to this mesh
     * @param allowStretch Whether or not the texture coordinates of this mesh should be set such that the meshes
     *                     texture may stretch.
     * @param texCellsX Number of times that the texture may be repeated on the x axis
     * @param texCellsY Number of times that the texture may be repeated on the y axis
     */
    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight,
            AtlasRegion atlasRegion, boolean allowStretch, int texCellsX, int texCellsY){
        texture = atlasRegion.getTexture();
        createMesh(grid, startX, startY, cellWidth, cellHeight, atlasRegion.originalWidth, atlasRegion.originalHeight,
                allowStretch, texCellsX, texCellsY);
    }

    /**
     * Create a mesh representing a given grid[][] of values, at a given position, and with a specified dimensions
     * for each cell in the mesh. With texture coordinates applied from a given Texture.
     * @param grid Array of values this mesh will represent
     * @param startX x position
     * @param startY y position
     * @param cellWidth width of each cell in the mesh
     * @param cellHeight height of each cell in the mesh
     * @param texture Texture to apply to this mesh
     * @param allowStretch Whether or not the texture coordinates of this mesh should be set such that the meshes
     *                     texture may stretch.
     * @param texCellsX Number of times that the texture may be repeated on the x axis
     * @param texCellsY Number of times that the texture may be repeated on the y axis
     * @param color Color to apply to the mesh
     */
    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight,
            Texture texture, boolean allowStretch, int texCellsX, int texCellsY, Color color){
        this.color = color;
        this.texture = texture;
        createMesh(grid, startX, startY, cellWidth, cellHeight, texture.getWidth(), texture.getHeight(),
                allowStretch, texCellsX, texCellsY);
    }

    /**
     * Create a mesh representing a given grid[][] of values, at a given position, and with a specified dimensions
     * for each cell in the mesh.
     * @param grid Array of balues this mesh will represent
     * @param startX x position
     * @param startY y position
     * @param cellWidth width of each cell in the mesh
     * @param cellHeight height of each cell in the mesh
     * @param allowStretch Whether or not the texture coordinates of this mesh should be set such that the meshes
     *                     texture may stretch.
     * @param texCellsX Number of times that the texture may be repeated on the x axis
     * @param texCellsY Number of times that the texture may be repeated on the y axis
     */
    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight,
            int texWidth, int texHeight, boolean allowStretch, int texCellsX, int texCellsY){

        if(cellWidth <= 0) cellWidth = cellSize;
        if(cellHeight <= 0) cellHeight = cellSize;
        /*----------------------------------------------------------------------
        Calculates the required texture increment value (texIncrementX/Y). This
        is the amount that the texture coordinates are incremented between adjacent
        vertices in order to satisfy the required texturing requirements.

        texCellsX/Y specify the number of texture cells the user wants in the
        respective direction.

        If allowStretch is true, the textures are allowed to be adjusted from
        native resolution. In this case, the arguments texCellsX/Y are obeyed
        and the required number of complete texture cells will be drawn (usually
        scaled to fit).

        If allowStretch is true AND the number of texCellsX/Y is equal to the number
        of grid cells in each direction then one texture cell (usually scaled)
        is drawn per quad i.e. each square grid will have an identically sized
        tecture cell drawn in its place.

        If allowStretch is false then the texCellsX/Y are redundant and the
        meshGrid is tiled with as many native resolution texture cells as possible.
        In this case, some texture cells will be cut of at the edges if they do
        not fit the grid. No scaling of the texture takes place in this case.
        ----------------------------------------------------------------------*/
        usesTexture = true;
        this.texCellsX = texCellsX;
        this.texCellsY = texCellsY;

        if (allowStretch == true){
            //Divide the number of whole textures we want by the length of the grid gives the number of texture units per grid point.
            this.texIncrementX = (float)texCellsX / (float)grid[0].length;
            this.texIncrementY = (float)texCellsY / (float)grid.length;
            createMesh(grid, startX, startY, cellWidth, cellHeight);
        }
        else {
            //Divide pixel length of grid by pixel length of texture to give number of native textures that will fit on the grid.
            maxTexCellsX = (float)(grid[0].length*cellWidth) / (float)(texWidth);
            maxTexCellsY = (float)(grid.length*cellHeight) / (float)(texHeight);
            //Divide the max number of native texture cells by the number of grid points to get the number of texture units per grid point.
            this.texIncrementX = maxTexCellsX / (float)grid[0].length;
            this.texIncrementY = maxTexCellsY / (float)grid.length;
            createMesh(grid, startX, startY, cellWidth, cellHeight);

        }

    }

    /**
     * Creates a deep copy of the meshes byte[][] array of values. For comparison and updating.
     * @param grid byte[][] arrays to make copy of
     * @return byte[][] copy
     */
    private byte[][] deepCopyArray(byte[][] grid){
        byte array[][] = new byte[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[i].length; j++){
                array[i][j] = grid[i][j];
            }
        }
        return array;
    }

    /**
     * Create the mesh given the initialised parameters
     * @param grid Array of balues this mesh will represent
     * @param startX x position
     * @param startY y position
     * @param cellWidth width of each cell in the mesh
     * @param cellHeight height of each cell in the mesh
     */
    private void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight){
        if(cellWidth <= 0) cellWidth = cellSize;
        if(cellHeight <= 0) cellHeight = cellSize;
//	        this.grid = grid.clone();
        this.grid = (deepCopyArray(grid));

        // 0---0---0---0
        // | s | s | / |
        // *---*---*---0   * = bottom left corner of square, used to refer to a square.
        // | s | s | s |
        // *---*---*---0   s = squares that can be drawn on the grid.
        // | s | s | s |
        // *---*---*---0   0 = points not included in grid i.e. number of grid points = number of squares


        // number of grid points in the x and y directions.
        int gridSizeX = grid[0].length;
        int gridSizeY = grid.length;

        //number of squares that can be drawn on that grid in the x and y directions
        int squaresX = gridSizeX;
        int squaresY = gridSizeY;
        int maxSquares = squaresX * squaresY;

        //number of corners that are stored for grid
        int cornersX = gridSizeX+1;
        int cornersY = gridSizeY+1;

        //Initialise the cornersVerts ready to be added to
        cornersVerts = new int[cornersX][cornersY][6];
        for(int[][] array : cornersVerts){
            for(int[]inner : array){
                Arrays.fill(inner, -1);
            }
        }
        updateTally = new byte[gridSizeY+1][gridSizeX+1];
        for(byte[] array : updateTally){
            Arrays.fill(array, (byte)0);
        }

        //number of triangles these squares can be split into
        int maxTriangles = maxSquares * 2;

        //each triangle has 3 vertices so number of vertices in the mesh is:
        int maxVerts = maxTriangles * 3;

        //How many components are associated with each vertex attribute?
        //Position Attribute (x,y)
        int positionComponents = 2;
        //colour attribute (rgba)
        int colorComponents = 4;

        //float texIncrementX=0, texIncrementY=0;
        if(usesTexture){
            mesh = new Mesh(true, maxVerts, 0,
                        new VertexAttribute(VertexAttributes.Usage.Position, positionComponents, "a_position"),
                        new VertexAttribute(VertexAttributes.Usage.Color, colorComponents, "a_color"),
                        new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, textureComponents, "a_texCoords"));
            //total number of components for each vertex
            vertexComponents = positionComponents + colorComponents + textureComponents;

        }
        else{
            mesh = new Mesh(true, maxVerts, 0,
                            new VertexAttribute(VertexAttributes.Usage.Position, positionComponents, "a_position"),
                            new VertexAttribute(VertexAttributes.Usage.Color, colorComponents, "a_color"));
            //total number of components for each vertex
            vertexComponents = positionComponents + colorComponents;

        }

        if(color.a > upperAlpha)
            color.a = upperAlpha;
        if(color.a < lowerAlphaCutoff)
            color.a = lowerAlphaCutoff;
        //create an array to store the vertices data:
        // [x1, y1, r1, g1, b1, a1, x2, y2 ...  xn, yn, rn, gn, bn, an]
        verts = new float[maxVerts * vertexComponents];
//	        //Now we need to populate the verts array with the vertices 
        // (and therefore the vertex components)

        float meshHeight = cellHeight*grid.length; //meshHeight is irrelevant, the texCoord will always be 1 at "meshHeight"

        //We loop through each point on the grid.
        //Each point is considered the bottom left point of one of the
        //sub-squares. Each square is made up of two triangles. From the bottom
        //left point we calculate the vertices for two triangles. These vertices
        //are added to the mesh vertex array.
        int y,x, xpos, ypos;
        int texYCount = 0, texXCount = 0;
        float texXPos, texYPos;

        for(x = 0; x < gridSizeX; x++){
            xpos = x*cellWidth + startX;
            for(y = 0; y < gridSizeY; y++){
                ypos = y*cellHeight + startY;
//                    System.out.println("Drawing cell " + x + " " + y);
                if(!usesTexture) {
//                        if(!flipped)
                        drawTrianglePair(x, y, xpos, ypos, cellWidth, cellHeight, color);
                }
                else{
                    texYPos = y*texIncrementY ;
                    texXPos = x*texIncrementX ;
                    drawTrianglePair(x, y, xpos, ypos, cellWidth, cellHeight, texXPos, texCellsY-texYPos, texIncrementX, -texIncrementY, color);
                    texXCount++;

                }

            }
            if(usesTexture){
                 texYCount++;
                 texXCount = 0;
            }
        }

        //Give the list of vertices to the mesh so it can draw
        compareAndUpdate(grid, true);
        mesh.setVertices(verts);
//            update(grid, true);
        created = true;

    }

    /////////////////////////////////////////////////////////////////////
    // ADJUSTED DRAW METHOD///////////////////////////
    /////////////////////////////////////////
    private int vertCount = 0;

    /**
     * Draw a triangle pair with for the given byte[][] array element (cell), at a specified x,y coordinate.
     * Includes texture coordinates for meshes with a texture applied.
     *
     * @param cellX The x coordinate of the cell this triangle pair represents
     * @param cellY The y coordinate of the cell this triangle pair represents
     * @param x The x position of the leftmost side of this rectangular triangle pair
     * @param y The y position of the bottommost side of this rectangular triangle pair
     * @param width The width of this rectangular triangle pair. Used to find the rightmost coordinates of this cell
     * @param height The height of this rectangular triangle pair. Used to find the topmost coordinates of this cell
     * @param texX The texture x coordinate of the leftmost side of this rectangular triangle pair
     * @param texY The texture y coordinate of the leftmost side of this rectangular triangle pair
     * @param texStepX The x increment of the texture coordinates, for finding the rightmost texture coordinate of this cell
     * @param texStepY The y increment of the texture coordinates, for finding the topmost texture coordinate of this cell
     * @param color The Color to apply to the triangle pair
     */
    private void drawTrianglePair(int cellX, int cellY, float x, float y, float width, float height, float texX, float texY, float texStepX, float texStepY, Color color) {
        //Index of the initial alpha component
        int aInitial = 5;
        int aIndex;
        int n =vertCount;
        //we don't want to hit any index out of bounds exception...
        //so we need to flush the batch if we can't store any more verts
        //if (idx==verts.length)
            //render();
        //now we push the vertex data into our array
        //we are assuming (0, 0) is lower left, and Y is up
        //bottom left vertex
        verts[idx++] = x; //Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color.r; //Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX;
        verts[idx++] = texY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX, cellY, aIndex);

        //topleft vertex
        verts[idx++] = x; //Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color.r; //Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX;
        verts[idx++] = texY+texStepY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX, cellY+1, aIndex);

        //top right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color.r;	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX + texStepX;
        verts[idx++] = texY + texStepY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX+1, cellY+1, aIndex);

        //bottom left vertex
        verts[idx++] = x;	//Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color.r;	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX;
        verts[idx++] = texY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX, cellY, aIndex);

        //top right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color.r;	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX + texStepX;
        verts[idx++] = texY + texStepY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX+1, cellY+1, aIndex);

        //bottom right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color.r;	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        verts[idx++] = texX + texStepX;
        verts[idx++] = texY;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX+1, cellY, aIndex);

        vertCount = n;
    }

    /**
     * Draw a triangle pair with for the given byte[][] array element (cell), at a specified x,y coordinate.
     * @param cellX The x coordinate of the cell this triangle pair represents
     * @param cellY The y coordinate of the cell this triangle pair represents
     * @param x The x position of the leftmost side of this rectangular triangle pair
     * @param y The y position of the bottommost side of this rectangular triangle pair
     * @param width The width of this rectangular triangle pair. Used to find the rightmost coordinates of this cell
     * @param height The height of this rectangular triangle pair. Used to find the topmost coordinates of this cell
     * @param color1 The Color to apply to the triangle pair
     */
    private void drawTrianglePair(int cellX, int cellY, float x, float y, float width, float height, Color color1) {
        //Index of the initial alpha component
        int aInitial = 5;
        int aIndex;
        int n =vertCount;
        //we don't want to hit any index out of bounds exception...
        //so we need to flush the batch if we can't store any more verts
        //now we push the vertex data into our array
        //we are assuming (0, 0) is lower left, and Y is up
        //bottom left vertex
        verts[idx++] = x; //Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color1.r; //Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX, cellY, aIndex);

        //top left vertex
        verts[idx++] = x; //Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color1.r; //Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
        addCornersVert(cellX, cellY+1, aIndex);

        //top right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color1.r;	//Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
//	        cornersVerts[cellX+1][cellY+1][n-1] = aIndex;
        addCornersVert(cellX+1, cellY+1, aIndex);

        //bottom left vertex
        verts[idx++] = x;	//Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color1.r;	//Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
//	        cornersVerts[cellX][cellY][n-1] = aIndex;
        addCornersVert(cellX, cellY, aIndex);

        //top right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color1.r;	//Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
//	        cornersVerts[cellX][cellY][n-1] = aIndex;
        addCornersVert(cellX+1, cellY+1, aIndex);

        //bottom right vertex
        verts[idx++] = x + width;	//Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color1.r;	//Color(r, g, b, a)
        verts[idx++] = color1.g;
        verts[idx++] = color1.b;
        verts[idx++] = color1.a;
        n++;
        aIndex = aInitial + (n-1)*vertexComponents;
//	        cornersVerts[cellX+1][cellY][n-1] = aIndex;
        addCornersVert(cellX+1, cellY, aIndex);

        vertCount = n;
    }

    /**
     * Add the index of a particular vertices' alpha component to a given element in the array of corners
     * {@link #cornersVerts cornerVerts}. Used in updating the alpha values of the mesh.
     * @param cornerX The x index of the corner
     * @param cornerY The y index of the corner
     * @param aIndex The index of the alpha component of a vertices - i.e the offset of the
     *               component element in the {@link #verts verts array}.
     */
    private void addCornersVert(int cornerX, int cornerY, int aIndex){
        int verts[] = cornersVerts[cornerX][cornerY];
        for(int i = 0; i < 6; i++){
            if(verts[i] == aIndex) return; //If the vert index is already in this corner, ignore
            if(verts[i] == -1){
                //if we find a spot to add the index to this corner, do so and end the method
                verts[i] = aIndex;
                return;
            }
        }
    }

    /**
     * Call the update of the mesh with the given grid of values that should be compared. Only
     * values that have changed will be updated. Sets the vertices in the mesh with the vert array upon update.
     * @param grid Array of values to compare and update
     * @return True if the mesh was updated
     */
    public boolean update(byte[][] grid){
        return update(grid, false);
    }

    /**
     * Call the update of the mesh with the given grid of values that should be compared. Only
     * values that have changed will be updated. Sets the vertices in the mesh with the vert array upon update.
     * @param grid Array of values to compare and update
     * @param forceUpdate Optionally may set to true to force the array to fully update
     * @return True if the mesh was updated
     */
    public boolean update(byte[][] grid, boolean forceUpdate){
//	        System.out.println("update Start");
        if(created){

            compareAndUpdate(grid);
            if(updateMesh){
//	            System.out.println("updateMesh is true");
                //mesh.setVertices(vertsComps);
//	            for(int i = 0; i < verts.length; i++){
//	                if(verts[i] == 0)
//	                    //System.out.println("VERT Color " + verts[i]);
//	                    continue;
//	            }

                mesh.setVertices(verts);
                updateMesh = false;
                this.grid = deepCopyArray(grid);
            }
            return true;
        }
        return false;
//	        System.out.println("update finish");
    }
    ////////////////////////////////////////////////////////
    // NEW UPDATE METHOD///////////////////////////
    /////////////////////////////////

    /**
     * Compares the passed in the byte[][] array values and updates the {@link #verts vewrts} array
     * if a change is detected.
     * @param newGrid Array of values to compare
     */
    public void compareAndUpdate(byte[][] newGrid){
        compareAndUpdate(newGrid, false);
    }

    /**
     * Compares the passed in the byte[][] array values and updates the {@link #verts vewrts} array
     * if a change is detected. May force the verts array to update.
     *
     * Cycles through the passed in array and if it detects a change in value updates each corner
     * of the associated mesh area (quad style triangle pair/cell). Assumes that each index of the passed
     * in array corresponds to the bottom left corner of the associated cell, for convenience, and updates that
     * corner, the corner to the right, above, and above and to the right.
     * Will update each corner only once.
     * @param newGrid Array of values to compare
     * @param forceUpdate True to force the array to update
     */
    public void compareAndUpdate(byte[][] newGrid, boolean forceUpdate){
//            System.out.println("Comparing");
        boolean updated = false;
        int gridSizeX = this.grid[0].length;
        int gridSizeY = this.grid.length;
        int x, y;
        int n; //n counts the number of vertices we are updating and is used to
               //refer to the correct index of relevant alpha component in the
               //vert array.

        int alpha; //stores the new alpha value

        //this is the index of the very first alpha component in the vert array
        //i.e. vert[5] holds the alpha component of the first vertex of the mesh.
        int aInitial = 5;

        int aIndex;
        n = 0;
        int updateCornerCount = 0;
        for(y = 0; y < gridSizeY; y++){
            if(Arrays.equals(this.grid[y], newGrid[y]) && !forceUpdate) continue;
            for(x = 0; x < gridSizeX; x++){

                n++;
                //System.out.println("Start updating cell x " + x + " y " + y);

                //If this grid cell is not the same as the old grid cell, update
                if ((this.grid[x][y] != newGrid[x][y] )|| forceUpdate){
//                        System.out.println("Difference Found!");
///////////////////////////////////////////////////////////
/////////////////////GRID UPDATE TALLY CHECK METHOD
/////////////////////////////////////////////////////////////
//	                    System.out.println("--------------------------------------UPDATE CELL " + x + " " + y);
                    if(updateTally[x][y] == 0){
                        updateCorner(x, y, newGrid);
                        updateCornerCount++;
                        updateTally[x][y] = 1;
                        updated = true;
                    }

                    if(updateTally[x+1][y] == 0){
//	                                System.out.println("Bottom right");
                        updateCorner(x+1, y, newGrid);
                        updateCornerCount++;
                        updateTally[x][y] = 1;
                        updated = true;
                    }

                    if(updateTally[x+1][y+1] == 0 ){
//	                                System.out.println("Top right");
                        updateCorner(x+1, y+1, newGrid);
                        updateCornerCount++;
                        updateTally[x][y] = 1;
                        updated = true;
                    }
                    if(updateTally[x][y+1] == 0){
//	                                System.out.println("Top left");
                        updateCorner(x, y+1, newGrid);
                        updateCornerCount++;
                        updateTally[x][y] = 1;
                        updated = true;
                    }
                }
                else{
                    updateCornerCount++;
                }

            }
        }
        if(updated){
            updateMesh = updated;
            for(byte[] array : updateTally){
                Arrays.fill(array, (byte)0);
            }

        }
    }

    /**
     * Method for updating all vertices located at a given corner coordinate. Updates the alpha component
     * of each vertices stored in the {@link #cornersVerts cornersVerts} array for the given index.
     * Alpha values updated based on the average value of the four cells surrounding the corner. That is
     * the grid elements: at the index passed in, one beneath, one left, and one left and below.
     * @param cornerX The x index of the corner element to update
     * @param cornerY The y index of the corner element to update
     * @param grid The array of environment values
     */
    private void updateCorner(int cornerX, int cornerY, byte[][] grid){
        int count = 0;
        float average = 0;
        int aIndex = 0;
        //Get an average of the values of the 4 possible cells that meet at this corner
        for(int j = cornerY-1; j <= cornerY; j++){
            if(j < 0 || j >= grid.length) continue;
            for(int i = cornerX-1; i <= cornerX; i++){
               if(i < 0 || i >= grid[0].length) continue;

               average += grid[i][j];

               count++;
            }
        }
        if(gridRange > 0)
            average = (average / count)/gridRange;
        else
            average /= count;
        if(average < lowerAlphaCutoff)
            average = lowerAlpha;
        if (average > upperAlphaCutoff)
            average = upperAlpha;
//            if(average >= lowerAlphaCutoff && average <= upperAlpha)
//                return;
        //Then update all the vertices that correspond with this corner

        for(int n = 0; n < cornersVerts[cornerX][cornerY].length; n++){

            if(cornersVerts[cornerX][cornerY][n] != -1){
                //find the alpha component index of each vertex in this corner
                aIndex = cornersVerts[cornerX][cornerY][n];
                //Then update the alpha component in the vert array with the new alpha value
                verts[aIndex] = average;
//                    System.out.println("Update corner " + cornerX + " " + cornerY + " alpha " + average);

            }
        }

    }

    ///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    /**
     * Retrieve the size of the {@link #verts verts} array
     * @return The size of the array
     */
    private int getTotalNumComponents() {
        return verts.length;
    }

    /**
     * Retrieve the number of components each vertex has
     * @return The number of vertex components
     */
    private int getNumVertexComponents() {
       return vertComponents;
    }

    /**
     * Get the number of vertices in the mesh
     * @return Number of mesh vertices
     */
    public int getVertexCount(){
        return getTotalNumComponents()/getNumVertexComponents();
    }
	    
}
