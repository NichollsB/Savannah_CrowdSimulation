package com.UKC_AICS.simulation.screen.graphics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class TileGraphics {

	private HashMap<String, byte[][]> infoLayers = new HashMap<String, byte[][]>();
	
	private SpriteManager manager;
	private byte[][] grassAmounts;
	private byte[][][][] tileGrass;//tileX, tileY, then 
	private Sprite sprite;
	
	private byte mapElementsX = 0, mapElementsY = 0;
	
	//SpriteCache attempt
	static final SpriteCache tileCache = new SpriteCache(10000, false);
	int spriteCacheCount = 0;
	private Array<Byte> cacheRows;
	private ObjectMap<Byte, Integer> cacheRow_Map = new ObjectMap<Byte, Integer>();
//	private int[][] 
	
	public TileGraphics(HashMap<String, byte[][]> infoLayers, SpriteManager manager){
//		this.infoLayers = infoLayers;
		this.manager = manager;
		this.infoLayers = infoLayers;

		this.manager.loadAssets_Tiles(null);
		
		for(byte[][] f : infoLayers.values()){
//			layermap = f;
			if(mapElementsX < f.length)
				mapElementsX = (byte) f.length;
			for(int i = 0; i < f.length; i++){
				if(mapElementsY < f[i].length){
					mapElementsY = (byte) f[i].length;
				}
					
			}
//			break;
		}
	}
	
	public void createCache(int y){
		byte[][] layermap;
		byte amount;
		AtlasRegion region = null;
		AtlasRegion nextRegion = region;
		int xPos = 0;
		if(cacheRow_Map.containsKey((byte)y)){
			tileCache.beginCache(y);
		}
		else
			tileCache.beginCache();
		for(int x = 0; x<mapElementsX; x++)
		{
			for(String layer : infoLayers.keySet())
			{
				layermap = infoLayers.get(layer);
				amount = layermap[x][y];
				if(layer.equals("water")){
					amount = (byte) ((amount >= 50) ? 90f : 0);
//					System.out.println(amount);
//					amount = 0;
				}
				else{
					for(int i = 0, nexti = 0; i <= 100; i+=10, nexti=i+10){
						if(amount >= i &&amount < nexti){
							amount = (byte) i;
						}
					}
				}
				nextRegion = manager.getTileRegion(layer, amount);
				if(nextRegion != null){
					region = nextRegion;
					System.out.println(x);
					tileCache.add(region, xPos, y*region.originalHeight, region.originalWidth, region.originalHeight);
				}
			}
			if(region!= null)
				xPos += region.originalWidth;
		}
		cacheRow_Map.put((byte)y, tileCache.endCache());
	}
	
	
	public void updateTiles(Batch batch){
		if(manager.update()){
			for(int y = 0; y<mapElementsY; y++){
				if(!cacheRow_Map.containsKey((byte) y))
					createCache(y);
				
			}
			tileCache.begin();
			for(int i : cacheRow_Map.values()){
				tileCache.draw(i);
			}
			tileCache.end();
			
			
////			batch.begin();
//			float amount;
//			float alpha;
//			int xPos = 0, yPos = 0;
//			boolean get = true;
//			
//			if(infoLayers.isEmpty())
//				return;
//			byte[][] layermap;
//			AtlasSprite nextSprite = null;
//			AtlasRegion region = null;
//			AtlasRegion nextRegion = null;
//			for(int x = 0; x < mapElementsX; x++)
//			{
//				for(int y = 0; y<mapElementsY; y++)
//				{
//					for(String layer : infoLayers.keySet())
//					{
////						if(layer != "water" || layer != "grass")
////							continue;
//						layermap = infoLayers.get(layer);
//						amount = (float)layermap[x][y];
//						
//						if(layer == "water"){
//							amount = (amount >= 50) ? 90f : 0;
////							System.out.println(amount);
////							amount = 0;
//						}
//						else{
//							for(int i = 0, nexti = 0; i <= 100; i+=10, nexti=i+10){
//								if(amount >= i &&amount < nexti){
//									amount = i;
//								}
//							}
//						}
////						nextSprite = manager.getTileSprite(layer, amount);
////						if(nextSprite != null){
////							sprite = nextSprite;
////							sprite.setPosition(xPos, yPos);
////							sprite.draw(batch);
////						}	
//						nextRegion = manager.getTileRegion(layer, (int)amount);
//						if(nextRegion != null){
//							region = nextRegion;
//							batch.draw(region, xPos, yPos, region.originalWidth, region.originalHeight);
//						}
////						System.out.println(region.name);
//						
//					}
////					if(sprite != null)
////						yPos += sprite.getHeight();
//
//					if(region != null){
//						yPos += region.originalHeight;
//					}
//						
//					
//				}
////				if(sprite != null)
////					xPos += sprite.getWidth();
//				if(region != null)
//					xPos += region.originalWidth;
//				yPos = 0;
//			}
					
			
			
//			for(String layer : infoLayers.keySet()){
//				if(layer != "grass" || layer != "water")  continue;
//				AtlasSprite nextSprite = null;
////				System.out.println(sprite);
////				if(sprite != null){
//					xPos = 0;
//					yPos = 0;
//					byte[][] layermap = infoLayers.get(layer);
//					for(int x = 0; x < layermap.length; x++){
//						for(int y = 0; y<layermap[x].length; y++){
//							amount = (float)layermap[x][y];
////							if(layer == "grass"){
////								sprite.setAlpha(amount);
////							}
////							else if(layer == "water" ){
////								if(amount > 0.6){
////									sprite.setAlpha(0.9f);
////								}
////								else
////									sprite.setAlpha(0);
////							}
//							if(layer == "water"){
//								amount = (amount >= 50) ? 90f : 0;
////								System.out.println(amount);
////								amount = 0;
//							}
//							else{
//								for(int i = 0, nexti = 0; i <= 100; i+=10, nexti=i+10){
//									if(amount >= i &&amount < nexti){
//										amount = i;
//									}
//								}
//							}
////							amount = 50;
////							if(get){
//								nextSprite = manager.getTileSprite(layer, amount);
//								get = false;
////							}
////							System.out.println(sprite);
//							if(nextSprite != null){
//								sprite = nextSprite;
//								sprite.setPosition(xPos, yPos);
//								sprite.draw(batch);
//							}	
//							if(sprite != null)
//								yPos += sprite.getHeight();
//						}
//						if(sprite != null)
//							xPos += sprite.getWidth();
//						yPos = 0;
//					}

//				}
//			}
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
