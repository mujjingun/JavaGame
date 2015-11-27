/**
 * 
 */

package geon.game.topview;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimatedActor extends Image {
	private final AnimationDrawable drawable;
	private float time = 0;
	private float delay;

	public AnimatedActor (AnimationDrawable drawable, float delay) {
		super(drawable);
		this.drawable = drawable;
		this.delay = delay;
	}

	@Override
	public void act (float delta) {
		time += delta;
		if(time > delay)
			drawable.act(delta);
		super.act(delta);
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
