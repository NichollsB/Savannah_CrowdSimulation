package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.screen.controlutils.ControlState;
import com.UKC_AICS.simulation.screen.controlutils.HoverListener;
import com.UKC_AICS.simulation.screen.controlutils.SelectedEntity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.UKC_AICS.simulation.entity.*;


/**
 * GUI component for displaying a scrollable tree style list of {@link com.UKC_AICS.simulation.entity.Entity entities}.
 * Allows the selection of boids, acquisition of information for display elsewhere, and removable of entities and explicit
 * placement in the simulation environment
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public class EntityTreeTable extends Table {
	//Entitys
	private final ObjectMap<Byte, EntityTree_Node> entityRoots = new ObjectMap<Byte, EntityTree_Node>();
	private final ObjectMap<Byte, ObjectMap<Byte, EntityTree_Node>> rootGroups = new ObjectMap<Byte, ObjectMap<Byte, EntityTree_Node>> ();
	private final ObjectMap<Entity, EntityTree_Node> entityNodes = new ObjectMap<Entity, EntityTree_Node>();
	
	private Tree tree;
	private EntityTree_Node root;
	private EntityTree_Node selectedNode;
	private boolean rootSelected = true;
	
	private Skin skin;
	private SimScreenGUI gui;
	
	private String selectedInfo = "";
	private Entity selectedEntity;
	private boolean entitySelected = false;
	private boolean groupSelected;

	
	private byte selectedType = 0;
	private byte selectedGroup = 0;
	
	private Array<HoverListener> hoverListeners = new Array<HoverListener>();
	
	private Array<Entity> nodeComparison = new Array<Entity>();
	
	private int entityNodes_num = 0;
	
	private ObjectMap<String, Button> buttons;
	private ObjectMap<Button, String> buttonsInfo;
	

	private ScrollPane scrollPane;
	private Stage stage;

    private byte treeEntityType;

    /**
     * Initialise the buttons for the window table with a given skin. Creates a remove and checked button,
     * that is, a button for deleting the selected entity, and one for the explicit placement of entities in the
     * simulation environment
     * @param skin To apply to buttons, etc
     */
	public void initButtons(Skin skin){
		final Skin s = skin;
		buttons = new ObjectMap<String, Button>(){{
			put("REMOVE", new TextButton("Remove Selected", s));
			TextButton.TextButtonStyle style = new TextButton("", s).getStyle();
			TextButton.TextButtonStyle checkedStyle = new TextButton.TextButtonStyle(style.up, style.down, style.down, style.font);
			put("CHECKED", new TextButton("Place New", checkedStyle));
		}};
		buttonsInfo = new ObjectMap<Button, String>(){{
			put(buttons.get("REMOVE"), "Remove selected item");
			put(buttons.get("REMOVE"), "Place new item of selected type - click to place on map");
		}};
		
	}

    /**
     * Constructor for the table
     * @param title To apply to the table
     * @param skin
     * @param gui The parent {@link com.UKC_AICS.simulation.screen.gui.SimScreenGUI gui}.
     */
	public EntityTreeTable(String title, Skin skin, SimScreenGUI gui) {
		super(skin);
		this.skin = skin;
		initButtons(skin);
	
		
		this.gui = gui;
		this.tree = new Tree(skin);
		hoverListeners.add(gui);
		createScrollPane(tree);
		
		
		root = new EntityTree_Node(new Label(title, skin), title);
		
		tree.add(root);
		root.setExpanded(true);
		tree.addListener(new SelectNodeListener(tree));
//		root.setSelectable(false);
		tree.getSelection().choose(root);
		selectedNode = root;
		tree.setName("EntityTree");
		//Add components to the table
		this.add(new Label(title, skin));
		this.row();
		
		this.add(scrollPane).fill().expand();
		this.row();
		this.add(buttons());
		this.pack();
		//Listener Test
//		registerListener(listener);
        tree.getSelection().clear();

        final SimScreenGUI g = gui;
        final EntityTreeTable t = this;
        this.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                g.deselectTree(t);
            }
        });
	}

    /**
     * Add the tables buttons to a table
     * @return Table of buttons
     */
	private Table buttons(){
		Table btnGrp = new Table();

		TextButton btn ;
		btn = (TextButton) buttons.get("REMOVE");
		btn.addListener(new SelectOptionsListener("REMOVE", buttonsInfo.get(btn)));
		btnGrp.add(btn).expandX().fillX();
		btnGrp.row();
		btn = (TextButton) buttons.get("CHECKED");
		btn.addListener(new SelectOptionsListener("CHECKED", buttonsInfo.get(btn)));
		btnGrp.add(btn).expandX().fillX();
		return btnGrp;
	}

	/**
	 * Adds a node to the tree under the specified species type byte node, and group node. If no group node exists matching
	 * the entitys group then addNodeGroup(byte spByte, byte group, String info) is called
	 * @param spByte Byte of the species to associate this entity with
	 * @param name String name to give the node
	 * @param info String stored in the node that may be displayed on selection of the node
	 * @param entity Entity to be associated with the created node
	 * @return Null if a root node associated with spByte does not exist, otherwise returns EntityTree_Node that is added
	 * as a child of group and species.
	 */
	public EntityTree_Node addEntityNode(byte spByte, String name, String info, Entity entity){
		Label label;
		EntityTree_Node node, entityNode;
		if(entity == null) return null;
		if(!entityRoots.containsKey(spByte)){
			System.out.println("Missing rootNode");
			return null;
		}
		node = entityRoots.get(spByte);
		String entityName = (entityRoots.get(spByte).getName() + " " + entity.getTertiaryType());
		label = new Label(entityName, skin);
		entityNode = new EntityTree_Node(label, entityName, info, spByte, entity, false);
		node.addNode(entityNode, entity.getTertiaryType());
//		entityNode.setParent(node);
//		entityNode.setParent(node);
		entityNodes_num++;
		root.setNumChildren(entityNodes_num);
        treeEntityType = entity.getType();
//			entityNodes.put()
//		System.out.println("entityNode added." +  " type " + entityNode.getName() + " subtype " + entity.getTertiaryType());
		return entityNode;
//		if(!rootGroups.containsKey(spByte) || !rootGroups.get(spByte).containsKey(entity.getType())){
//			addNodeGroup(spByte, entity.getTertiaryType(), info);
//			System.out.println("GROUP TYPE " + entity.getTertiaryType());
//		}
//		String entityName = (entityRoots.get(spByte).getName() + " " + entity.getTertiaryType());
//		label = new Label(entityName, skin);
//		node = new EntityTree_Node(label, entityName, info, spByte, entity, false);
//		entityNode = node;
//		try{
//			rootGroups.get(spByte).get(entity.getTertiaryType()).add(node);
//			rootGroups.get(spByte).get(entity.getTertiaryType()).incrementNumChildren(1);
//			rootGroups.get(spByte).get(entity.getTertiaryType()).setText();
//			node = entityRoots.get(spByte);
//			node.incrementNumChildren(1);
//			node.setText();
//			entityNodes_num++;
//			root.setNumChildren(entityNodes_num);
//		}
//		catch(NullPointerException e){
//			System.out.println("Adding entity node to root.");
//			entityRoots.get(spByte).add(node);
//		}

		
	}	

	/**
	 * Add a root node, under which all other nodes will be stored. May have a type (spByte) that may be used to specify
	 * under which root additional nodes should be added.
	 * @param spByte Byte type (species) associated with the new root node.
	 * @param name String name to be given to the node
	 * @param info String information to be stored in the node, and which may be retrieved on update for displaying, etc
	 * @return EntityTree_Node root node already contained within the tree, or newly added if none existed.
	 */
	public EntityTree_Node addRootNode(byte spByte, String name, String info){
		if(entityRoots.containsKey(spByte)) return entityRoots.get(spByte);
		Label label = new Label(name, skin);
		EntityTree_Node node = new EntityTree_Node(label, name, info, spByte, null, true);
		entityRoots.put(spByte, node);
//		rootGroups.put(spByte, new ObjectMap<Byte, EntityTree_Node>());
		root.add(node);
		return node;
	}
	/**
	 * Add a group node (sub-root node) to the specified root node, indicated by spByte
	 * @param spByte Byte type of the root node that the group should be added to
	 * @param group Byte id to be assigned to the new node (if no node already exists under this id)
	 * @param info String to be stored in the node, which will be returned by update() and may be used to display information about this node
	 * @return Null if no root node with identifier spByte exists, or EntityTree_Node if a new group node is created, or already exists
	 */
	public EntityTree_Node addNodeGroup(byte spByte, byte group, String info){
//		System.out.println("Make group for species " + spByte + " group " + group);
		if(!entityRoots.containsKey(spByte)) return null;
		if(!rootGroups.containsKey(spByte)){
//			addRootNode(spByte, Byte.toString(spByte), info);
			rootGroups.put(spByte, new ObjectMap<Byte, EntityTree_Node>());
		}
		if(rootGroups.get(spByte).containsKey(group)) 
			return rootGroups.get(spByte).get(group);
		EntityTree_Node rootNode = entityRoots.get(spByte);
		String name = rootNode.getName() + " " + group;
		Label groupLabel = new Label(name, skin);
		String groupInfo = name + "/n" + "/t" + "Population: " + 0;
		EntityTree_Node newGroup = new EntityTree_Node(groupLabel, name, groupInfo, group, null, false);
		ObjectMap<Byte, EntityTree_Node> groups = rootGroups.get(spByte);
		groups.put(group, newGroup);
		rootGroups.put(spByte, groups);
		rootNode.add(newGroup);
//		System.out.println("new group "+ newGroup);
		return newGroup;
	}
	
	
	/**
	 * Cycle through entity array and create new nodes for each and assign to a root node and group node. If no root node matching
	 * the entity type (species) exists then a node will not be created for that entity.
	 * @param entities Array of Entitys to create nodes for.
	 */
	public void compareAndUpdateNodes(Array<Entity> entities){
		boolean change = false;

		nodeComparison = entityNodes.keys().toArray();
		for(Entity b : entities){
			if(!entityNodes.containsKey(b) && entityRoots.containsKey(b.getSubType())){
				entityNodes.put(b, addEntityNode(b.getSubType(), null, b.toString(), b));
			}
			else if(entityNodes.containsKey(b)){
				if(nodeComparison != null)
					if(nodeComparison.contains(b, false)){
						nodeComparison.removeValue(b, false);
						change = true;
					}
			}
		}
		if(!change) return;
//		if(nodeComparison != null){
//			for(Entity b : nodeComparison){
//				if(entityNodes.containsKey(b)){
//					entityNodes.get(b).removeNode(entityNodes.get(b));
//					entityNodes.remove(b);
//					entityNodes_num--;
//				}
//			}
//			root.setNumChildren(entityNodes_num);
//		}
        int n = entityNodes.size;
        int removed = 0;
        for(Entity b : entityNodes.keys()){
            if(!entities.contains(b, true)){
                removed++;
                entityNodes.get(b).removeNode(entityNodes.get(b));
                entityNodes.remove(b);
                entityNodes_num--;
                if(b.equals(selectedEntity)){
                    selectedEntity = null;
                    entitySelected = false;
                }

            }
        }
        if(entityNodes.size != n)
//             System.out.println("Finished removing redundant entity nodes. Entity num " + entityNodes.size + " original size " +
//              " removed " + removed);
        root.setNumChildren(entityNodes_num);
		
	}
	

	private boolean selected = true;
	
	/**
	 * Called to trigger the selection of a node by non standard means. I.e. without the user actually selecting the node within the tree
	 * Chooses the node associated with the Entity via the Tree's Selection, triggering the SelectNodeListener
	 * @param entity Entity for which the corresponding node must be selected
	 * @param select If Boolean is false will deselect all nodes, otherwise attempts the selection of associated node
	 */
	public void selectNodeByEntity(Entity entity, boolean select){
		
		if(!select || entity == null){
            if(selected) {
                selected = false;
                deselectNodes();
                tree.getSelection().clear();
                tree.getSelection().choose(root);
                root.expandTo();
                root.setExpanded(true);
            }
            return;
		}
		if(selectedEntity != null)
			if(entity.equals(selectedEntity))
				return;
			
		if(entityNodes.containsKey(entity)){
//			System.out.println("Found entity in entityNodes " + entity.getType());
			tree.getSelection().choose(entityNodes.get(entity));
		}
	}

	/**
	 * Called by SelectNodeListener. Will carry out the behaviour associated with selecting the specified node. Expanding table
	 * to the node and expanding children. If the node, or its children have associated entitys it will set them as tracked, so that
	 * they may be highlight in the simulation view
	 * @param node EntityTree_Node to be selected
	 */
	private void nodeSelected(EntityTree_Node node){
//		deselectNodes();
		Actor actor;
		if (node.equals(root)){
			deselectNodes();
			root.expandTo();
			root.setExpanded(true);
			selectedNode = node;
			actor = root.getActor();
			scrollPane.scrollToCenter(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            return;
		} else if(node.equals(selectedNode)){
			deselectNodes();
			node.expandTo();
			node.setExpanded(true);
			selectedNode = node;
			actor = selectedNode.getActor();
			scrollPane.scrollTo(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
			return;
		}
		deselectNodes();
		node.expandTo();
		node.setExpanded(true);
		if(node.hasEntity()) {
//			selectEntity(node.getEntity());
//			entityNodes.get(entity).expandTo();
			Entity entity = node.getEntity();
			entity.setTracked(true);
			entitySelected = true;
			selectedEntity = entity;
			selectedType = entity.getSubType();
			selectedGroup = entity.getTertiaryType();
			selectedInfo = selectedEntity.toString();
		}
		else {
			EntityTree_Node childNode;
			for(Node child : node.getChildren()){
//				for(Tree.Node childchild : child.getChildren()){
//					childNode = (EntityTree_Node) childchild;
//					if(childNode.hasEntity()) childNode.getEntity().setTracked(true);
//				}
//				childNode = (EntityTree_Node) child;
//				if(childNode.hasEntity()) childNode.getEntity().setTracked(true);
				setTracked(child, true);
			}
			if(!node.isRoot()){
				selectedGroup = node.getID();
				EntityTree_Node parent = (EntityTree_Node) node.getParent();
				selectedType = parent.getID();
//				selectedType = entityRoots.
				groupSelected = true;
				selectedNode = node;
			}
			else{
                groupSelected = true;
				selectedType = node.getID();
                Array<EntityTree_Node> array = node.getNodeChildren();
                byte j = 0;
                for(EntityTree_Node n : array){
                    if(n.getID() == j)
                        j++;
                }
				selectedGroup = j;
			}
			selectedInfo = node.getInfo();
		}
		if(!node.equals(root)) rootSelected = false;
		else rootSelected = true;
		selectedNode = node;
		actor = node.getActor();
		scrollPane.scrollToCenter(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		SelectedEntity.set(treeEntityType, selectedType, selectedGroup);
        gui.setSelectedInfoItem(this);
        selected = true;
	}
	
	/**
	 * Unselects all nodes in the tree. Removing associated entity tracked status
	 */
	private void deselectNodes(){
		tree.collapseAll();
//		root.setExpanded(true);
//		tree.getSelection().choose(root);
		EntityTree_Node node;
		for(Entity b : entityNodes.keys()){
			b.setTracked(false);
		}
//		for(Node treeNode : tree.getNodes()){
//			setTracked(treeNode, false);
////			node = (EntityTree_Node) treeNode;
////			if(node.hasEntity()) node.getEntity().setTracked(false);
//		}
		selectedInfo = " ";
		entitySelected = false;
		selectedEntity = null;
		groupSelected = false;
		selectedNode = null;
	}
	
	/**
	 * Sets entitys tracked status in all children of specified node recursively. I.e. setting for a root node will loop through children and their children
	 * @param node Tree.Node to set
	 * @param track Boolean indicating if the entitys should be set to tracked or not tracked
	 */
	private void setTracked(Node node, boolean track){
		if(node == null) return;
		EntityTree_Node n =  (EntityTree_Node) node;
		if(n.hasEntity()) n.getEntity().setTracked(track);
		for(Node child : n.getChildren()){
			setTracked(child, track);
		}
	}
	
	/**
	 * Updates the tree, checking that all the entitys have associated nodes and updating the selectedInfo to be returned by the tree
	 * @param entitys Entity array to compare against the current nodes
	 * @param updateNodes If the boolean is false, the Tree will not have any new nodes added, or removed to match entitys.
	 * @return
	 */
	public String update(Array<Entity> entitys, boolean updateNodes){
		if(updateNodes){
			compareAndUpdateNodes(entitys);
			if(groupSelected){
				selectedNode.setInfo(selectedNode.getName() + "\n" + "\t" + "Population: " + selectedNode.numChildren());
				selectedInfo = selectedNode.getInfo();
			}
		}
		if(entitySelected ){
            selectedInfo = selectedEntity.toString();
		}
        else if(!groupSelected)
            selectedInfo = "";
		return selectedInfo;
	}

    /**
     * Creates a scroll pane type actor for the table content
     * @param content Content to add to the scroll pane. Usually a table
     * @return Scroll pane actor
     */
	private Actor createScrollPane(Actor content){
    	Table scrollTable = new Table(skin);
//    	scrollTable.add("east");

		scrollTable.add(content).left().top();

		final ScrollPane scroll = new ScrollPane(scrollTable, skin);
    	InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
	
//		scroll.setSmoothScrolling(true);
		scroll.setScrollBarPositions(false, false);
		scroll.setForceScroll(false, true);
	    scroll.setFlickScroll(false);
	    scroll.setOverscroll(false, false);
	    scroll.setFadeScrollBars(false);
	    scroll.setCancelTouchFocus(true);
		scrollPane = scroll;
		
		scroll.addListener(new ClickListener(){
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
	    		gui.setScrollFocus(scroll);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				gui.setScrollFocus(null);
			}
		});
		
		return scroll;
	}

    /**
     * Listener for the selection of a tree node. Calls the method {@link #nodeSelected(EntityTree_Node) nodeSelected}.
     */
	class SelectNodeListener extends ChangeListener{
		Tree tree;
		
		SelectNodeListener(Tree tree){
			this.tree = tree;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			
				try{
					EntityTree_Node selected  = (EntityTree_Node) tree.getSelection().getLastSelected();
					nodeSelected(selected);
				}
				catch(IllegalStateException e){
				}
		}
		
	}
	


	private boolean clicked = false;

    /**
     * Method called when a button is selected. Changes the the {@link com.UKC_AICS.simulation.screen.controlutils.ControlState control state}
     * based on the input button string and sets the {@link com.UKC_AICS.simulation.screen.controlutils.SelectedEntity selected entity},
     * or indicates an entity should be deleted
     * @param button If "CHECKED" sets the control state to the checked state, for the explicit placement
     *               of entities (of the type of the entity selected) . If "REMOVE", then will mark the
     *               entity for {@link #markForDeletion(EntityTree_Node) deletion}.
     */
	public void buttonSelected(String button) {
		tree.getSelection().choose(selectedNode);
		if(button == "CHECKED"){
			ControlState.changeState(buttons.get(button).isChecked(), ControlState.State.PLACEMENT);
			if(selectedNode.equals(root))
				SelectedEntity.set(false);
			else {
                SelectedEntity.set(treeEntityType, selectedType, selectedGroup);
            }
		}
		if(button == "REMOVE"){
			markForDeletion(selectedNode);
//			removeEntityNodes(selectedNode);
//			selectedNode.remove();
		}
		clicked = false;


	}


    /**
     * Listener for the windows buttons. Calls the {@link #buttonSelected(String)} method on click,
     * or triggers the hovering behaviour {@link com.UKC_AICS.simulation.screen.controlutils.HoverListener} of the listeners
     */
	class SelectOptionsListener extends ClickListener {
		private String btn;
		private String btnInfo;
		public SelectOptionsListener(String btn, String buttonInfo){
			this.btn = btn;
			this.btnInfo = buttonInfo;
		}
		public void clicked (InputEvent event, float x, float y) {
			clicked = true;
			buttonSelected(btn);
		}
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			for(HoverListener listener : hoverListeners){
				listener.hover(event, x, y, btnInfo);
			}
		}
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
			for(HoverListener listener : hoverListeners){
				listener.unhover(event);
			}
		}
	}

    /**
     * Mark a given node for deletion. Results in any entity assigned to the node being deleted, or entities assigned
     * to all child nodes being removed. Calls {@link com.UKC_AICS.simulation.entity.Entity#delete()} on any entities
     * found
     * @param node Node to mark for deletion.
     */
	private void markForDeletion(EntityTree_Node node){
		if(node.hasEntity()){
			node.getEntity().delete();
			return;
		}
		else if(node.getChildren().size>0){
			for(Node n : node.getChildren()){
				markForDeletion((EntityTree_Node)n);
			}
		}
	}

	public void clearAll(){
		tree.clear();
		
		tree.add(root);
		root.setExpanded(true);
		tree.getSelection().clear();
		entityNodes.clear();
		entityRoots.clear();
		rootGroups.clear();
	}
	

	
}
