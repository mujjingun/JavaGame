/**
 * 
 */
package geon.game.topview;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimatedActor extends Image
{
    private final AnimationDrawable drawable;

    public AnimatedActor(AnimationDrawable drawable)
    {
        super(drawable);
        this.drawable = drawable;
    }

    @Override
    public void act(float delta)
    {
        drawable.act(delta);
        super.act(delta);
    }
}