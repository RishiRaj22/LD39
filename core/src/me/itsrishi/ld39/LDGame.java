package me.itsrishi.ld39;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LDGame extends Game implements ScreenChangeCommunicator {
    SpriteBatch batch;
    Texture img;
    ArrayList<Phone> phones;

    @Override
    public void create() {
        AssetManager assetManager = new AssetManager();
        assetManager.load("phones.png", Texture.class);
        assetManager.load("chargers.png", Texture.class);
        setScreen(new GameScreen(this));
    }

    @Override
    public void changeScreenTo(final Screen screen, final float transitionTime, Color transitionColor) {

        setScreen(new TransitionScreen(transitionTime, transitionColor));
        final Screen lastScreen = screen;
        final Runnable newScreen = new Runnable() {
            @Override
            public void run() {
                setScreen(lastScreen);
            }
        };
        if(transitionTime <= 0) {
            Gdx.app.postRunnable(newScreen);
            return;
        }
        final Application app = Gdx.app;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Thread sleeps");
                    Thread.sleep((long)(transitionTime * 1000));
                    System.out.println("Thread wakes");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                app.postRunnable(newScreen);
            }
        });
        thread.start();
    }
}
