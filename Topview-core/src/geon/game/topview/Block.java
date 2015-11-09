package geon.game.topview;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public class Block implements Serializable{
	private static final long serialVersionUID = -3547357690787644603L;

	public Block(BlockType blockType, Vector3 pos) {
		this.blockType = blockType;
		this.pos = pos;
	}
	
	private BlockType blockType;
	private Vector3 pos;
	
	public BlockType getBlockType(){
		return blockType;
	}

	public void render(ModelBatch modelBatch, Environment environment, boolean[] notRender) {
		for(int i = 0; i < 6; i++) {
			if(notRender[i]) {
				getBlockType().getModel()[i].transform.setTranslation(pos.x, pos.y, pos.z);
				//if(shader != null)
					modelBatch.render(getBlockType().getModel()[i], environment);
				//else 
				//	modelBatch.render(getBlockType().getModel()[i], environment);
			}
		}
	}
}
