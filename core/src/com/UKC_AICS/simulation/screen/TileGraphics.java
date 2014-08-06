package com.UKC_AICS.simulation.screen;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;

public class TileGraphics {

	private final ObjectMap<String, byte[][]> infoLayers = new ObjectMap<String, byte[][]>();
	
	private SpriteManager manager;
	private byte[][] grassAmounts;
	private byte[][][][] tileGrass;//tileX, tileY, then 
	private Sprite sprite;
	
	public TileGraphics(HashMap<String, byte[][]> infoLayers, SpriteManager manager){
//		this.infoLayers = infoLayers;
		this.manager = manager;
//		this.manager.loadAssets_Tiles();
		for(String key : infoLayers.keySet()){
			if(this.manager.loadAssets_Tile(key)){
				this.infoLayers.put(key, infoLayers.get(key));
			}
//			this.manager.loadAssets_Tile(key);
//			System.out.println(infoLayers.);
		}
	}
	
	public void updateTiles(Batch batch){
		if(manager.update()){
//			batch.begin();
			float amount;
			float alpha;
			int xPos = 0, yPos = 0;
			for(String layer : infoLayers.keys()){
				sprite = manager.getTileSprite(layer);
				if(sprite != null){
					xPos = 0;
					yPos = 0;
					byte[][] layermap = infoLayers.get(layer);
					for(int x = 0; x < layermap.length; x++){
						for(int y = 0; y<layermap[x].length; y++){
							amount = (float)layermap[x][y];
							if(layer.equals("grass")){
								sprite.setAlpha(amount/100);
							}
							else if(layer.equals("terrain" )){
								
								if(amount >=1 ){
									sprite.setAlpha(0.9f);
								}
								else
									sprite.setAlpha(0);
							}
							sprite.setPosition(xPos, yPos);
							sprite.draw(batch);
							yPos += sprite.getHeight();
						}
						xPos += sprite.getWidth();
						yPos = 0;
					}

				}
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
