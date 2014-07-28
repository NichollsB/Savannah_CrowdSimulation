package com.UKC_AICS.simulation.entity.states;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 11/07/2014.
 */
public class Drink extends State {
    public Drink(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        if(boid.thirst < 25) { //and check there is enough water is still there to drink from.
//            System.out.println(boid + "\n Just quit DRINK state ");
            return true;
        } else {

            boid.setState(this.toString());
            boid.setAcceleration(new Vector3(0f,0f,0f));
            boid.setVelocity(new Vector3(0f,0f,0f));

            // drink from tile
            boid.thirst -= 0.1;
            return false;
        }
    }
}
