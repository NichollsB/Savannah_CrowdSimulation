package com.UKC_AICS.simulation.screen.graphics;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class TileMesh {
	 private byte[][] grid;//contains values of grid
	    //contains x,y position of each cell adjaxcent to corresponding corner
	    //Key corresponds to corner in gridCorners, int[][] array holds x,y positions of cells in the grid array
	    private ObjectMap<Integer, int[][]> cornersCells = new ObjectMap<Integer, int[][]>();
	    
	    private int[][] gridCorners;//contains the vertices within each corner - first value is color?
	    private int cornerCount = 0;
	    
	    private int[] verticesOffsets;//linear list of vertexes with component offsets?
	    private int vertOffsetsCount = 0;//current count of vertex when adding to vertOffsets
	    
	    private final Array<Integer> gridIndices = new Array<Integer>();
	    private short[] indices;
	    
	    private float[] verts;
	    private float[] vertsComps;
	    private int vertCompCount = 0;
	    
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
	    
	    private int texRepeatX, texRepeatY;
	    
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

    public TileMesh(float lowerCutoff, float upperCutoff, float alphaMin, float alphaMax, int valueRange){
            this.lowerAlphaCutoff = lowerCutoff;
        this.upperAlphaCutoff = upperCutoff;
        this.lowerAlpha = alphaMin;
            this.upperAlpha = alphaMax;
            this.gridRange = valueRange;
        }
        public TileMesh(){}
	    
	    private boolean created = false;

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
	    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight, Color c){
	    	usesTexture = true;
	    	this.color = c;
            this.texIncrementY = 1;
            this.texIncrementX = 1;
	    	createMesh(grid, startX, startY, cellWidth, cellHeight);
	    }
	    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight,
	            AtlasRegion atlasRegion, boolean allowStretch, int texCellsX, int texCellsY){
	    	texture = atlasRegion.getTexture();
	    	createMesh(grid, startX, startY, cellWidth, cellHeight, atlasRegion.originalWidth, atlasRegion.originalHeight,
	    			allowStretch, texCellsX, texCellsY);
	    }
	    public void createMesh(byte[][] grid, int startX, int startY, int cellWidth, int cellHeight,
	            Texture texture, boolean allowStretch, int texCellsX, int texCellsY, Color color){
            this.color = color;
	    	this.texture = texture;
	    	createMesh(grid, startX, startY, cellWidth, cellHeight, texture.getWidth(), texture.getHeight(),
	    			allowStretch, texCellsX, texCellsY);
	    }
	    public void createMesh(byte[][] bs, int startX, int startY, int cellWidth, int cellHeight,
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
	            this.texIncrementX = (float)texCellsX / (float)bs[0].length;
	            this.texIncrementY = (float)texCellsY / (float)bs.length;
	            createMesh(bs, startX, startY, cellWidth, cellHeight);
	        }
	        else {
	            //Divide pixel length of grid by pixel length of texture to give number of native textures that will fit on the grid.
	            maxTexCellsX = (float)(bs[0].length*cellWidth) / (float)(texWidth);
	            maxTexCellsY = (float)(bs.length*cellHeight) / (float)(texHeight);
	            //Divide the max number of native texture cells by the number of grid points to get the number of texture units per grid point.
	            this.texIncrementX = maxTexCellsX / (float)bs[0].length;
	            this.texIncrementY = maxTexCellsY / (float)bs.length;
	            createMesh(bs, startX, startY, cellWidth, cellHeight);
	            
	        }
	        
	    }
	    /*
	    public void createMesh(int[][] grid, int startX, int startY, int width, int height,
	            Texture texture, int texRepeat, boolean repeatTexX){
	        createMesh(grid, startX, startY, width, height, true, texture, texRepeat, repeatTexX);
	    }
	    
	     public void createMesh(int[][] grid, int startX, int startY, int width, int height,
	            boolean fixedCellDimensions, Texture texture, int texRepeat, boolean repeatTexX){
	        this.texRepeatX = texRepeat;
	        this.texRepeatY = texRepeat;
	        this.repeatTexX = repeatTexX;
	        createMesh(grid, startX, startY, width, height, fixedCellDimensions, texture);
	    }
	     
	    public void createMesh(int[][] grid, int startX, int startY, int width, int height,
	            Texture texture){
	        createMesh(grid, startX, startY, width, height, true, texture);
	    }
	    
	    public void createMesh(int[][] grid, int startX, int startY, int width, int height, 
	            boolean fixedCellDimensions, Texture texture){
	        meshTexture = texture;
	        usesTexture = true;
	        createMesh(grid, startX, startY, width, height, fixedCellDimensions, defaultCol);
	    }
	    
	    public void createMesh(int[][] grid, int startX, int startY, int width, int height,
	            Color color){
	        createMesh(grid, startX, startY, width, height, true, color);
	    }
	    
	    public void createMesh(int[][] grid, int startX, int startY, int width, int height, 
	            boolean fixedCellDimensions, Color color){
	        if(fixedCellDimensions){
	            createMesh(grid, startX, startY, width, height);
	        }
	        else {
	            //createMesh(grid, startX, startY, width/grid[0].length, height/grid.length);
	            createMesh(grid, startX, startY, width, height);
	        }
	        col = color;
	        usesTexture = false;
	    }
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
	    int vertCount = 0;
	    //void drawTrianglePair(int cellX, int cellY, float x, float y, float width, float height, float texX, float texY, float texWidth, float texHeight, Color color1) {
	    //renamed variable texWidth and texHeight as they are misleading - they are not the height and width of the texture.
	    void drawTrianglePair(int cellX, int cellY, float x, float y, float width, float height, float texX, float texY, float texStepX, float texStepY, Color color1) {
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
	        verts[idx++] = color1.r; //Color(r, g, b, a)
	        verts[idx++] = color1.g;
	        verts[idx++] = color1.b;
	        verts[idx++] = color1.a;
	        verts[idx++] = texX; 
	        verts[idx++] = texY;
	        n++;
	        aIndex = aInitial + (n-1)*vertexComponents;
	        addCornersVert(cellX, cellY, aIndex);
	        
	        //topleft vertex
	        verts[idx++] = x; //Position(x, y)
	        verts[idx++] = y + height;
	        verts[idx++] = color1.r; //Color(r, g, b, a)
	        verts[idx++] = color1.g;
	        verts[idx++] = color1.b;
	        verts[idx++] = color1.a;
	        verts[idx++] = texX; 
	        verts[idx++] = texY+texStepY;
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
	        verts[idx++] = texX + texStepX; 
	        verts[idx++] = texY + texStepY;
	        n++;
	        aIndex = aInitial + (n-1)*vertexComponents;
	        addCornersVert(cellX+1, cellY+1, aIndex);
	        
	        //bottom left vertex
	        verts[idx++] = x;	//Position(x, y)
	        verts[idx++] = y;
	        verts[idx++] = color1.r;	//Color(r, g, b, a)
	        verts[idx++] = color1.g;
	        verts[idx++] = color1.b;
	        verts[idx++] = color1.a;
	        verts[idx++] = texX; 
	        verts[idx++] = texY;
	        n++;
	        aIndex = aInitial + (n-1)*vertexComponents;
	        addCornersVert(cellX, cellY, aIndex);
	        
	        //top right vertex
	        verts[idx++] = x + width;	//Position(x, y)
	        verts[idx++] = y + height;
	        verts[idx++] = color1.r;	//Color(r, g, b, a)
	        verts[idx++] = color1.g;
	        verts[idx++] = color1.b;
	        verts[idx++] = color1.a;
	        verts[idx++] = texX + texStepX; 
	        verts[idx++] = texY + texStepY;
	        n++;
	        aIndex = aInitial + (n-1)*vertexComponents;
	        addCornersVert(cellX+1, cellY+1, aIndex);
	        
	        //bottom right vertex
	        verts[idx++] = x + width;	//Position(x, y)
	        verts[idx++] = y;
	        verts[idx++] = color1.r;	//Color(r, g, b, a)
	        verts[idx++] = color1.g;
	        verts[idx++] = color1.b;
	        verts[idx++] = color1.a;
	        verts[idx++] = texX + texStepX; 
	        verts[idx++] = texY;
	        n++;
	        aIndex = aInitial + (n-1)*vertexComponents;
	        addCornersVert(cellX+1, cellY, aIndex);
	        
	        vertCount = n;
	    }
	    void drawTrianglePair(int cellX, int cellY, float x, float y, float width, float height, Color color1) {
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
	    public void addCornersVert(int cornerX, int cornerY, int aIndex){
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
	    ////////////////////////////////////////////////////////
	    /////////////////////////////////////////////////////////
	    public boolean update(byte[][] grid){
            return update(grid, false);
        }
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
        public void compareAndUpdate(byte[][] newGrid){
            compareAndUpdate(newGrid, false);
        }
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
	    public void updateCorner(int cornerX, int cornerY, byte[][] grid){
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
	        average = (average / count)/gridRange;
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

	    int getIndex() {
	        return verts.length;
	    }

	    int getNumVertexComponents() {
	       return vertComponents;
	    }
	    public int getVertexCount(){
	    	return getIndex()/getNumVertexComponents();
	    }
	    
}
