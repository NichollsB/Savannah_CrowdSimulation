package com.UKC_AICS.simulation.screen.graphics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.ObjectMap;

public class TileGraphics {

	private HashMap<String, byte[][]> infoLayers = new HashMap<String, byte[][]>();
	
	private SpriteManager manager;
	private byte[][] grassAmounts;
	private byte[][][][] tileGrass;//tileX, tileY, then 
	private Sprite sprite;
	
	public TileGraphics(HashMap<String, byte[][]> infoLayers, SpriteManager manager){
//		this.infoLayers = infoLayers;
		this.manager = manager;
		this.infoLayers = infoLayers;

		this.manager.loadAssets_Tiles(null);
	}
	
	public void updateTiles(Batch batch){
		if(manager.update()){
//			batch.begin();
			float amount;
			float alpha;
			int xPos = 0, yPos = 0;
			for(String layer : infoLayers.keySet()){

				AtlasSprite nextSprite;
//				System.out.println(sprite);
//				if(sprite != null){
					xPos = 0;
					yPos = 0;
					byte[][] layermap = infoLayers.get(layer);
					for(int x = 0; x < layermap.length; x++){
						for(int y = 0; y<layermap[x].length; y++){
							amount = (float)layermap[x][y];
//							if(layer == "grass"){
//								sprite.setAlpha(amount);
//							}
//							else if(layer == "water" ){
//								if(amount > 0.6){
//									sprite.setAlpha(0.9f);
//								}
//								else
//									sprite.setAlpha(0);
//							}
							if(layer == "water"){
								amount = (amount > 60) ? 90f : 0;
//								amount = 0;
							}
							nextSprite = manager.getTileSprite(layer, amount);
//							System.out.println(sprite);
							if(nextSprite != null){
								sprite = nextSprite;
								sprite.setPosition(xPos, yPos);
								sprite.draw(batch);
							}	
							if(sprite != null)
								yPos += sprite.getHeight();
						}
						if(sprite != null)
							xPos += sprite.getWidth();
						yPos = 0;
					}

//				}
			}
//			batch.end();
		}
	}
	
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
