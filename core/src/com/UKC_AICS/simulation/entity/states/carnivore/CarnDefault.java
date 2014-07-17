package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.entity.states.Thirsty;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;

/**
 * Created by Emily on 16/07/2014.
 */
public class CarnDefault extends State{

    public CarnDefault(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {


        if(boid.thirst < 15) {
            System.out.println(boid + "\n Just posted Thirsty state "  );
            parent.pushState(boid, new Thirsty(parent, bm));
        }
        else if(boid.hunger < 15) {
            System.out.println(boid + "\n Just posted Hungry state "  );
            parent.pushState(boid, new Hunt(parent,bm));
        } else {


        }
        return false; // this is a default state so should NOT be popped.
    }
}
