package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.entity.states.State;
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
            stack.add(initialState);
            if(boid.getSpecies() == 1) {
                stack.add(new Hungry(this, boidManager));
            }
            boidStates.put(boid, stack);
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
