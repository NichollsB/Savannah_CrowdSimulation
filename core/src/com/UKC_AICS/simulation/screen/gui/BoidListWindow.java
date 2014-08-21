package com.UKC_AICS.simulation.screen.gui;

import java.util.HashMap;

import com.UKC_AICS.simulation.gui.controlutils.ControlState;
import com.UKC_AICS.simulation.gui.controlutils.DialogueWindowHandler;
import com.UKC_AICS.simulation.gui.controlutils.HoverListener;
import com.UKC_AICS.simulation.gui.controlutils.SelectedEntity;
import com.UKC_AICS.simulation.gui.controlutils.SettingsEditor;
import com.UKC_AICS.simulation.gui.controlutils.TreeOptionsInterface;
import com.UKC_AICS.simulation.gui.controlutils.TreeOptionsListener;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import com.UKC_AICS.simulation.entity.*;


public class BoidListWindow extends Table implements TreeOptionsInterface {
	//Boids
	private final ObjectMap<Byte, BoidTree_Node> boidRoots = new ObjectMap<Byte, BoidTree_Node>();
	private final ObjectMap<Byte, ObjectMap<Byte, BoidTree_Node>> rootGroups = new ObjectMap<Byte, ObjectMap<Byte, BoidTree_Node>> ();
	private final ObjectMap<Entity, BoidTree_Node> boidNodes = new ObjectMap<Entity, BoidTree_Node>();
	
	private Tree tree;
	private BoidTree_Node root;
	private BoidTree_Node selectedNode;
	private boolean rootSelected = true;
	
	private Skin skin;
	private SimScreenGUI gui;
	
	private String selectedInfo = "";
	private Entity selectedBoid;
	private boolean boidSelected = false;
	private boolean groupSelected;

	
	private byte selectedType = 0;
	private byte selectedGroup = 0;
	
	private static final Array<TreeOptionsListener> listeners = new Array<TreeOptionsListener>();
	private Array<HoverListener> hoverListeners = new Array<HoverListener>();
	
	private Array<Entity> nodeComparison = new Array<Entity>();
	
	private int boidNodes_num = 0;
	
	private ObjectMap<ButtonType, Button> buttons;
	private ObjectMap<Button, String> buttonsInfo;
	
	private boolean fixedType = false;
	private byte fixedTypeValue = 0;
	
	private ScrollPane scrollPane;
	private Stage stage;

    private final ObjectMap<Byte, Image> nodeImages = new ObjectMap<Byte, Image>();
	
	public void initButtons(Skin skin){
		final Skin s = skin;
		buttons = new ObjectMap<ButtonType, Button>(){{
			put(ButtonType.REMOVE, new TextButton(ButtonType.REMOVE.getName(), s));
			TextButton.TextButtonStyle style = new TextButton("", s).getStyle();
			TextButton.TextButtonStyle checkedStyle = new TextButton.TextButtonStyle(style.up, style.down, style.down, style.font);
			put(ButtonType.CHECKED, new TextButton(ButtonType.CHECKED.getName(), checkedStyle));
		}};
		buttonsInfo = new ObjectMap<Button, String>(){{
			put(buttons.get(ButtonType.REMOVE), "Remove selected item");
			put(buttons.get(ButtonType.CHECKED), "Place new item of selected type - click to place on map");
		}};
		
	}

//    public
	public BoidListWindow(String title, Skin skin, SimScreenGUI gui, boolean fixedType, byte fixedTypeValue) {
		super(skin);
		
		this.fixedType = fixedType;
		this.fixedTypeValue = (fixedType) ? fixedTypeValue : -1;
		
		this.skin = skin;
		initButtons(skin);
	
		
		this.gui = gui;
		this.tree = new Tree(skin);
		createScrollPane(tree);
		hoverListeners.add(gui);
		
		root = new BoidTree_Node(new Label(title, skin), title);
		
		tree.add(root);
		root.setExpanded(true);
		tree.addListener(new SelectNodeListener(tree));
//		root.setSelectable(false);
		tree.getSelection().choose(root);
		selectedNode = root;
		tree.setName("BoidTree");
		//Add components to the table
		this.add(new Label(title, skin));
		this.row();
		
		this.add(scrollPane).fill().expand();
		this.row();
		this.add(buttons());
		this.pack();
		//Listener Test
		TreeOptionsListener listener = new SettingsEditor();
//		registerListener(listener);
        tree.getSelection().clear();
	}
	public void resize(){
		this.pack();
	}
	
