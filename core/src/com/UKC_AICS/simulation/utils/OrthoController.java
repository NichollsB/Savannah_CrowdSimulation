package com.UKC_AICS.simulation.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 29/06/2014.
 */
public class OrthoController extends CamControllerBase{


    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();


    public OrthoController(Camera camera) {
        super(camera);
    }

    protected OrthoController(CameraGestureListener gestureListener, Camera camera) {
        super(gestureListener, camera);
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        camera.unproject(curr.set(x, y, 0));
        if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
            camera.unproject(delta.set(last.x, last.y, 0));
            delta.sub(curr);
            camera.position.add(delta.x, delta.y, 0);
        }
        last.set(x, y, 0);
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        last.set(-1, -1, -1);
        return false;
    }

}
