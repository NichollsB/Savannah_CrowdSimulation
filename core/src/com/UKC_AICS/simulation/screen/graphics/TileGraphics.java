package com.UKC_AICS.simulation.screen.graphics;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class TileGraphics extends SpriteCache {

	private HashMap<String, byte[][]> infoLayers = new HashMap<String, byte[][]>();
	
	private SpriteManager manager;
	private byte[][] grassAmounts;
	private byte[][][][] tileGrass;//tileX, tileY, then 
	private Sprite sprite;
	
	private byte mapElementsX = 0, mapElementsY = 0;
	
	//SpriteCache attempt
	
	int cacheCount = 0;
	private Array<Byte> cacheRows;
	private ObjectMap<Integer, Integer> cacheRow_Map = new ObjectMap<Integer, Integer>();
//	private int[][] 
	
	private final static int MAXCACHE = 100000;
	private static SpriteCache tileCache = new SpriteCache(MAXCACHE, false);
	private int cacheableLayers;
	
	public TileGraphics(HashMap<String, byte[][]> infoLayers, SpriteManager manager, SpriteCache cache){
//		this.infoLayers = infoLayers;
		super(MAXCACHE, false);
		tileCache = cache;
		
		this.manager = manager;
		this.infoLayers = new HashMap<String, byte[][]>();

		this.manager.loadAssets_Tiles(null);
		
		for(String s : infoLayers.keySet()){
			byte[][] f = infoLayers.get(s);
			copyInformationLayer(s, infoLayers.get(s), this.infoLayers);
//			layermap = f;
			if(mapElementsX < f.length)
				mapElementsX = (byte) f.length;
			for(int i = 0; i < f.length; i++){
				if(mapElementsY < f[i].length){
					mapElementsY = (byte) f[i].length;
				}
					
			}
		}
		
	}
	
	public void createCache(int y, HashMap<String, byte[][]> infoLayers){
//		System.out.println(y);
		byte[][] layermap;
		byte amount;
		AtlasRegion region = null;
		AtlasRegion nextRegion = region;
		int xPos = 0;
		int numCachedLayers=0;
		if(cacheRow_Map.containsKey(y)){
			this.beginCache(cacheRow_Map.get(y));
//			System.out.println("Recreate cache " + y + " id "+ cacheRow_Map.get(y));
		}
		else
			this.beginCache();
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
//					System.out.println(x);
//					System.out.println("layer region " + nextRegion.name + " x " + x + " y " + y);
					this.add(region, xPos, y*region.originalHeight, region.originalWidth, region.originalHeight);
					cacheCount += 1;
					numCachedLayers++;
				}
//				else{
//					System.out.println("null region...");
//				}
//				else if (numCachedLayers <= cacheableLayers){
//					cacheCount++;
//					numCachedLayers++;
////					System.out.println(layer + cacheCount);
//					this.add(new Sprite());
//					
//				}
			}
			if(region!= null)
				xPos += region.originalWidth;
		}
		int id = this.endCache();
		cacheRow_Map.put(y, id);
	}
	
	boolean copyLayers = false;
	Array<String> layersToCopy = new Array<String>();
	public void updateTiles(Batch batch, boolean update, HashMap<String, byte[][]> infoLayers){
		this.setProjectionMatrix(batch.getProjectionMatrix());
//		System.out.println(infoLayers.get("grass").length);
		
		
		if(manager.update()){
			if(cacheableLayers <= 0)
				cacheableLayers = manager.getNumTileRegions();
			//CACHE METHOD
			
//			if(infoLayers.equals(this.infoLayers)) return;
//			else{
//			if(update){
				for(int y = 0; y<mapElementsY; y++){
					
					if(!cacheRow_Map.containsKey(y)){
						createCache(y, infoLayers);
						continue;
					}
					else if (update){
						for(String layer : infoLayers.keySet()){
//							System.out.println("updating layer " + layer);
							if(!Arrays.equals(this.infoLayers.get(layer)[y], infoLayers.get(layer)[y])){
//								System.out.println("Value change, copy layer " + layer);
								createCache(y, infoLayers);
								copyLayers = true;
								layersToCopy.add(layer);
								break;
							}
						}
					}
				}
				if(copyLayers){
					for(String layer : layersToCopy){
						copyInformationLayer(layer, infoLayers.get(layer), this.infoLayers);
					}
					copyLayers = false;
					layersToCopy.clear();
				}
//				this.infoLayers = (HashMap<String, byte[][]>) infoLayers.clone();
//			}
//			else{
//				for(int y = 0; y<mapElementsY; y++){
//					if(!cacheRow_Map.containsKey(y)){
//						createCache(y, infoLayers);
//						continue;
//					}
//				}
//			}
//			for(int y = 0; y<mapElementsY; y++){
//				if(!cacheRow_Map.containsKey(y) || update){
////					createCache(y);
//				}
//				
//			}
//			System.out.println(cacheRow_Map.size);
			this.begin();
			int id;
			for(int i : cacheRow_Map.keys()){
				id = cacheRow_Map.get(i);
				try{
					this.draw(id);
				}
				catch(NullPointerException e){
					System.out.println("nul cache id........ row " + i + " id " + id);
				}
//				System.out.println("Drawing cache");
			}
			this.end();
			
			//BATCH METHOD
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
	
	private void copyInformationLayer(String sourceKey, byte[][] sourceValue, HashMap<String, byte[][]> targetMap){
//		System.out.println("Copy layer " + sourceKey);
		byte[][] targetValue = new byte[sourceValue.length][sourceValue[0].length];
		for(int i = 0; i < sourceValue.length; i++){
			for(int j = 0; j < sourceValue[i].length; j++){
//				System.out.println(" i " + i + " j " + j + " targetlength i " + targetValue.length + " j " + targetValue[i].length);
				targetValue[i][j] = sourceValue[i][j];
			}
		}
		targetMap.put(sourceKey, targetValue);
	}

	private void createTileCache(int tileX, int tileY, byte amount) {
		// TODO Auto-generated method stub
		
	}
	
}
