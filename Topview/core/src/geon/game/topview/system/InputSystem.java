
package geon.game.topview.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputSystem extends EntitySystem implements InputProcessor {

	private Map<Object, Integer> click = new HashMap<>();
	private Map<Object, Integer> keyDown = new HashMap<>();
	private Stack<Vector2> clickStack = new Stack<>();
	private Vector2 lastMouse = new Vector2();
	private boolean isCursorCatched;
	private boolean isMouseCorrect = false;

	private Vector2 tmp = new Vector2();

	public Vector2 getMouseMoved () {
		if (isMouseCorrect)
			return tmp.set(lastMouse.x - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - lastMouse.y);
		else
			return tmp.set(0, 0);
	}

	@Override
	public void update (float deltaTime) {
		if(Gdx.input.getInputProcessor() != this) {
			Gdx.input.setInputProcessor(this);
			Gdx.input.setCursorCatched(true);
			isCursorCatched = true;
		}
		updateMouse(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}

	public boolean isPressed (int keycode) {
		if (click.containsKey(keycode))
			return click.get(keycode) == 1;
		else
			return false;
	}

	public boolean hasPushed(int keycode) {
		if (keyDown.containsKey(keycode) && keyDown.get(keycode) == 1) {
			keyDown.put(keycode, 0);
			return true;
		} else
			return false;
	}

	public void updateMouse (int x, int y) {
		if (isCursorCatched) {
			Gdx.input.setCursorPosition(x, y);
		}
		lastMouse.set(x, y);
	}

	@Override
	public boolean keyDown (int keycode) {
		click.put(keycode, 1);
		if (keycode == Keys.ESCAPE) {
			Gdx.input.setCursorCatched(false);
			isCursorCatched = false;
			isMouseCorrect = false;
		}
		keyDown.put(keycode, 1);
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		click.put(keycode, 0);
		if(keycode == Keys.F11) {
			if(!Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setDisplayMode(
					Gdx.graphics.getDesktopDisplayMode().width,
					Gdx.graphics.getDesktopDisplayMode().height, true);
			} else {
				Gdx.graphics.setDisplayMode(1024, 768, false);
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		Gdx.input.setCursorCatched(true);
		isCursorCatched = true;
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		clickStack.push(new Vector2(screenX, screenY));
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		if (isCursorCatched) {
			isMouseCorrect = true;
			lastMouse.set(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
