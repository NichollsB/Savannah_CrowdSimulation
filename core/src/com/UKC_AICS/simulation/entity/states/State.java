package com.UKC_AICS.simulation.entity.states;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 09/07/2014.
 */
abstract public class State {

    protected static BoidManager bm;
    protected static StateMachine parent;
    protected Vector3 steering = new Vector3();

    public State(StateMachine parent, BoidManager bm) {
        this.bm = bm;
        this.parent = parent;
    }

    abstract public boolean update(Boid boid);

}
