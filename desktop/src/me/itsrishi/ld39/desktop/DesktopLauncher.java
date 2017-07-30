package me.itsrishi.ld39.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.itsrishi.ld39.LDGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1120;
		config.height = 600;
        config.resizable = false;
		config.title = "Battery Low";
		config.addIcon("icon_128.png", Files.FileType.Local);
		config.addIcon("icon_32.png", Files.FileType.Local);
		config.addIcon("icon_16.png", Files.FileType.Local);
		new LwjglApplication(new LDGame(), config);
	}
}
