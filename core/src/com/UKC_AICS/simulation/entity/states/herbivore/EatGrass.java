package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;

/**
 * Created by Emily on 10/07/2014.
 */
public class EatGrass extends State{
    public EatGrass(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {
        if(boid.position.x < 0 || boid.position.x > Constants.mapWidth || boid.position.y < 0 || boid.position.y > Constants.mapHeight) {
            System.out.println("I am out of bounds" + boid.position.x + " , " + boid.position.y);
        }
        byte grassAmount = WorldManager.getTileInfoAt((int) boid.position.x, (int) boid.position.y).get("grass");

        if( grassAmount >= 10 && boid.hunger < 80) {
            boid.setState(this.toString());
//            boid.setAcceleration(new Vector3(0f, 0f, 0f).sub(boid.velocity));
            grassAmount -= 1;
            boid.hunger += 1;
            WorldManager.changeTileOnLayer(boid.position.x, boid.position.y, "grass", grassAmount);
        } else {
//            System.out.println(boid + "\n Just quit EATGRASS state "  );
            return true;
        }

        return false;
    }
}
