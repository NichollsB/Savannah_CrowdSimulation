package com.UKC_AICS.simulation.entity.behaviours;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 04/07/2014.
 */
public class Attractor extends Behaviour {

    public Vector3 act() {
        tmpVec.set(0,0,0);

        return tmpVec;
    }
}
