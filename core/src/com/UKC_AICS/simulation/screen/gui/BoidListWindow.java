package com.UKC_AICS.simulation.screen.gui;

import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.gui.controlutils.DialogueWindowHandler;
import com.UKC_AICS.simulation.gui.controlutils.SettingsEditor;
import com.UKC_AICS.simulation.gui.controlutils.TreeOptionsInterface;
import com.UKC_AICS.simulation.gui.controlutils.TreeOptionsHandler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.org.apache.bcel.internal.generic.IFNE;



public class BoidListWindow extends Table implements TreeOptionsInterface {
	//Boids
	private final ObjectMap<Byte, BoidTree_Node> boidRoots = new ObjectMap<Byte, BoidTree_Node>();
	private final ObjectMap<Byte, ObjectMap<Byte, BoidTree_Node>> rootGroups = new ObjectMap<Byte, ObjectMap<Byte, BoidTree_Node>> ();
	private final ObjectMap<Boid, BoidTree_Node> boidNodes = new ObjectMap<Boid, BoidTree_Node>();
	
	private Tree tree;
	private BoidTree_Node root;
	
	private Skin skin;
	private SimScreenGUI gui;
	
	private String selectedInfo = "";
	private Boid selectedBoid;
	private boolean boidSelected = false;
	private BoidTree_Node selectedNode;
	private boolean groupSelected;
	private final ObjectMap<Byte, String> speciesInfo = new ObjectMap<Byte, String>();

	
	private byte selectedType = 0;
	private byte selectedGroup = 0;
	
	private static final Array<TreeOptionsHandler> listeners = new Array<TreeOptionsHandler>();
	
	public BoidListWindow(String title, Skin skin, SimScreenGUI gui) {
		super(skin);
		this.skin = skin;
		this.gui = gui;
		this.tree = new Tree(skin);
		
		root = new BoidTree_Node(new Label(title, skin));
		tree.add(root);
		root.setExpanded(true);
		this.addListener(new SelectNodeListener(tree));
//		root.setSelectable(false);
		tree.getSelection().choose(root);
		
		//Add components to the table
		this.add(new Label("title", skin));
		this.row();
		this.add(createScrollPane(tree)).fill().expand();
		this.row();
		this.add(buttons());
		this.pack();
		//Listener Test
		TreeOptionsHandler listener = new SettingsEditor();
//		registerListener(listener);
	}
	
	private HorizontalGroup buttons(){
		HorizontalGroup btnGrp = new HorizontalGroup();
		
		TextButton btn = new TextButton(ButtonType.ADD.getName(), skin);
		btn.addListener(new SelectOptionsListener(ButtonType.ADD));
		btnGrp.addActor(btn);
		btn = new TextButton(ButtonType.REMOVE.getName(), skin);
		btn.addListener(new SelectOptionsListener(ButtonType.REMOVE));
		btnGrp.addActor(btn);
		return btnGrp;
	}

	/**
	 * Adds a node to the tree under the specified species type byte node, and group node. If no group node exists matching
	 * the boids group then addNodeGroup(byte spByte, byte group, String info) is called
	 * @param spByte Byte of the species to associate this boid with
	 * @param name String name to give the node
	 * @param info String stored in the node that may be displayed on selection of the node
	 * @param boid Boid to be associated with the created node
	 * @return Null if a root node associated with spByte does not exist, otherwise returns BoidTree_Node that is added
	 * as a child of group and species.
	 */
	public BoidTree_Node addBoidNode(byte spByte, String name, String info, Boid boid){
		Label label;
		BoidTree_Node node;
		
		if(!boidRoots.containsKey(spByte)){
			System.out.println("Missing rootNode");
			return null;
		}
		if(!rootGroups.containsKey(spByte) || !rootGroups.get(spByte).containsKey(boid.group)){
			addNodeGroup(spByte, boid.group, info);
		}
		label = new Label((boidRoots.get(spByte).getName() + " " + boidRoots.get(spByte).getChildren().size+1), skin);
		node = new BoidTree_Node(label, name, info, spByte, boid, false);
//		boidRoots.get(spByte).add(node);
//		System.out.println("node" + rootGroups.get(spByte).get(boid.group));
		try{
			rootGroups.get(spByte).get(boid.group).add(node);
		}
		catch(NullPointerException e){
			boidRoots.get(spByte).add(node);
		}
		return node;
		
	}	
	
