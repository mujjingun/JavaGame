package geon.game.topview;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

public class Block implements Serializable{
	private static final long serialVersionUID = -3547357690787644603L;
	
	public static final float[][] verts_o = {
		{0, 0, 0}, // 0
		{0, 0, 1}, // 1
		{0, 1, 0}, // 2 
		{0, 1, 1}, // 3
		{1, 0, 0}, // 4
		{1, 0, 1}, // 5
		{1, 1, 0}, // 6
		{1, 1, 1}, // 7
	};
	public static final float[][][] verts = {
		{
			{0, 1, 1}, // 3
			{0, 1, 0}, // 2
			{1, 1, 0}, // 6
			{1, 1, 1}, // 7
		},
		
		{
			{1, 1, 0}, // 6
			{0, 1, 0}, // 2
			{0, 0, 0}, // 0
			{1, 0, 0}, // 4
		},
		
		{
			{0, 1, 0}, // 2 
			{0, 1, 1}, // 3
			{0, 0, 1}, // 1
			{0, 0, 0}, // 0
		},
		
		{
			{1, 1, 1}, // 7
			{1, 1, 0}, // 6
			{1, 0, 0}, // 4
			{1, 0, 1}, // 5
		},
		
		{
			{0, 1, 1}, // 3
			{1, 1, 1}, // 7
			{1, 0, 1}, // 5
			{0, 0, 1}, // 1
		},
		
		{
			{0, 0, 1}, // 1
			{1, 0, 1}, // 5
			{1, 0, 0}, // 4
			{0, 0, 0}, // 0
		}
	};
	
	public static final float[][] norms = {
		{0, 1, 0},
		{0, 0, -1},
		{-1, 0, 0},
		{1, 0, 0},
		{0, 0, 1},
		{0, -1, 0},
	};

	public Block(BlockType blockType, Vector3 pos) {
		this.blockType = blockType;
		this.pos = pos;
	}
	
	private BlockType blockType;
	private Vector3 pos;
	
	public BlockType getBlockType(){
		return blockType;
	}

	public Vector3 getPos () {
		return pos;
	}

	public void setPos (Vector3 pos) {
		this.pos.set(pos);
	}
	
	
}