	private Table buttons(){
		Table btnGrp = new Table();

		TextButton btn ;
		btn = (TextButton) buttons.get(ButtonType.REMOVE);
		btn.addListener(new SelectOptionsListener(ButtonType.REMOVE, buttonsInfo.get(btn)));
		btnGrp.add(btn).expandX().fillX();
		btnGrp.row();
		btn = (TextButton) buttons.get(ButtonType.CHECKED);
		btn.addListener(new SelectOptionsListener(ButtonType.CHECKED, buttonsInfo.get(btn)));
		btnGrp.add(btn).expandX().fillX();
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
	public BoidTree_Node addBoidNode(byte spByte, String name, String info, Entity boid){
		Label label;
		BoidTree_Node node, boidNode;
		if(boid == null) return null;
		if(!boidRoots.containsKey(spByte)){
			System.out.println("Missing rootNode");
			return null;
		}
		node = boidRoots.get(spByte);
		String boidName = (boidRoots.get(spByte).getName() + " " + boid.getTertiaryType());
		label = new Label(boidName, skin);
		boidNode = new BoidTree_Node(label, boidName, info, spByte, boid, false);
		node.addNode(boidNode, boid.getTertiaryType());
//		boidNode.setParent(node);
//		boidNode.setParent(node);
		boidNodes_num++;
		root.setNumChildren(boidNodes_num);
//			boidNodes.put()
//		System.out.println("boidNode added." +  " type " + boidNode.getName() + " subtype " + boid.getTertiaryType());
		return boidNode;
//		if(!rootGroups.containsKey(spByte) || !rootGroups.get(spByte).containsKey(boid.getType())){
//			addNodeGroup(spByte, boid.getTertiaryType(), info);
//			System.out.println("GROUP TYPE " + boid.getTertiaryType());
//		}
//		String boidName = (boidRoots.get(spByte).getName() + " " + boid.getTertiaryType());
//		label = new Label(boidName, skin);
//		node = new BoidTree_Node(label, boidName, info, spByte, boid, false);
//		boidNode = node;
//		try{
//			rootGroups.get(spByte).get(boid.getTertiaryType()).add(node);
//			rootGroups.get(spByte).get(boid.getTertiaryType()).incrementNumChildren(1);
//			rootGroups.get(spByte).get(boid.getTertiaryType()).setText();
//			node = boidRoots.get(spByte);
//			node.incrementNumChildren(1);
//			node.setText();
//			boidNodes_num++;
//			root.setNumChildren(boidNodes_num);
//		}
//		catch(NullPointerException e){
//			System.out.println("Adding boid node to root.");
//			boidRoots.get(spByte).add(node);
//		}

		
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
//		System.out.println("Make group for species " + spByte + " group " + group);
		if(!boidRoots.containsKey(spByte)) return null;
		if(!rootGroups.containsKey(spByte)){
//			addRootNode(spByte, Byte.toString(spByte), info);
			rootGroups.put(spByte, new ObjectMap<Byte, BoidTree_Node>());
		}
		if(rootGroups.get(spByte).containsKey(group)) 
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
//		System.out.println("new group "+ newGroup);
		return newGroup;
	}
	
	
	/**
	 * Cycle through boid array and create new nodes for each and assign to a root node and group node. If no root node matching
	 * the boid type (species) exists then a node will not be created for that boid.
	 * @param boids Array of Boids to create nodes for.
	 */
	public void compareAndUpdateNodes(Array<Entity> boids){
		boolean change = false;

		nodeComparison = boidNodes.keys().toArray();
		for(Entity b : boids){
			if(!boidNodes.containsKey(b) && boidRoots.containsKey(b.getSubType())){
				boidNodes.put(b, addBoidNode(b.getSubType(), null, b.toString(), b));
			}
			else if(boidNodes.containsKey(b)){
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
//				if(boidNodes.containsKey(b)){
//					boidNodes.get(b).removeNode(boidNodes.get(b));
//					boidNodes.remove(b);
//					boidNodes_num--;
//				}
//			}
//			root.setNumChildren(boidNodes_num);
//		}
        int n = boidNodes.size;
        int removed = 0;
        for(Entity b : boidNodes.keys()){
            if(!boids.contains(b, true)){
                removed++;
                boidNodes.get(b).removeNode(boidNodes.get(b));
                boidNodes.remove(b);
                boidNodes_num--;
            }
        }
        if(boidNodes.size != n)
//             System.out.println("Finished removing redundant entity nodes. Entity num " + boidNodes.size + " original size " +
//              " removed " + removed);
        root.setNumChildren(boidNodes_num);
		
	}
	

	
	
	/**
	 * Called to trigger the selection of a node by non standard means. I.e. without the user actually selecting the node within the tree
	 * Chooses the node associated with the Boid via the Tree's Selection, triggering the SelectNodeListener
	 * @param boid Boid for which the corresponding node must be selected
	 * @param select If Boolean is false will deselect all nodes, otherwise attempts the selection of associated node
	 */
	public void selectNodeByBoid(Entity boid, boolean select){
		
		if(!select || boid == null){ 
			deselectNodes(); 
//			tree.getSelection().choose(root);
            tree.getSelection().clear();
			root.expandTo();
			root.setExpanded(true);
			return;
		}
		if(selectedBoid != null)
			if(boid.equals(selectedBoid))
				return;
			
		if(boidNodes.containsKey(boid)){
//			System.out.println("Found entity in boidNodes " + boid.getType());
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
		if(node.hasBoid()) {
//			selectBoid(node.getBoid());
//			boidNodes.get(boid).expandTo();
			Entity boid = node.getBoid();
			boid.setTracked(true);
			boidSelected = true;
			selectedBoid = boid;
			selectedType = boid.getSubType();
			selectedGroup = boid.getTertiaryType();
			selectedInfo = selectedBoid.toString();
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
				selectedGroup = node.getID();
				BoidTree_Node parent = (BoidTree_Node) node.getParent();
				selectedType = parent.getID();
//				selectedType = boidRoots.
				groupSelected = true;
				selectedNode = node;
			}
			else{
				selectedType = node.getID();
				selectedGroup = -1;
			}
			selectedInfo = node.getInfo();
		}
		if(!node.equals(root)) rootSelected = false;
		else rootSelected = true;
		selectedNode = node;
		actor = node.getActor();
		scrollPane.scrollToCenter(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		SelectedEntity.set((byte)1, selectedType, selectedGroup);
        gui.setSelctedInfoItem(this);
	}
	
	/**
	 * Unselects all nodes in the tree. Removing associated boid tracked status
	 */
	private void deselectNodes(){
		tree.collapseAll();
//		root.setExpanded(true);
//		tree.getSelection().choose(root);
		BoidTree_Node node;
		for(Entity b : boidNodes.keys()){
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
		selectedNode = null;
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
	public String update(Array<Entity> boids, boolean updateNodes){
		if(updateNodes){
			compareAndUpdateNodes(boids);
			if(groupSelected){
				selectedNode.setInfo(selectedNode.getName() + "/n" + "/t" + "Population: " + selectedNode.getChildren().size);
				selectedInfo = selectedNode.getInfo();
			}
		}
		if(boidSelected){
			selectedInfo = selectedBoid.toString();
			
		}
		return selectedInfo;
	}
	
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
				}
		}
		
	}
	

	@Override
	public void registerListener(TreeOptionsListener listener) {
		listeners.add(listener);
	}

	private boolean clicked = false;
	@Override
	public void buttonSelected(ButtonType button) {
		tree.getSelection().choose(selectedNode);
		if(button == ButtonType.CHECKED){
			ControlState.changeState(buttons.get(button).isChecked(), ControlState.State.PLACEMENT);
			if(selectedNode.equals(root))
				SelectedEntity.set(false);
			else
				SelectedEntity.set((byte)1, selectedType, selectedGroup);
		}
		if(button == ButtonType.REMOVE){
			markForDeletion(selectedNode);
//			removeBoidNodes(selectedNode);
//			selectedNode.remove();
		}
		clicked = false;
		

	}
//	@Override
//	public void showButtonHelper(boolean showHelper, String helper) {
//		// TODO Auto-generated method stub
//		
//	}
	
	

	class SelectOptionsListener extends ClickListener {
		private ButtonType btn;
		private String btnInfo;
		public SelectOptionsListener(ButtonType btn, String buttonInfo){
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
	
	private void markForDeletion(BoidTree_Node node){
		if(node.hasBoid()){
			node.getBoid().delete();
			return;
		}
		else if(node.getChildren().size>0){
			for(Node n : node.getChildren()){
				markForDeletion((BoidTree_Node)n);
			}
		}
	}

	
	

	
}
