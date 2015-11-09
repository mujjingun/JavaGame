package geon.game.topview;

import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.zip.Inflater;

/**
 * <pre>
 * geon.game.topview
 * 	|_DDA
 * 
 * 개요 : 
 * 작성일 : 2015. 9. 24.
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class DDA {
	Ray ray;
	float lim;
	Vector3 deltaT = new Vector3();
	public Vector3 tv = new Vector3();
	Vector3 cellIndex = new Vector3();
	boolean isFirst = true;
	Vector3 originOffset = new Vector3();

	public DDA (Ray ray, float lim) {
		this.ray = ray.cpy();
		this.lim = lim;
		ray.origin.add(0.5f, 0.5f, 0.5f);
		originOffset.set(ray.origin.x - floor(ray.origin.x),
				ray.origin.y - floor(ray.origin.y),
				ray.origin.z - floor(ray.origin.z));
		ray.direction.nor();
		cellIndex.set(floor(ray.origin.x), floor(ray.origin.y), floor(ray.origin.z));
		if (ray.direction.x == 0) {
			deltaT.x = 0;
			tv.x = Float.POSITIVE_INFINITY;
		} else {
			deltaT.x = Math.abs(1 / ray.direction.x);
			tv.x = (floor(ray.origin.x) - ray.origin.x + (ray.direction.x > 0 ? 1 : 0)) / ray.direction.x;
		}
		if(ray.direction.y == 0) {
			deltaT.y = 0;
			tv.y = Float.POSITIVE_INFINITY;
		} else {
			deltaT.y = Math.abs(1 / ray.direction.y);
			tv.y = (floor(ray.origin.y) - ray.origin.y + (ray.direction.y > 0 ? 1 : 0)) / ray.direction.y;
		}
		if(ray.direction.z == 0) {
			deltaT.z = 0;
			tv.z = Float.POSITIVE_INFINITY;
		} else {
			deltaT.z = Math.abs(1 / ray.direction.z);
			tv.z = (floor(ray.origin.z) - ray.origin.z + (ray.direction.z > 0 ? 1 : 0)) / ray.direction.z;
		}
		//Gdx.app.log("DDASTART", "tv: "+tv);
	}

	private Collision ret = new Collision();
	public Collision step (Vector3 voxel) {
//		if (isFirst) {
//			isFirst = false;
//			Vector3 normals = new Vector3(-1, -1, -1);
//			if(originOffset.x > 0.5) {
//				originOffset.x = 1 - originOffset.x;
//				normals.x = 1;
//			}
//			if(originOffset.y > 0.5) {
//				originOffset.y = 1 - originOffset.y;
//				normals.y = 1;
//			}
//			if(originOffset.z > 0.5) {
//				originOffset.z = 1 - originOffset.z;
//				normals.z = 1;
//			}
//			if(originOffset.x < originOffset.y && originOffset.x < originOffset.z) {
//				ret.setDist(-originOffset.x);
//				ret.setNormal(normals.x, 0, 0);
//			} else if(originOffset.y < originOffset.x && originOffset.y < originOffset.z) {
//				//Gdx.app.log("first", ""+originOffset);
//				ret.setDist(-originOffset.y);
//				ret.setNormal(0, normals.y, 0);
//			} else {
//				ret.setDist(-originOffset.z);
//				ret.setNormal(0, 0, normals.z);
//			}
//			voxel.set(cellIndex);
//			ret.setCell(cellIndex);
//			return ret;
//		}
		//Gdx.app.log("dda", ""+tv);
		float distance = 0;
		if (tv.x == 0.0f || tv.x < tv.y && tv.x < tv.z) {
			distance = tv.x;
			tv.x += deltaT.x;
			cellIndex.x += Math.copySign(1, ray.direction.x);
			ret.setNormal(-Math.copySign(1, ray.direction.x), 0, 0);
		} else if (tv.y == 0.0f || tv.y < tv.x && tv.y < tv.z) {
			distance = tv.y;
			tv.y += deltaT.y;
			cellIndex.y += Math.copySign(1, ray.direction.y);
			ret.setNormal(0, -Math.copySign(1, ray.direction.y), 0);
		} else if(tv.z == 0.0f || tv.z < tv.x && tv.z < tv.y) {
			distance = tv.z;
			tv.z += deltaT.z;
			cellIndex.z += Math.copySign(1, ray.direction.z);
			ret.setNormal(0, 0, -Math.copySign(1, ray.direction.z));
		}
		voxel.set(cellIndex);
		ret.setDist(distance);
		ret.setCell(cellIndex);
		return ret;
	}

	public static class Collision {
		private float dist;
		private Vector3 normal = new Vector3();
		private Vector3 cell = new Vector3();

		public Vector3 getCell() {
			return cell;
		}

		public void setCell(Vector3 cell) {
			this.cell.set(cell);
		}

		public float getDist() {
			return dist;
		}

		public void setDist(float dist) {
			this.dist = dist;
		}

		public Vector3 getNormal() {
			return normal;
		}

		public void setNormal(float x, float y, float z) {
			this.normal.set(x, y, z);
		}
	}
}

