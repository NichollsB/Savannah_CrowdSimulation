package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.Evade;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Emily on 28/07/2014.
 */
public class Panic extends State {
    public Panic(StateMachine parent, BoidManager bm) {
        super(parent, bm);
    }

    @Override
    public boolean update(Boid boid) {

        Array<Boid> nearBoids = BoidManager.getBoidGrid().findInSight(boid);
        Array<Boid> predators = new Array<Boid>();

        for (Boid b : nearBoids) {
            if (SimulationManager.speciesData.get(b.getSpecies()).getDiet().equals("carnivore")) {
                predators.add(b);
            }
        }
        steering.set(0f,0f,0f);
        if(predators.size > 0) {
            for(Boid predator : predators) {
                steering.add(Evade.act(boid, predator));
            }
        } else {
            boid.panic -= 10;
        }

        boid.setAcceleration(steering);


        //popping decisions.
        if(boid.panic < 30 ) {
            return true;
        } else {
            return false;
        }

    }
}
