package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.Pursuit;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 16/07/2014.
 * Predator
 */
public class GoForKill extends State {

    private Boid target;

    public GoForKill(StateMachine parent, BoidManager bm, Boid target) {
        super(parent, bm);
        this.target = target;
    }

    @Override
    public boolean update(Boid boid) {
        if (parent.checkBoid(target)) {
            //TODO  Needs to have a target and use the pursuit behaviour

            //TODO kill prey on collision detection

            //TODO place corpse on prey kill

            if (boid.getPosition().cpy().sub(target.getPosition()).len() > 16f) {  //TODO add some check for pursuing kill?
                steering.set(0f, 0f, 0f);
                steering.add(Pursuit.act(boid, target));

                boid.setAcceleration(steering);

                return false;
            } else {
                //Target has been killed.
                //TODO drop a corpse
                //TODO this is supposed to remove boid when killed --> throws concurrent error with statemachine
//                bm.removeBoid(target);

                return true;
            }

        } else {
            return true;
        }
    }
}
