package geon.game.topview.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
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
    private final float walkSpeed = 5, sprintSpeed = 10;
    private final Vector3 go = new Vector3();
    private boolean isOnGround = false, isColliding = false;

    private static final Vector3 tmp = new Vector3(), deltaS = new Vector3();
    private final Ray ray = new Ray();
    
    private boolean isWalking = false;
    private float swayTime = 0;

    private static void collide(Vector3 v, Vector3 norm) {
        norm = tmp.set(norm);
        v.sub(norm.scl(norm.dot(v)));
    }
    
    
    public Vector3 getGo () {
		return go;
	}


	public Vector3 getVel () {
		return vel;
	}


	@Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        InputSystem input = getEngine().getSystem(InputSystem.class);
        CameraSystem cameraSystem = getEngine().getSystem(CameraSystem.class);
        Vector2 front = cameraSystem.getFront();
        Vector2 right = cameraSystem.getRight();
        MapSystem mapGen = getEngine().getSystem(MapSystem.class);
        
        isWalking = false;
        if(isColliding) go.set(0, 0, 0);
        if (input.isPressed(Input.Keys.W) || input.isPressed(Input.Keys.SHIFT_LEFT)) {
      	  isWalking = true;
      	  go.add(front.x, 0, front.y);
        }
        if (input.isPressed(Input.Keys.S)) go.add(-front.x, 0, -front.y);
        if (input.isPressed(Input.Keys.A)) go.add(-right.x, 0, -right.y);
        if (input.isPressed(Input.Keys.D)) go.add(right.x, 0, right.y);
        
        // sprint
        if(input.isPressed(Input.Keys.SHIFT_LEFT))
      	  go.nor().scl(sprintSpeed);
        else 
      	  go.nor().scl(walkSpeed);

        // only jump when on ground
        if (isOnGround && input.hasPushed(Input.Keys.SPACE)) vel.add(0, 8, 0);

        vel.add(go);
        vel.add(tmp.set(acc).scl(deltaTime)); // dv = at

        deltaS.set(tmp.set(vel).scl(deltaTime)); // ds = vt

        isOnGround = false;
        isColliding = false;
        for(Vector3 feetOffset : FEET_OFFSETS) {
            feetPos.set(cameraSystem.getCamPos()).add(feetOffset);
            for(int i = ignoreList.size() - 1; i >= 0; i--)
                vectorPool.free(ignoreList.remove(i));
            for(int i = 0; i < 3; i++) {
                ray.set(feetPos, deltaS);
                DDA.Collision collision;
                float len = deltaS.len();
                if ((collision = mapGen.doesCollide(ray, len, ignoreList)) != null) {
                   float lenDot = deltaS.dot(collision.getNormal());
                   float distDot = tmp.set(deltaS).nor().scl(collision.getDist()).dot(collision.getNormal());
                   if(-distDot <= -lenDot + 0.101f) {
		                 ignoreList.add(vectorPool.obtain().set(collision.getCell()));
		                 isColliding = true;
		                 if(collision.getNormal().y > 0) {
		                     isOnGround = true;
		                 }
		                 collide(vel, collision.getNormal());
		                 collide(go, collision.getNormal());
		                 deltaS.sub(tmp.set(collision.getNormal()).scl(lenDot - distDot - 0.1f));
               	 }
                }
            }
        }
        
        vel.sub(go);
        
        vel.scl(0.99999f);

        cameraSystem.setCamPos(cameraSystem.getCamPos().add(deltaS));
        
        if(isWalking) {
      	  if(input.isPressed(Input.Keys.SHIFT_LEFT))
      		  swayTime += deltaTime * 3;
      	  else swayTime += deltaTime;
      	  cameraSystem.sway(true, swayTime);
        } else cameraSystem.sway(false, 0);

        if(cameraSystem.getCamPos().y < -100) cameraSystem.getCamPos().set(1f, 10, 1f);
        
    }
}
