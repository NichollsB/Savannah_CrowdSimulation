package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class BoidTree_Node extends Node{
	private Boid boid;
	private Species species;
	private byte spcByte;
	private boolean isSpecies = false;
	
	private String name;
	private byte spByte;
	private String info;
	
	public BoidTree_Node(Actor actor) {
		super(actor);
	}
	public BoidTree_Node(Actor actor, String name, String info, byte species, boolean isSpecies, Boid boid) {
		super(actor);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
		this.name = name;
		this.info = info;
		this.spByte = species;
		this.isSpecies = isSpecies;
		this.boid = boid;
	}
	
	public Boid getBoid(){
		return boid;
	}
	
	public byte getSpecies(){
		return spByte;
	}
	
	
	public String getName(){
		return name;
	}
	
	public String getInfo(){
		return info;
	}
	public boolean isSpecies(){
		return isSpecies;
	}
//	public BoidTree_Node(Actor actor, Boid boid) {
//		super(actor);
//		this.boid = boid;
//		isSpecies = false;
//		spcByte = boid.getSpecies();
//	}
//	public byte getSpeciesVyte(){
//		return spcByte;
//	}
//	public Species getSpecies(){
//		if(isSpecies) return species;
//		return null;
//	}
//	public Boid getBoid(){
//		if(isSpecies) return null;
//		return boid;
//	}

}
