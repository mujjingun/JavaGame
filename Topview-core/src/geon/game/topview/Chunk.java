package geon.game.topview;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;


public class Chunk implements Serializable{

	public static final int XMAX = 16, YMAX = 256, ZMAX = 16;
	public Block[][][] chunkData = new Block[XMAX][YMAX][ZMAX]; // [x][y][z]
	{
		for(int i = 0; i < Chunk.XMAX; i++) {
			chunkData[i] = new Block[YMAX][ZMAX];
			for(int j = 0; j < Chunk.YMAX; j++) {
				chunkData[i][j] = new Block[ZMAX];
				for(int k = 0; k < Chunk.ZMAX; k++) {
					chunkData[i][j][k] = new Block(BlockType.Air, new Vector3(i, j, k));
				}
			}
		}
	}
}
