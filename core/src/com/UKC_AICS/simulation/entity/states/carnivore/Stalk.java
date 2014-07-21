package com.UKC_AICS.simulation.entity.states.carnivore;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.behaviours.Seek;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector3;

import static com.UKC_AICS.simulation.managers.StateMachine.behaviours;

/**
 * Created by Emily on 16/07/2014.
 */

public class Stalk extends State {

    private Boid target;

    public Stalk(StateMachine parent, BoidManager bm, Boid target) {
        super(parent, bm);
        this.target = target;
    }

    @Override
    public boolean update(Boid boid) {
        //check boid still exists
        if(parent.checkBoid(target)) {
            float distance = boid.getPosition().cpy().sub(target.getPosition()).len();
            //check still within sight
            if (distance < boid.sightRadius) {
                Vector3 tv = new Vector3(0f, 0f, 0f);
                Vector3 targetPos = new Vector3(0f, 0f, 0f);
                targetPos.set(target.getPosition());
                tv.set(target.getVelocity());
                tv.scl(-1f);
                tv.nor().scl(10f);
                targetPos.add(tv);

                steering.set(Seek.act(boid, targetPos));


                //Check is boid is still in list.  If not pop to hunt (for corpse)
                //check if prey is close enought to be chased down
                if (boid.getPosition().cpy().sub(target.getPosition()).len() < 100f) {
                    parent.pushState(boid, new GoForKill(parent, bm, target));
                }
            }
            //TODO Stalk steering behaviour --> follow leader

        } else {
            return true;
        }
        return true;
    }
}
