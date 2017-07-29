package me.itsrishi.ld39;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class LDGame extends Game {
    SpriteBatch batch;
    Texture img;
    ArrayList<Phone> phones;

    @Override
    public void create() {
        AssetManager assetManager = new AssetManager();
        assetManager.load("phones.png", Texture.class);
        assetManager.load("chargers.png", Texture.class);
        setScreen(new GameScreen());
    }
}
