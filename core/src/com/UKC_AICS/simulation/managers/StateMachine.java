package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.entity.states.carnivore.CarnDefault;
import com.UKC_AICS.simulation.entity.states.herbivore.HerbDefault;
import com.UKC_AICS.simulation.entity.states.herbivore.Hungry;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Emily on 09/07/2014.
 */
public class StateMachine {

    private static BoidManager boidManager;
    public static Map<String, Behaviour> behaviours = new HashMap<String, Behaviour>();
//    private Boid owner;
//    private State currentState;
//    private Stack<State> states;

    private HashMap<Boid,Stack<State>> boidStates;

    /**
     * first implementation of a stack finite state machine.
     *
     */
    public StateMachine(BoidManager bm) {
//        this.owner = owner;
//        this.currentState = initialState;
        this.boidManager = bm;

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());

        behaviours.put("attractor", new Attractor());
        behaviours.put("repeller", new Repeller());

        behaviours.put("collision", new Collision2());

        behaviours.put("pursuit", new Pursuit());

        boidStates = new HashMap<Boid, Stack<State>>();
    }

    public void update() {
        boolean poppable = false;
        for (Boid boid : boidStates.keySet()) {
            poppable = boidStates.get(boid).peek().update(boid);
            if(poppable) {
                boidStates.get(boid).pop();
            }
        }
//        states.peek().update(owner);
    }
    public void update(Boid boid) {
        boolean poppable = boidStates.get(boid).peek().update(boid);
        if(poppable) {
            boidStates.get(boid).pop();
        }
    }


    public void addBoid(Boid boid, State initialState) {
        if( ! boidStates.containsKey(boid)) {
            Stack<State> stack = new Stack<State>();
            stack.add(getDefaultState(boid.getSpecies()));
            stack.add(initialState);
            boidStates.put(boid, stack);
        }
    }

    private State getDefaultState(byte species) {
        String diet = SimulationManager.speciesData.get(species).getDiet();
        if(diet.equals("herbivore")) {
            return new HerbDefault(this, boidManager);
        }
        else if(diet.equals("carnivore")) {
            return new CarnDefault(this, boidManager);
        }
        else {
            return null;
        }
    }

    public void addBoid(Boid boid) {
        addBoid(boid, new HerbDefault(this, boidManager));
    }

    public void removeBoid(Boid boid){
        if(boidStates.containsKey(boid)) {
            boidStates.remove(boid);
        }
    }

    public void pushState(Boid boid, State state) {

        if(!boidStates.containsKey(boid)) {
            addBoid(boid);
        }

        boidStates.get(boid).add(state);
    }
}
