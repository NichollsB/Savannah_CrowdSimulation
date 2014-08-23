package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityTree_Node extends Tree.Node{
	private Entity entity;
	private boolean hasEntity = false;
	
	private String name = "";
	private byte id;
	private String info;
	
	private boolean root;
	private String text;
	private int numChildren = 0;
	private ObjectMap<Byte, EntityTree_Node> childNodes = new ObjectMap<Byte, EntityTree_Node>();
	private EntityTree_Node parent;
	
	private Label label;
	
	public EntityTree_Node(Actor actor) {
		super(actor);
	}
	public EntityTree_Node(Actor actor, String name){
		super(actor);
		this.name = name;
		this.label = (Label)this.getActor();
	}
    public EntityTree_Node(Label actor, Table content, String name, String info, byte id, Entity entity, boolean root) {
        super(content);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
        this.name = name;
        this.info = info;
        this.id = id;
        this.root = root;
        if(entity != null){
            this.entity = entity;
            hasEntity = true;
        }
        this.label = actor;
    }
	public EntityTree_Node(Label actor, String name, String info, byte id, Entity entity, boolean root) {
		super(actor);
//		this.species = species;
//		isSpecies = true;
//		spcByte = species.getSpbyte();
		this.name = name;
		this.info = info;
		this.id = id;
		this.root = root;
		if(entity != null){
			this.entity = entity;
			hasEntity = true;
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
	
	public Entity getEntity(){
		return entity;
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
	
	public boolean hasEntity(){
		return hasEntity;
	}
	
	public void setInfo(String info){
		this.info = info;
	}
//	public EntityTree_Node(Actor actor, Entity entity) {
//		super(actor);
//		this.entity = entity;
//		isSpecies = false;
//		spcByte = entity.getSpecies();
//	}
//	public byte getSpeciesVyte(){
//		return spcByte;
//	}
//	public Species getSpecies(){
//		if(isSpecies) return species;
//		return null;
//	}
//	public Entity getEntity(){
//		if(isSpecies) return null;
//		return entity;
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
	
	public EntityTree_Node addNode(EntityTree_Node node, byte id){
		EntityTree_Node child;
		if(!childNodes.containsKey(id)){
//			child = childNodes.get(id);
			String name = this.getName() + " " + id;
			Label groupLabel = (Label)node.getActor();
			groupLabel = new Label(name, groupLabel.getStyle());
			String groupInfo = name + "/n" + "/t" + "Population: " + 0;
			EntityTree_Node newNode = new EntityTree_Node(groupLabel, name, groupInfo, id, null, false);
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
	
	public void setParent(EntityTree_Node parent){
		this.parent = parent;
	}
	public void removeNode(EntityTree_Node node){
		
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
	
	public Array<EntityTree_Node> getNodeChildren(){
		return childNodes.values().toArray();
	}
}
