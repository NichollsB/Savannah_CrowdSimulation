package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Tree {@link com.badlogic.gdx.scenes.scene2d.ui.Tree.Node node} class for nodes within the
 * {@link com.UKC_AICS.simulation.screen.gui.EntityTreeTable Entity Tree}.
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
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

    /**
     * Default constructor
     * @param actor Actor to add to this node
     */
	public EntityTree_Node(Actor actor) {
		super(actor);
	}

    /**
     * Construct the node with an actor and a String name
     * @param actor Actor to add to this node
     * @param name The nodes name
     */
	public EntityTree_Node(Actor actor, String name){
		super(actor);
		this.name = name;
		this.label = (Label)this.getActor();
	}

    /**
     * Construct a node with a label, content table, and an entity
     * @param actor
     * @param content Content Table to add to the node
     * @param name Name of the node
     * @param info Information string to apply to the node
     * @param id Node id byte. Generally for defining the type of the entity added
     * @param entity Entity stored in this node
     * @param root True if this node is a root node
     */
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

    /**
     * Construct a node with a label, and an entity
     * @param actor Label to add to this node
     * @param name Name of the node
     * @param info Information string to apply to the node
     * @param id Node id byte. Generally for defining the type of the entity added
     * @param entity Entity stored in this node
     * @param root True if this node is a root node
     */
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

    /**
     * Re-generate the text for the nodes label. With the {@link #name name} of the node, and the nodes
     * {@link #numChildren population} of child entities contained
     */
	public void setText(){
		this.label.setText(name + " : " + numChildren);
	}

    /**
     * Set the nodes text with the specified String parameter
     * @param text To add to the node label
     */
	public void setText(String text){
		this.text = text;
		this.label.setText(text);
	}

    /**
     * Get the entity contained in the node, if there is one
     * @return The entity of the node, or null if there is none
     */
	public Entity getEntity(){
		return entity;
	}

    /**
     * Get the node id
     * @return The node id byte
     */
	public byte getID(){
		return id;
	}

    /**
     * Get the name of the node
     * @return Nodes name
     */
	public String getName(){
		return name;
	}

    /**
     * Get the information string applied to the node
     * @return Node {@link #info information}
     */
	public String getInfo(){
		return info;
	}
    /**
     * Sets the information string of the node
     * @param info String to apply to {@link #info information}
     */
    public void setInfo(String info){
        this.info = info;
    }
    /**
     * Whether or not the node has an entity added to it
     * @return True if it has an entity, otherwise false
     */
	public boolean hasEntity(){
		return hasEntity;
	}

    /**
     * Is the node a root node
     * @return true if it is a root node
     */
	public boolean isRoot() {
		return root;
	}

    /**
     * Set the number of children the node has, and call the {@link #setText()} method
     * @param num Value to apply to the {@link #numChildren field}
     */
	public void setNumChildren(int num){
		numChildren = num;
		setText();
	}

    /**
     * increment the nodes number of children by the parameter value, and call the {@link #setText()} method
     * @param num Value to add to {@link #numChildren}
     */
	public void incrementNumChildren(int num){
		numChildren+=num;
		setText();
	}

    /**
     * Set the number of {@link #numChildren children} the node has based on the number of child nodes added
     */
	public void findNumChildren(){
		numChildren = this.getChildren().size;
		setText();
	}

    /**
     * Return the number of children this node has
     * @return {@link #numChildren}
     */
	public int numChildren(){
		return numChildren;
	}

    /**
     * Add a node to a child of this node, or create a group node and add the node as a child to that.
     * Child nodes are stored in {@link #childNodes}
     * @param node Node to add as a child
     * @param id {@link #id id} value to give to the node
     * @return The child of this node - the node to which the parameter node was added to
     */
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

    /**
     * Set the {@link #parent parent} node of this node
     * @param parent Node to set as parent
     */
	public void setParent(EntityTree_Node parent){
		this.parent = parent;
	}

    /**
     * Remove the given node from the parent of this node. Should generally be used to remove this node from
     * the tree.
     * @param node To remove
     */
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

    /**
     * Get the children of this node
     * @return Array of {@link #childNodes} values
     */
	public Array<EntityTree_Node> getNodeChildren(){
		return childNodes.values().toArray();
	}
}
