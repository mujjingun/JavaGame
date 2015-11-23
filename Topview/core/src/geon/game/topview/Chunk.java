package geon.game.topview;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


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
	private ModelBuilder md;
	private boolean haveToUpdate = true;
	private boolean haveToUpdateModel = false;
	
	public ModelInstance getModel () {
		return model;
	}
	
	public void update() {
		haveToUpdate = true;
	}
	
	public boolean generateModelIfUpdated() {
		if(haveToUpdateModel) {
			haveToUpdateModel = false;
			Model m = md.end();
			Mesh mesh = m.meshes.get(0);
			int numVertices = mesh.getNumVertices();
			int vertexSize = mesh.getVertexAttributes().vertexSize / 4;
			int offset = mesh.getVertexAttribute(512).offset / 4;
			float[] vertices = new float[numVertices * vertexSize];
			mesh.getVertices(vertices);
			int c = 0;
			for(int i = 0; i < numVertices * vertexSize; i += vertexSize) {
				vertices[i + offset] = customAttributes.get(c++);
				vertices[i + offset + 1] = customAttributes.get(c++);
			}
			mesh.updateVertices(0, vertices);
			model = new ModelInstance(m);
			return true;
		}
		return false;
	}

	private ArrayList<Float> customAttributes = new ArrayList<>();
	private int[] above = new int[3];
	private final int[][] SURR = {
		{-1, -1},
		{0, -1},
		{1, -1},
		{1, 0},
		{1, 1},
		{0, 1},
		{-1, 1},
		{-1, 0},
	};
	private final boolean[] INVERT = {
		true, true, true, false, false, false
	};
	public void generateMesh() {
		if(!haveToUpdate) return;
		boolean[] notRender = {
			false, false, false, false, false, false,
		};
		md = new ModelBuilder();
		md.begin();
		MeshPartBuilder mpd = md.part("part", GL20.GL_TRIANGLES, new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, "a_position"),
			new VertexAttribute(Usage.Normal, 3, "a_normal"),
			new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"),
			new VertexAttribute(512, 2, "a_occlusion")), Resources.material);
		
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
							
							int occ = 0;
							
							above[0] = i + (int)Block.norms[p][0];
							above[1] = j + (int)Block.norms[p][1];
							above[2] = k + (int)Block.norms[p][2];
							
							int[] sideIdx = new int[2];
							int sideIdxIdx = 0;
							
							for(int t = 0; t < 3; t++) {
								if((int)Block.norms[p][t] == 0)
									sideIdx[sideIdxIdx++] = t;
							}
							int[] tmp = new int[3]; 
							for(int o = 0; o < 8; o++) {
								tmp[0] = above[0];
								tmp[1] = above[1];
								tmp[2] = above[2];
								for(int t = 0; t < 2; t++)
									tmp[sideIdx[t]] += SURR[INVERT[p]? o : ((7 - o + 1) % 8)][t];
								if(getLocal(tmp[0], tmp[1], tmp[2]).getBlockType() != BlockType.Air)
									occ += 1 << o;
							}
							
							float w = 1f / 16;
							float x = w * (occ % 16);
							float y = w * (occ / 16);
							float pad = w / 32;
							
							customAttributes.add(x + pad);
							customAttributes.add(y + pad);
							
							customAttributes.add(x + pad);
							customAttributes.add(y + w - pad);
							
							customAttributes.add(x + w - pad);
							customAttributes.add(y + w - pad);
							
							customAttributes.add(x + w - pad);
							customAttributes.add(y + pad);
						}
					}
				}
			}
		}
		
		haveToUpdateModel = true;
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
