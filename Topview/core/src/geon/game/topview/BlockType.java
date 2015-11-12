
package geon.game.topview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public enum BlockType {
	Air(0), Dirt(1), Brick(2);
	
	private int blockId;

	private BlockType (int blockId) {
		this.blockId = blockId;
	}

	
	public void setUV(MeshPartBuilder mpd) {
		Texture t = Game.texture;
		float w = 64f / t.getWidth();
		mpd.setUVRange(w * (blockId + 0.01f), 0, w * (blockId + 1 - 0.01f), 1);
	}
}
