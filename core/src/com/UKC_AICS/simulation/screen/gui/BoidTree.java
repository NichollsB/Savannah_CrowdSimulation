package com.UKC_AICS.simulation.screen.gui;

import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.org.apache.bcel.internal.generic.IFNE;



public class BoidTree extends Tree {
	//Boids
	private final ObjectMap<Byte, BoidTree_Node> boidRoots = new ObjectMap<Byte, BoidTree_Node>();
	private final ObjectMap<Boid, BoidTree_Node> boidNodes = new ObjectMap<Boid, BoidTree_Node>();
	
	//Objects
	private final ObjectMap<Byte, BoidTree_Node> objectRoots = new ObjectMap<Byte, BoidTree_Node>();
	private final ObjectMap<Boid, BoidTree_Node> objectNodes = new ObjectMap<Boid, BoidTree_Node>();
	
	private Skin skin;
	private SimScreenGUI gui;
	
	private String selectedInfo = "";
	private Boid selectedBoid;
	private boolean boidSelected = false;
	private final ObjectMap<Byte, String> speciesInfo = new ObjectMap<Byte, String>();

	private static final ObjectMap<String, Tree.Node> rootNodes = new ObjectMap<String, Tree.Node>(){{
		put("Species", null);
		put("Boids", null);
	}};
	
	public BoidTree(Skin skin, SimScreenGUI gui) {
		super(skin);
		this.skin = skin;
		this.gui = gui;
		
		this.addListener(new SelectBoidListener(this));

		for(String s : rootNodes.keys()){
			rootNodes.put(s, new Tree.Node(new Label(s, skin)));
		}

	}
	public BoidTree_Node addObjectNode(byte spByte, String name, String info, Boid boid){
		Label label;
		BoidTree_Node node;
		if(!boidRoots.containsKey(spByte)){
			label = new Label(name, skin);
			node = new BoidTree_Node(label, name, info, spByte, true, boid);
			objectRoots.put(spByte, node);
			this.add(node);
			return node;
		}
		else {
			label = new Label((boidRoots.get(spByte).getName() + " " + boidRoots.get(spByte).getChildren().size+1), skin);
			node = new BoidTree_Node(label, name, info, spByte, false, boid);
			objectRoots.get(spByte).add(node);
//			boidNodes.put(key, value)
			return node;
		}
//		return null;
	}	
	
	public BoidTree_Node addBoidNode(byte spByte, String name, String info, Boid boid){
		Label label;
		BoidTree_Node node;
		if(!boidRoots.containsKey(spByte)){
			label = new Label(name, skin);
			node = new BoidTree_Node(label, name, info, spByte, true, boid);
			boidRoots.put(spByte, node);
			this.add(node);
			return node;
		}
		else {
			label = new Label((boidRoots.get(spByte).getName() + " " + boidRoots.get(spByte).getChildren().size+1), skin);
			node = new BoidTree_Node(label, name, info, spByte, false, boid);
			boidRoots.get(spByte).add(node);
//			boidNodes.put(key, value)
			return node;
		}
//		return null;
	}	
	
	public void addNodes(Array<Boid> boids){
		for(Boid b : boids){
			if(!boidNodes.containsKey(b) && boidRoots.containsKey(b.getSpecies())){
				boidNodes.put(b, addBoidNode(b.getSpecies(), "", b.toString(), b));
			}
		}
	}
	
	public BoidTree_Node createNode(byte spByte, String name, String info, boolean hasChildren, Array<Boid> boids){
		
		Label label = new Label(name, skin);
		BoidTree_Node node;

		node = new BoidTree_Node(label, name, info, spByte, hasChildren, null);
//		Node spNode = new Node(label);
		if(hasChildren){
			speciesInfo.put(spByte, info);
			boidRoots.put(spByte, node);
		}
		this.add(node);
		int i = 0;
		for(Boid b : boids){
			BoidTree_Node childNode = createNode(spByte, (name + " " + i), b.toString(), false, null);
			node.add(childNode);
			boidNodes.put(b, childNode);
			i++;
		}
		return node;
	}
	
	public void selectSpecies(byte spByte){
		deselectBoids();
		for(Boid b : boidNodes.keys()){
			if(b.getSpecies() == spByte){ 
//				System.out.print(b.getSpecies() + " ");
				b.setTracked(true);
				boidNodes.get(b).expandTo();
			}
		}
		//System.out.println("Select species " + speciesNodes.containsKey(spByte));
		if(boidRoots.containsKey(spByte)) selectedInfo = boidRoots.get(spByte).toString();//speciesInfo.get(spByte);
	}
	
	public void selectBoid(Boid boid){
		deselectBoids();
//		if(boids.indexOf(boid, false) != -1){
		
		if(boidNodes.containsKey(boid)){
			boidNodes.get(boid).expandTo();
			boid.setTracked(true);
			boidSelected = true;
			selectedBoid = boid;
//			selectedInfo = boid.toString();
		}
	}
	
	public void selectBoid_Node(Boid boid, boolean select){
		if(!select || boid == null){ 
//			deselectBoids(); 
			return;
		}
			
		if(selectedBoid != null){
			if(selectedBoid.equals(boid)) return;
		}
//			if(selectedBoid.equals(boid)) return;
		if(boidNodes.containsKey(boid)){
				this.getSelection().choose(boidNodes.get(boid));
		}
	}
	
	public void deselectBoids(){
		this.collapseAll();
		for(Boid b : boidNodes.keys()){
			b.setTracked(false);
			selectedInfo = " ";
			boidSelected = false;
			selectedBoid = null;
		}
	}
	
	public String update(Array<Boid> boids, boolean updateNodes){
		if(updateNodes)
			addNodes(boids);
		
//		for(Boid b : boids){
//			if(!boidNodes.containsKey(b)){
////				createNode(b.getSpecies(), null, b.toString(), false, null);
//				
//			}
//		}
//		
//		
		if(boidSelected)
			selectedInfo = selectedBoid.toString();
//		System.out.println(selectedInfo);
		System.out.println("selected " + selectedInfo);
		return selectedInfo;
	}
	
	

	
	class SelectBoidListener extends ChangeListener{
		BoidTree tree;
		
		SelectBoidListener(BoidTree tree){
			this.tree = tree;
		}

		@Override
		public void changed(ChangeEvent event, Actor actor) {
//			System.out.println("tree selection event");
//			tree.selected();
			try{
				BoidTree_Node selected  = (BoidTree_Node) tree.getSelection().getLastSelected();
	//			System.out.println("Select change" + selected.getName());
				if(selected.isSpecies()) selectSpecies(selected.getSpecies());
				else selectBoid(selected.getBoid());
			}
			catch(IllegalStateException e){
				System.out.println("Same boid was probably selected twice via viewport. For some reason this causes an exception in tree Selection");
			}
//			
		}
		
	}

	
}
