package com.UKC_AICS.simulation.managers;

import EvolutionaryAlgorithm.EA2;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.entity.states.Drink;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.entity.states.Thirsty;
import com.UKC_AICS.simulation.entity.states.carnivore.ApproachCorpse;
import com.UKC_AICS.simulation.entity.states.carnivore.CarnDefault;
import com.UKC_AICS.simulation.entity.states.carnivore.CarnReproduce;
import com.UKC_AICS.simulation.entity.states.carnivore.Eat;
import com.UKC_AICS.simulation.entity.states.carnivore.GoForKill;
import com.UKC_AICS.simulation.entity.states.carnivore.Hunt;
import com.UKC_AICS.simulation.entity.states.carnivore.Stalk;
import com.UKC_AICS.simulation.entity.states.herbivore.EatGrass;
import com.UKC_AICS.simulation.entity.states.herbivore.HerbDefault;
import com.UKC_AICS.simulation.entity.states.herbivore.Hungry;
import com.UKC_AICS.simulation.entity.states.herbivore.Panic;
import com.UKC_AICS.simulation.entity.states.herbivore.Reproduce;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Emily.
 */
public class StateMachine {

    private static BoidManager boidManager;
    private EA2 ea;
    public static Map<String, Behaviour> behaviours = new HashMap<String, Behaviour>();

    public HashMap<Boid,Stack<State>> boidStates;

    /**
     * Stack Based Finite State Machine
     *
     * takes a reference to the boidmanager to be able to access and make adjustments to the lists of boids.
     *
     */
    public StateMachine(BoidManager bm, EA2 ea) {
//        this.owner = owner;
//        this.currentState = initialState;
    	this.ea=ea;
        this.boidManager = bm;
        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());

        behaviours.put("attractor", new Attractor());
        behaviours.put("repeller", new Repeller());

        behaviours.put("collision", new Collision());

        behaviours.put("seek", new Seek());
        behaviours.put("pursuit", new Pursuit());
        behaviours.put("arrive", new Arrive());

        boidStates = new HashMap<Boid, Stack<State>>();
    }

    /**
     * this will cycle through every boid and call update on it's active state.
     */
    public void update() {
        boolean poppable = false;
        for (Boid boid : boidStates.keySet()) {
            poppable = boidStates.get(boid).peek().update(boid);
            if(poppable) {
                boidStates.get(boid).pop();
            }
            boid.state = boidStates.get(boid).peek().toString();
        }
//        states.peek().update(owner);
    }

    /**
     * this will call update on the specific boid's active state.
     * @param boid - boid to update.
     */
    public void update(Boid boid) {
        boolean poppable = boidStates.get(boid).peek().update(boid);
        if(poppable) {
            boidStates.get(boid).pop();
        }
    }

    /**
     * adds the boid to the stack with a default state.
     * @param boid
     */
    public void addBoid(Boid boid) {
        if( ! boidStates.containsKey(boid)) {
            Stack<State> stack = new Stack<State>();
            stack.add(getDefaultState(boid.getSpecies()));
            boidStates.put(boid, stack);
        }
    }

    /**
     * Adds a boid with both a default state and another state that's provided.
     *
     * @param boid
     * @param initialState
     */
    public void addBoid(Boid boid, State initialState) {
        if( ! boidStates.containsKey(boid)) {
            Stack<State> stack = new Stack<State>();
            stack.add(getDefaultState(boid.getSpecies()));
            stack.add(initialState);
            boidStates.put(boid, stack);
        }
    }

    /**
     * adds the boid without a base state.
     *
     * @param boid - boid to add
     * @param noUse - dummy parameter so that it has a different method signature
     */
    public void addBoid(Boid boid, boolean noUse) {
        if( ! boidStates.containsKey(boid)) {
            Stack<State> stack = new Stack<State>();
            boidStates.put(boid, stack);
        }
    }

    public void addBoid(Boid boid, Stack<State> states) {
        if( ! boidStates.containsKey(boid)) {
            boidStates.put(boid, states);
        }
    }

    private State getDefaultState(byte species) {
        String diet = SimulationManager.speciesData.get(species).getDiet();
        if(diet.equals("herbivore")) {
            return new HerbDefault(this, boidManager, ea);
        }
        else if(diet.equals("carnivore")) {
            return new CarnDefault(this, boidManager, ea);
        }
        else {
            return null;
        }
    }


    public void removeBoid(Boid boid){
        if(boidStates.containsKey(boid)) {
            boidStates.remove(boid);
        }
    }

    public boolean checkBoid(Boid boid) {
       return boidStates.containsKey(boid);
    }

    public void pushState(Boid boid, State state) {
        if(!boidStates.containsKey(boid)) {
            addBoid(boid);
        }
        boidStates.get(boid).add(state);
    }
    
    /**
     * Added by Matt Odinga 
     * Retrives the states from the string array and creats the state stack
     * @param boid
     * @param strArray
     */
    
    public void retriveStates(Boid boid ,String[] strArray) {
    	Stack<State> states = new Stack<State>();

    	for(int i = 0;  i <strArray.length;  i++){
            
            if(strArray[i].equals("HerbDefault")){
            	HerbDefault state = new HerbDefault(this, boidManager, ea);
            	states.push(state);
            }
            
            if(strArray[i].equals("EatGrass")){
                EatGrass state = new EatGrass(this, boidManager);
                states.push(state);
            }
            
            if(strArray[i].equals("Hungry")){
            	Hungry state = new Hungry(this, boidManager);
            	states.push(state);
            }
            
            if(strArray[i].equals("Panic")){
            	Panic state = new  Panic (this, boidManager);
            	states.push(state);
            }
            
            if(strArray[i].equals("Reproduce")){
                Reproduce state = new Reproduce(this, boidManager, ea);
                states.push(state);
            }
            
            if(strArray[i].equals("ApproachCorpse")){
                ApproachCorpse state = new ApproachCorpse(this, boidManager, null);
                states.push(state);
            }
            
            if(strArray[i].equals("CarnDefault")){
            	CarnDefault state = new  CarnDefault (this, boidManager, ea);
            	states.push(state);
            }

            if(strArray[i].equals("CarnReproduce")){
            	CarnReproduce state = new  CarnReproduce(this, boidManager, ea);
            	states.push(state);
            }
            
            if(strArray[i].equals("Eat")){
                Eat state = new  Eat(this, boidManager);
                states.push(state);
            }
            
            if(strArray[i].equals("GoForKill")){
            	GoForKill state = new GoForKill(this, boidManager, null);
            	states.push(state);
            }
            
            if(strArray[i].equals("Hunt")){
            	Hunt state = new Hunt(this, boidManager);
            	states.push(state);
            }
            
            if(strArray[i].equals("Stalk")){
                Stalk state = new Stalk(this, boidManager, null);
                states.push(state);
            }
            
            if(strArray[i].equals("Drink")){
            	Drink state = new Drink(this, boidManager);
            	states.push(state);
            }
            
            if(strArray[i].equals("Thirsty")){
                Thirsty state = new Thirsty(this, boidManager);
                states.push(state);
            }   
    	} 
    	addBoid(boid, states);
    }
    public Stack<State> getStack(Boid boid){
    	return boidStates.get(boid);
    }
}
