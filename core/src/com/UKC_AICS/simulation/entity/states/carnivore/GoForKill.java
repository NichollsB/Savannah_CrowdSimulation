package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.Pursuit;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.UKC_AICS.simulation.managers.WorldManager;
import com.badlogic.gdx.math.Vector3;

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
        //check boid still exists
        if (parent.checkBoid(target)) {
            //TODO  Needs to have a target and use the pursuit behaviour

            //TODO kill prey on collision detection

            //TODO place corpse on prey kill

            //distance between boid and target
            float distance = boid.getPosition().cpy().sub(target.getPosition()).len();

            //check if target is collided  and killed
            if (distance <= 16f) {  //TODO add some check for pursuing kill?
                //Target has been killed.
                //TODO drop a corpse
                Object food = new Object((byte)0,(byte)0, new Vector3(target.position.x, target.position.y, 0f));
                WorldManager.putObject(food);
                bm.storeBoidForRemoval(target);
                parent.pushState(boid, new Eat(parent, bm, target));
                return true;
            //check target is still within sight range
            } else if (distance < boid.sightRadius){
                steering.set(0f, 0f, 0f);
                steering.add(Pursuit.act(boid, target));
                boid.setAcceleration(steering);

                System.out.println("Kill chase: " + target.getSpecies() + " species, " + target.position.x + ", " + target.position.y);
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
}
