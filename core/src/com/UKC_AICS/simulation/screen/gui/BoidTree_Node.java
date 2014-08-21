package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Species;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class BoidTree_Node extends Tree.Node{
	private Entity boid;
	private boolean hasBoid = false;
	
	private String name = "";
	private byte id;
	private String info;
	
	private boolean root;
	private String text;
	private int numChildren = 0;
	private ObjectMap<Byte, BoidTree_Node> childNodes = new ObjectMap<Byte, BoidTree_Node>();
	private BoidTree_Node parent;
	
	private Label label;
	
	public BoidTree_Node(Actor actor) {
		super(actor);
	}
	public BoidTree_Node(Actor actor, String name){
		super(actor);
		this.name = name;
		this.label = (Label)this.getActor();
	}
    public BoidTree_Node(Label actor, Table content, String name, String info, byte id, Entity entity, boolean root) {
        super(content);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
        this.name = name;
        this.info = info;
        this.id = id;
        this.root = root;
        if(entity != null){
            this.boid = entity;
            hasBoid = true;
        }
        this.label = actor;
    }
	public BoidTree_Node(Label actor, String name, String info, byte id, Entity entity, boolean root) {
		super(actor);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
		this.name = name;
		this.info = info;
		this.id = id;
		this.root = root;
		if(entity != null){
			this.boid = entity;
			hasBoid = true;
		}
		this.label =  actor;
	}
	
	public void setText(){
		this.label.setText(name + " : " + numChildren);
	}
	public void setText(String text){
		this.text = text;
		this.label.setText(text);
	}
	
	public Entity getBoid(){
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
	
	public BoidTree_Node addNode(BoidTree_Node node, byte id){
		BoidTree_Node child;
		if(!childNodes.containsKey(id)){
//			child = childNodes.get(id);
			String name = this.getName() + " " + id;
			Label groupLabel = (Label)node.getActor();
			groupLabel = new Label(name, groupLabel.getStyle());
			String groupInfo = name + "/n" + "/t" + "Population: " + 0;
			BoidTree_Node newNode = new BoidTree_Node(groupLabel, name, groupInfo, id, null, false);
			childNodes.put(id, newNode);
			newNode.setParent(this);
			this.add(newNode);
//			return newNode;
		}
		child = childNodes.get(id);
		child.add(node);
		node.setParent(child);
		child.incrementNumChildren(1);
//		childNodes.put((byte)childNodes.size, node);
		this.incrementNumChildren(1);
		this.setText();
		return child;
	}
	
	public void setParent(BoidTree_Node parent){
		this.parent = parent;
	}
	public void removeNode(BoidTree_Node node){
		
		if(parent != null)
			parent.removeNode(node);
		if(node!= null){
			if(!node.equals(this))
				incrementNumChildren(-1);
			else
				this.remove();
		}
		else
			incrementNumChildren(-1);
	}
	
	public Array<BoidTree_Node> getNodeChildren(){
		return childNodes.values().toArray();
	}
}
