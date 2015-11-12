package geon.game.topview.system;

import static com.badlogic.gdx.math.MathUtils.floor;
import geon.game.topview.Block;
import geon.game.topview.BlockType;
import geon.game.topview.Chunk;
import geon.game.topview.DDA;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class MapSystem extends EntitySystem {

	private HashMap<Vector2, Chunk> chunks = new HashMap<>();
	private ModelInstance select;
	private final Vector3 selectPos = new Vector3();
	private final Vector3 tmp = new Vector3();
	
	@Override
	public void addedToEngine (Engine engine) {
		generateMap();
		
		Material m = new Material(new TextureAttribute(TextureAttribute.Diffuse, new Texture(Gdx.files.internal("select.png"))));
		m.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		select = new ModelInstance(new ModelBuilder().createBox(1.03f, 1.03f, 1.03f, m,
	            	            Usage.Position | Usage.Normal | Usage.TextureCoordinates));
	}

	private void addBlock(BlockType bType, int x, int y, int z, boolean makeChunk) {
		Chunk c = getChunk(tmp.set(x, y, z), makeChunk);
		Vector3 local = Chunk.toLocal(x, y, z);
		if(c != null)
			c.chunkData[(int)local.x][(int)local.y][(int)local.z] = new Block(bType, new Vector3(x, y, z));
	}
	
	class MazeMaker {
		public final int[][] DIR = {
			{-1, 0}, {0, 1}, {1, 0}, {0, -1}
		};
		private final static int MAZE_HEIGHT = 16, MAZE_WIDTH = 16;
		class Cell {
			public int[] wall = new int[4]; {
				wall[0] = wall[1] = wall[2] = wall[3] = 1;
			}
			@Override
			public String toString() {
				return String.format("%d %d %d %d ", wall[0], wall[1], wall[2], wall[3]);
			}
		}
		private final Cell[][] maze = new Cell[MAZE_HEIGHT + 2][];
		private final boolean[][] chk = new boolean[MAZE_HEIGHT + 2][]; {
			for (int i = 0; i < MAZE_HEIGHT + 2; i++){
				chk[i] = new boolean[MAZE_WIDTH + 2];
				maze[i] = new Cell[MAZE_WIDTH + 2];
				for (int j = 0; j < MAZE_WIDTH + 2; j++) {
					chk[i][j] = false;
					maze[i][j] = new Cell();
					if(i == 1 && j == 1) {
						maze[i][j].wall[0] = 0;
						maze[i][j].wall[3] = 0;
					}
					if(i == 0 || i == MAZE_HEIGHT + 1 || j == 0 || j == MAZE_WIDTH + 1) {
						chk[i][j] = true;
					}
				}
			}
		}
		private final Random rand = new Random();
		public Cell[][] makeMaze(int h, int w) {
			chk[h][w] = true;
			for(;;) {
				boolean blocked = true;
				for(int j = 0; j < 4; j++) {
					if(!chk[h + DIR[j][0]][w + DIR[j][1]]) {
						blocked = false;
						break;
					}
				}
				if (blocked) break;
				int d = rand.nextInt(4);
				int nh = h + DIR[d][0], nw = w + DIR[d][1];
				if (chk[nh][nw]) continue;
				maze[h][w].wall[d] = maze[nh][nw].wall[(d + 2) % 4] = 0;
				makeMaze(nh, nw);
			}
			return maze;
		}
	}
	
	public void generateMap() {
		
		MazeMaker.Cell[][] cells = new MazeMaker().makeMaze(1, 1);
		for(int i = 1; i < cells.length - 1; i++) {
			for(int j = 1; j < cells[i].length - 1; j++) {
				int ci = i * 3, cj = j * 3;
				if(cells[i][j].wall[0] > 0) {
					addBlock(BlockType.Brick, ci, 1, cj + 1, true);
					addBlock(BlockType.Brick, ci, 1, cj + 2, true);

					addBlock(BlockType.Brick, ci, 2, cj + 1, true);
					addBlock(BlockType.Brick, ci, 2, cj + 2, true);
				}
				if(cells[i][j].wall[1] > 0) {
					addBlock(BlockType.Brick, ci + 1, 1, cj + 3, true);
					addBlock(BlockType.Brick, ci + 2, 1, cj + 3, true);

					addBlock(BlockType.Brick, ci + 1, 2, cj + 3, true);
					addBlock(BlockType.Brick, ci + 2, 2, cj + 3, true);
				}
				if(cells[i][j].wall[2] > 0) {
					addBlock(BlockType.Brick, ci + 3, 1, cj + 1, true);
					addBlock(BlockType.Brick, ci + 3, 1, cj + 2, true);

					addBlock(BlockType.Brick, ci + 3, 2, cj + 1, true);
					addBlock(BlockType.Brick, ci + 3, 2, cj + 2, true);
				}
				if(cells[i][j].wall[3] > 0) {
					addBlock(BlockType.Brick, ci + 1, 1, cj, true);
					addBlock(BlockType.Brick, ci + 2, 1, cj, true);

					addBlock(BlockType.Brick, ci + 1, 2, cj, true);
					addBlock(BlockType.Brick, ci + 2, 2, cj, true);

				}
				
				addBlock(BlockType.Brick, ci, 1, cj, true);
				addBlock(BlockType.Brick, ci, 2, cj, true);
				addBlock(BlockType.Brick, ci, 1, cj + 3, true);
				addBlock(BlockType.Brick, ci, 2, cj + 3, true);
			}
		}
		
		for(Entry<Vector2, Chunk> chunk : chunks.entrySet())
			chunk.getValue().generateMesh();
		
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		if(selectPos != null) {
			select.transform.setToTranslation(selectPos);
			modelBatch.render(select, environment);
		}
		int count = 0;
		for(Entry<Vector2, Chunk> chunk : chunks.entrySet()) {
			//chunk.getValue().
			modelBatch.render(chunk.getValue().getModel(), environment);
		}
	}

	public DDA.Collision doesCollide(Ray ray, float lim, List<Vector3> ignore) {
		if(lim == 0 || ray.direction.len() == 0) return null;
		//Gdx.app.log("start", ""+ray.origin);
		DDA PointDDA = new DDA(ray, 100);
		Vector3 curPos = new Vector3();
		for(int i = 0; i < 30; i++) {
			DDA.Collision c = PointDDA.step(curPos);
			//Gdx.app.log("normal", ""+c.getNormal()+", "+c.getDist()+" bt"+getBlock(curPos).getBlockType());
			//Gdx.app.log("test", " curpos:"+curPos);
			if (getBlock(curPos).getBlockType() != BlockType.Air) {
				//Gdx.app.log("test", "collide!");
				return c;
			}
			// lim
			if (c.getDist() > lim + 1f) break;
		}
		return null;
	}
	
	Block getBlock(Vector3 pos) {
		return getBlock(pos, false);
	}
	Block getBlock(Vector3 pos, boolean makeChunk) {
		Chunk c = getChunk(pos, makeChunk);
		if(c == null) {
			return new Block(BlockType.Air, pos);
		}
		Vector3 v3 = Chunk.toLocal((int)pos.x, (int)pos.y, (int)pos.z);
		return c.chunkData[(int)v3.x][(int)v3.y][(int)v3.z];
	}
	
	private Vector2 tmp2 = new Vector2();
	Chunk getChunk(Vector3 pos, boolean makeChunk) {
		Vector2 chunkPos = tmp2.set(floor(pos.x / Chunk.XMAX), floor(pos.z / Chunk.ZMAX));
		Chunk chunk = chunks.get(chunkPos);
		if(chunk != null)
			return chunk;
		else if (makeChunk){
			chunk = new Chunk();
			chunks.put(new Vector2(chunkPos), chunk);
			
			for(int i = 0; i < Chunk.XMAX; i++) {
				for(int j = 0; j < Chunk.ZMAX; j++) {
					addBlock(BlockType.Dirt, (int)chunkPos.x * Chunk.ZMAX + i, 0, (int)chunkPos.y * Chunk.ZMAX + j, false);
				}
			}
			
			return chunk;
		}
		return null;
	}

	public Vector3 getSelectPos () {
		return selectPos;
	}

	public void setSelectPos (Vector3 selectPos) {
		this.selectPos.set(selectPos);
	}
	
}
