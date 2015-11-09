package geon.game.topview.system;

import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.math.collision.Ray;
import geon.game.topview.Block;
import geon.game.topview.BlockType;
import geon.game.topview.Chunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import geon.game.topview.DDA;

public class MapSystem extends EntitySystem {

	private HashMap<Vector2, Chunk> chunks = new HashMap<>();
	private ModelInstance select;
	private final Vector3 selectPos = new Vector3();
	private final Vector3 tmp = new Vector3();
	
	@Override
	public void addedToEngine (Engine engine) {
		generateMap();
	}

	private void addBlock(BlockType bType, int x, int y, int z) {
		Chunk c = getChunk(tmp.set(x, y, z));
		if(c != null)
			c.chunkData[x][y][z] = new Block(bType, new Vector3(x, y, z));
	}
	
	public void generateMap() {
		Chunk chunk = new Chunk();
		chunks.put(new Vector2(0, 0), chunk);
		
		for(int i = 0; i < Chunk.XMAX; i++) {
			for(int j = 0; j < Chunk.ZMAX; j++) {
				addBlock(BlockType.Dirt, i, 0, j);
			}
		}
		addBlock(BlockType.Dirt, 1, 1, 1);
		addBlock(BlockType.Dirt, 1, 2, 1);
		addBlock(BlockType.Dirt, 2, 1, 1);
		addBlock(BlockType.Dirt, 2, 2, 1);
		addBlock(BlockType.Dirt, 2, 1, 2);
		addBlock(BlockType.Dirt, 1, 1, 2);
		
		Material m = new Material(new TextureAttribute(TextureAttribute.Diffuse, new Texture(Gdx.files.internal("select.png"))));
		m.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		select = new ModelInstance(new ModelBuilder().createBox(1.03f, 1.03f, 1.03f, m,
	            	            Usage.Position | Usage.Normal | Usage.TextureCoordinates));
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		if(selectPos != null) {
			select.transform.setToTranslation(selectPos);
			modelBatch.render(select, environment);
		}
		for(Entry<Vector2, Chunk> chunk : chunks.entrySet()) {
			for(int i = 0; i < Chunk.XMAX; i++) {
				for(int j = 0; j < Chunk.YMAX; j++) {
					for(int k = 0; k < Chunk.ZMAX; k++) {
						Block block = chunk.getValue().chunkData[i][j][k];
						if(block.getBlockType() != BlockType.Air) {
							boolean[] notRender = new boolean[6];
							notRender[0] = getBlock(new Vector3(i - 1, j, k)).getBlockType() == BlockType.Air;
							notRender[1] = getBlock(new Vector3(i + 1, j, k)).getBlockType() == BlockType.Air;
							notRender[2] = getBlock(new Vector3(i, j - 1, k)).getBlockType() == BlockType.Air;
							notRender[3] = getBlock(new Vector3(i, j + 1, k)).getBlockType() == BlockType.Air;
							notRender[4] = getBlock(new Vector3(i, j, k - 1)).getBlockType() == BlockType.Air;
							notRender[5] = getBlock(new Vector3(i, j, k + 1)).getBlockType() == BlockType.Air;
							block.render(modelBatch, environment, notRender);
						}
					}
				}
			}
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

	int mathMod(int x, int y) {
		return ((x % y) + y) % y;
	}
	Block getBlock(Vector3 pos) {
		if(getChunk(pos) == null) {
			return new Block(BlockType.Air, pos);
		}
		return getChunk(pos).chunkData[mathMod((int)pos.x, Chunk.XMAX)][mathMod((int)pos.y, Chunk.YMAX)][mathMod((int)pos.z, Chunk.ZMAX)];
	}
	private Vector2 tmp2 = new Vector2();
	Chunk getChunk(Vector3 pos) {
		Vector2 chunk = tmp2.set(floor(pos.x / Chunk.XMAX), floor(pos.z / Chunk.ZMAX));
		return chunks.get(chunk);
	}

	public Vector3 getSelectPos () {
		return selectPos;
	}

	public void setSelectPos (Vector3 selectPos) {
		this.selectPos.set(selectPos);
	}
	
}
