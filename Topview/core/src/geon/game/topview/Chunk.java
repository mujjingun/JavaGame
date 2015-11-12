package geon.game.topview;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class Chunk implements Serializable{

	private static final long serialVersionUID = 1L;
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
	
	private ModelInstance model = null;
	
	public ModelInstance getModel () {
		return model;
	}

	public void generateMesh() {
		boolean[] notRender = {
			false, false, false, false, false, false,
		};
		ModelBuilder md = new ModelBuilder();
		md.begin();
		MeshPartBuilder mpd = md.part("part", GL20.GL_TRIANGLES, new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, "a_position"),
			new VertexAttribute(Usage.Normal, 3, "a_normal"),
			new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")), Game.material);

		int count = 0;
		for(int i = 0; i < Chunk.XMAX; i++) {
			for(int j = 0; j < 5; j++) {
				for(int k = 0; k < Chunk.ZMAX; k++) {
					Block block = chunkData[i][j][k];
					if(block.getBlockType() != BlockType.Air) {
						notRender[0] = getLocal(i, j + 1, k).getBlockType() != BlockType.Air;
						notRender[1] = getLocal(i, j, k - 1).getBlockType() != BlockType.Air;
						notRender[2] = getLocal(i - 1, j, k).getBlockType() != BlockType.Air;
						notRender[3] = getLocal(i + 1, j, k).getBlockType() != BlockType.Air;
						notRender[4] = getLocal(i, j, k + 1).getBlockType() != BlockType.Air;
						notRender[5] = getLocal(i, j - 1, k).getBlockType() != BlockType.Air;
						for(int p = 0; p < 6; p++) {
							if(notRender[p]) continue;
							block.getBlockType().setUV(mpd);
							mpd.rect(
								Block.verts[p][3][0] - 0.5f + block.getPos().x,
								Block.verts[p][3][1] - 0.5f + block.getPos().y,
								Block.verts[p][3][2] - 0.5f + block.getPos().z,
								Block.verts[p][2][0] - 0.5f + block.getPos().x,
								Block.verts[p][2][1] - 0.5f + block.getPos().y,
								Block.verts[p][2][2] - 0.5f + block.getPos().z,
								Block.verts[p][1][0] - 0.5f + block.getPos().x,
								Block.verts[p][1][1] - 0.5f + block.getPos().y,
								Block.verts[p][1][2] - 0.5f + block.getPos().z,
								Block.verts[p][0][0] - 0.5f + block.getPos().x,
								Block.verts[p][0][1] - 0.5f + block.getPos().y,
								Block.verts[p][0][2] - 0.5f + block.getPos().z,
								Block.norms[p][0],
								Block.norms[p][1],
								Block.norms[p][2]);
							count ++;
						}
					}
				}
			}
		}
		Gdx.app.log("count", ""+count);
		Model m = md.end();
		model = new ModelInstance(m);
	}

	static int mathMod(int x, int y) {
		return ((x % y) + y) % y;
	}
	
	private static Vector3 tmp = new Vector3();
	public static Vector3 toLocal(int x, int y, int z) {
		return tmp.set(mathMod((int)x, Chunk.XMAX), mathMod((int)y, Chunk.YMAX), mathMod((int)z, Chunk.ZMAX));
	}
	
	public Block getLocal(int x, int y, int z) {
		if(x >= 0 && x < XMAX && y >= 0 && y < YMAX && z >= 0 && z < ZMAX)
			return chunkData[x][y][z];
		return new Block(BlockType.Air, null);
	}
}