	/**
	 * Add a root node, under which all other nodes will be stored. May have a type (spByte) that may be used to specify
	 * under which root additional nodes should be added.
	 * @param spByte Byte type (species) associated with the new root node.
	 * @param name String name to be given to the node
	 * @param info String information to be stored in the node, and which may be retrieved on update for displaying, etc
	 * @return BoidTree_Node root node already contained within the tree, or newly added if none existed.
	 */
	public BoidTree_Node addRootNode(byte spByte, String name, String info){
		if(boidRoots.containsKey(spByte)) return boidRoots.get(spByte);
		Label label = new Label(name, skin);
		BoidTree_Node node = new BoidTree_Node(label, name, info, spByte, null, true);
		boidRoots.put(spByte, node);
//		rootGroups.put(spByte, new ObjectMap<Byte, BoidTree_Node>());
		root.add(node);
		return node;
	}
	/**
	 * Add a group node (sub-root node) to the specified root node, indicated by spByte
	 * @param spByte Byte type of the root node that the group should be added to
	 * @param group Byte id to be assigned to the new node (if no node already exists under this id)
	 * @param info String to be stored in the node, which will be returned by update() and may be used to display information about this node
	 * @return Null if no root node with identifier spByte exists, or BoidTree_Node if a new group node is created, or already exists
	 */
	public BoidTree_Node addNodeGroup(byte spByte, byte group, String info){
		
		if(!boidRoots.containsKey(spByte)) return null;
		if(!rootGroups.containsKey(spByte)){
//			addRootNode(spByte, Byte.toString(spByte), info);
			rootGroups.put(spByte, new ObjectMap<Byte, BoidTree_Node>());
		}
		else if(rootGroups.get(spByte).containsKey(group)) 
			return rootGroups.get(spByte).get(group);
		BoidTree_Node rootNode = boidRoots.get(spByte);
		String name = rootNode.getName() + " " + group;
		Label groupLabel = new Label(name, skin);
		String groupInfo = name + "/n" + "/t" + "Population: " + 0;
		BoidTree_Node newGroup = new BoidTree_Node(groupLabel, name, groupInfo, group, null, false);
		ObjectMap<Byte, BoidTree_Node> groups = rootGroups.get(spByte);
		groups.put(group, newGroup);
		rootGroups.put(spByte, groups);
		rootNode.add(newGroup);
//		System.out.println("new Group " + newGroup);
		return newGroup;
	}
	
	/**
	 * Cycle through boid array and create new nodes for each and assign to a root node and group node. If no root node matching
	 * the boid type (species) exists then a node will not be created for that boid.
	 * @param boids Array of Boids to create nodes for.
	 */
	public void compareAndAddNodes(Array<Boid> boids){
		for(Boid b : boids){
			if(!boidNodes.containsKey(b) && boidRoots.containsKey(b.getSpecies())){
				boidNodes.put(b, addBoidNode(b.getSpecies(), "", b.toString(), b));
			}
		}
	}
	
	/**
	 * Removes any nodes whos associated boid is not contained within the comparison Boid array
	 * @param boids Array of boids to compare against the trees nodes and their associated boids
	 */
	public void compareAndRemoveNodes(Array<Boid> boids){
		byte species;
		for(Boid b : boidNodes.keys()){
			
			if(!boids.contains(b, false)){
				species = b.getSpecies();
				tree.remove(boidNodes.get(b));				
				boidNodes.remove(b);
			}
		}
	}
	
	/**
	 * Called to trigger the selection of a node by non standard means. I.e. without the user actually selecting the node within the tree
	 * Chooses the node associated with the Boid via the Tree's Selection, triggering the SelectNodeListener
	 * @param boid Boid for which the corresponding node must be selected
	 * @param select If Boolean is false will deselect all nodes, otherwise attempts the selection of associated node
	 */
	public void selectNodeByBoid(Boid boid, boolean select){
		if(!select || boid == null){ 
			deselectNodes(); 
			return;
		}
			
		if(selectedBoid != null){
			if(selectedBoid.equals(boid)) 
				return;
		}
//			if(selectedBoid.equals(boid)) return;
		if(boidNodes.containsKey(boid)){
			tree.collapseAll();
			tree.getSelection().choose(boidNodes.get(boid));
		}
	}
	
	/**
	 * Called by SelectNodeListener. Will carry out the behaviour associated with selecting the specified node. Expanding table
	 * to the node and expanding children. If the node, or its children have associated boids it will set them as tracked, so that
	 * they may be highlight in the simulation view
	 * @param node BoidTree_Node to be selected
	 */
	private void nodeSelected(BoidTree_Node node){
		deselectNodes();
		if(node.equals(root)) return;
		node.expandTo();
		node.setExpanded(true);
		if(node.hasBoid()) {
//			selectBoid(node.getBoid());
//			boidNodes.get(boid).expandTo();
			Boid boid = node.getBoid();
			boid.setTracked(true);
			boidSelected = true;
			selectedBoid = boid;
		}
		else {
			BoidTree_Node childNode;
			for(Node child : node.getChildren()){
//				for(Tree.Node childchild : child.getChildren()){
//					childNode = (BoidTree_Node) childchild;
//					if(childNode.hasBoid()) childNode.getBoid().setTracked(true);
//				}
//				childNode = (BoidTree_Node) child;
//				if(childNode.hasBoid()) childNode.getBoid().setTracked(true);
				setTracked(child, true);
			}
			if(!node.isRoot()){
				groupSelected = true;
				selectedNode = node;
			}
				
			selectedInfo = node.getInfo();
		}
	}
	
