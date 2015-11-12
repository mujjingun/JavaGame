
package geon.game.topview.desktop;

import geon.game.topview.Game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GAME";
		config.width = 1024;
		config.height = 768;
		config.samples = 3;
		config.fullscreen = false;
		config.vSyncEnabled = false;
		config.useGL30 = false;
		config.foregroundFPS = 500;

		new LwjglApplication(new Game(), config);

	}
}
