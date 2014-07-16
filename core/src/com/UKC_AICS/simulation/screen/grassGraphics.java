package com.UKC_AICS.simulation.screen;

public class grassGraphics {

	private byte[][] grassAmounts;
	private byte[][][][] tileGrass;//tileX, tileY, then 
	
	public void updateGrass(byte[][] grass){
		for(int i = 0; i<grassAmounts.length; i++){
			for(int j = 0; j < grassAmounts[i].length; j++){
//				for(int n = 0; n < tileGrass[i][j].length; n++){
					if(grassAmounts[i][j] < tileGrass[i][j].length){
						createTileCache(i, j, grassAmounts[i][j]);
					}
					else if(grassAmounts[i][j] > tileGrass[i][j].length){
//						addToCache()
					}
//				}
				
			}
		}
		
		
	}

	private void createTileCache(int tileX, int tileY, byte amount) {
		// TODO Auto-generated method stub
		
	}
	
}
