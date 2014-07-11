package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 10/07/2014.
 */
public class Eat extends State{
    public Eat(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {
        byte grassAmount = WorldManager.getTileInfoAt((int) boid.position.x, (int) boid.position.y).get("grass");

        if( grassAmount >= 10 && boid.hunger < 80) {
            boid.setAcceleration(new Vector3(0f, 0f, 0f).sub(boid.velocity));
            grassAmount -= 1;
            boid.hunger += 1;
            WorldManager.changeTileOnLayer(boid.position.x, boid.position.y, "grass", grassAmount);
        } else {
            return true;
        }

        return false;
    }
}