	/**
	 * Unselects all nodes in the tree. Removing associated boid tracked status
	 */
	private void deselectNodes(){
//		tree.collapseAll();
		BoidTree_Node node;
		for(Boid b : boidNodes.keys()){
			b.setTracked(false);
		}
//		for(Node treeNode : tree.getNodes()){
//			setTracked(treeNode, false);
////			node = (BoidTree_Node) treeNode;
////			if(node.hasBoid()) node.getBoid().setTracked(false);
//		}
		selectedInfo = " ";
		boidSelected = false;
		selectedBoid = null;
		groupSelected = false;
	}
	
	/**
	 * Sets boids tracked status in all children of specified node recursively. I.e. setting for a root node will loop through children and their children
	 * @param node Tree.Node to set
	 * @param track Boolean indicating if the boids should be set to tracked or not tracked
	 */
	private void setTracked(Node node, boolean track){
		if(node == null) return;
		BoidTree_Node n =  (BoidTree_Node) node;
		if(n.hasBoid()) n.getBoid().setTracked(track);
		for(Node child : n.getChildren()){
			setTracked(child, track);
		}
	}
	
	/**
	 * Updates the tree, checking that all the boids have associated nodes and updating the selectedInfo to be returned by the tree
	 * @param boids Boid array to compare against the current nodes
	 * @param updateNodes If the boolean is false, the Tree will not have any new nodes added, or removed to match boids.
	 * @return
	 */
	public String update(Array<Boid> boids, boolean updateNodes){
		if(updateNodes){
			compareAndRemoveNodes(boids);
			compareAndAddNodes(boids);
			if(boidSelected)
				selectedInfo = selectedBoid.toString();
			if(groupSelected){
				selectedNode.setInfo(selectedNode.getName() + "/n" + "/t" + "Population: " + selectedNode.getChildren().size);
				selectedInfo = selectedNode.getInfo();
			}
		}

//		System.out.println(selectedInfo);
//		System.out.println("selected " + selectedInfo);
		return selectedInfo;
	}
	
	private Actor createScrollPane(Actor content){
    	Table scrollTable = new Table(skin);
//    	scrollTable.add("east");

		scrollTable.add(content).left().top();

		ScrollPane scroll = new ScrollPane(scrollTable, skin);
    	InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};

		scroll.setSmoothScrolling(true);
		scroll.setScrollBarPositions(true, false);
//		scroll.updateVisualScroll();
//		scroll.setForceScroll(true, true);
//		System.out.println(scroll.getScrollY());
//		scroll.scrollTo(0,0,100,100);
//		System.out.println(scroll.getScrollY());
//		scroll.updateVisualScroll();
		scroll.setFadeScrollBars(false);
		return scroll;
	}

	
	class SelectNodeListener extends ChangeListener{
		Tree tree;
		
		SelectNodeListener(Tree tree){
			this.tree = tree;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
			try{
				BoidTree_Node selected  = (BoidTree_Node) tree.getSelection().getLastSelected();
				nodeSelected(selected);
			}
			catch(IllegalStateException e){
				System.out.println("Same boid was probably selected twice via viewport. For some reason this causes an exception in tree Selection");
			}
//			
		}
		
	}


	@Override
	public void registerListener(TreeOptionsHandler listener) {
		listeners.add(listener);
	}

	@Override
	public void buttonSelected(ButtonType button) {
//		System.out.println("Selected" + button.toString());
		for(TreeOptionsHandler listener : listeners){
			if(button == ButtonType.ADD){
				 listener.onAdd(selectedType, selectedGroup, selectedBoid);
			}
			if(button == ButtonType.REMOVE){
				 listener.onRemove(selectedType, selectedGroup, selectedBoid);
			}
		}
		
		

	}

	class SelectOptionsListener extends ClickListener {
		private ButtonType btn;
		public SelectOptionsListener(ButtonType btn){
			this.btn = btn;
		}
		public void clicked (InputEvent event, float x, float y) {
//			System.out.print("clicked");
			buttonSelected(btn);
		}
	}

	
}
