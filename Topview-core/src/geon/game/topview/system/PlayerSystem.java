package geon.game.topview.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Pool;
import geon.game.topview.DDA;

import java.util.ArrayList;
import java.util.List;

public class PlayerSystem extends EntitySystem{
    // 발이 눈에서 얼마나 멀리 떨어져 있는지
    private final Vector3[] FEET_OFFSETS = {
            new Vector3(-.4f, -1.7f, -.4f),
            new Vector3(-.4f, -1.7f, .4f),
            new Vector3(.4f, -1.7f, .4f),
            new Vector3(.4f, -1.7f, -.4f),
    };
    private final List<Vector3> ignoreList = new ArrayList<>();
    private final Pool<Vector3> vectorPool = new Pool<Vector3>() {
        @Override
        protected Vector3 newObject() {
            return new Vector3();
        }
    };
    private final Vector3 feetPos = new Vector3();
    private final Vector3 acc = new Vector3(0, -30, 0);
    private final Vector3 vel = new Vector3(0, 0, 0);
    private final float walkSpeed = 200;
    Vector3 go = new Vector3();
    private boolean isOnGround = false, isColliding = false;

    private static final Vector3 tmp = new Vector3(), deltaS = new Vector3();
    private static final Vector2 tmp2 = new Vector2();
    private final Ray ray = new Ray();

    private static void collide(Vector3 v, Vector3 norm) {
        norm = tmp.set(norm);
        v.sub(norm.scl(norm.dot(v)));
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        InputSystem input = getEngine().getSystem(InputSystem.class);
        CameraSystem cameraSystem = getEngine().getSystem(CameraSystem.class);
        Vector2 front = cameraSystem.getFront();
        Vector2 right = cameraSystem.getRight();
        MapSystem mapGen = getEngine().getSystem(MapSystem.class);

        go.set(0, 0, 0);
        if (input.isPressed(Input.Keys.W)) go.add(front.x, 0, front.y);
        if (input.isPressed(Input.Keys.S)) go.add(-front.x, 0, -front.y);
        if (input.isPressed(Input.Keys.A)) go.add(-right.x, 0, -right.y);
        if (input.isPressed(Input.Keys.D)) go.add(right.x, 0, right.y);
        go.nor().scl(deltaTime * walkSpeed);

        // only jump when on ground
        if (isOnGround && input.hasPushed(Input.Keys.SPACE)) vel.add(0, 8, 0);

        if(isColliding) {
            vel.x /= 1.2f;
            vel.z /= 1.2f;
        }
        vel.add(go);
        vel.add(tmp.set(acc).scl(deltaTime)); // dv = at
        if(tmp2.set(vel.x, vel.z).len() > 5) {
            tmp2.nor().scl(5);
            vel.x = tmp2.x;
            vel.z = tmp2.y;
        }

        deltaS.set(vel).scl(deltaTime); // ds = vt

        isOnGround = false;
        isColliding = false;
        for(Vector3 feetOffset : FEET_OFFSETS) {
            feetPos.set(cameraSystem.getCamPos()).add(feetOffset);
            //Gdx.app.log("offset: ", ""+feetOffset);
            for(int i = ignoreList.size() - 1; i >= 0; i--)
                vectorPool.free(ignoreList.remove(i));
            for(int i = 0; i < 3; i++) {
                ray.set(feetPos, deltaS);
                DDA.Collision collision;
                float len = deltaS.len();
                //Gdx.app.log("ds", ""+deltaS);
                if ((collision = mapGen.doesCollide(ray, len, ignoreList)) != null) {
                   float lenDot = deltaS.dot(collision.getNormal());
                   float distDot = tmp.set(deltaS).nor().scl(collision.getDist()).dot(collision.getNormal());
                   Gdx.app.log("", String.format("norm : %s, distDot: %f, lendot: %f",collision.getNormal(), -distDot, -lenDot));
               	 if(-distDot <= -lenDot + 0.1f) {
		                 ignoreList.add(vectorPool.obtain().set(collision.getCell()));
		                 isColliding = true;
		                 if(collision.getNormal().y > 0) {
		                     isOnGround = true;
		                 }
		                 collide(vel, collision.getNormal());
		                 collide(go, collision.getNormal());
		                 //Gdx.app.log("dot", "dotmove: " + tmp.set(collision.getNormal()).scl(lenDot - dot) );
		                 deltaS.sub(tmp.set(collision.getNormal()).scl(lenDot - distDot)).add(tmp.set(collision.getNormal()).scl(0.1f));
		                 //collide(deltaS, collision.getNormal());
		                 //.set(tmp.set(vel).scl(deltaTime));
               	 }
                }
            }
        }

        cameraSystem.setCamPos(cameraSystem.getCamPos().add(deltaS));

        if(cameraSystem.getCamPos().y < -100) cameraSystem.getCamPos().set(10, 10, 10);

    }
}
