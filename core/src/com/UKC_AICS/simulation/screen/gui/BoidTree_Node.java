package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class BoidTree_Node extends Node{
	private Boid boid;
	private boolean hasBoid = false;
	
	private String name = "";
	private byte id;
	private String info;
	
	private boolean root;
	private String text;
	private int numChildren = 0;
	
	private Label label;
	
	public BoidTree_Node(Actor actor) {
		super(actor);
	}
	public BoidTree_Node(Actor actor, String name){
		super(actor);
		this.name = name;
		this.label = (Label)this.getActor();
	}
	public BoidTree_Node(Actor actor, String name, String info, byte id, Boid boid, boolean root) {
		super(actor);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
		this.name = name;
		this.info = info;
		this.id = id;
		this.root = root;
		if(boid != null){
			this.boid = boid;
			hasBoid = true;
		}
		this.label = (Label)this.getActor();
	}
	
	public void setText(){
		this.label.setText(name + " : " + numChildren);
	}
	public void setText(String text){
		this.text = text;
		this.label.setText(text);
	}
	
	public Boid getBoid(){
		return boid;
	}
	
	public byte getID(){
		return id;
	}
	
	
	public String getName(){
		return name;
	}
	
	public String getInfo(){
		return info;
	}
	
	public boolean hasBoid(){
		return hasBoid;
	}
	
	public void setInfo(String info){
		this.info = info;
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
	public boolean isRoot() {
		return root;
	}
	
	public void setNumChildren(int num){
		numChildren = num;
		setText();
	}
	public void incrementNumChildren(int num){
		numChildren+=num;
		setText();
	}
	public void findNumChildren(){
		numChildren = this.getChildren().size;
		setText();
	}

	public int numChildren(){
		return numChildren;
	}
}
