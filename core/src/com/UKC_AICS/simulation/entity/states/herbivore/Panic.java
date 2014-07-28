package com.UKC_AICS.simulation.entity.states.herbivore;

import com.UKC_AICS.simulation.entity.Boid;
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

        Array<Boid> closeBoids = new Array<Boid>();
        Array<Boid> predators = new Array<Boid>();

        for (Boid b : nearBoids) {
            if (SimulationManager.speciesData.get(b.getSpecies()).getDiet().equals("carnivore")) {
                predators.add(b);
            }
        }

        return true;
    }
}
