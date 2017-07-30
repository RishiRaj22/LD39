package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Rishi Raj
 */

public class TransitionScreen implements Screen {
    float time = 0;
    private float transitionTime = 1;
    private Color transitionColor;
    ShapeRenderer renderer = new ShapeRenderer();

    public TransitionScreen(float transitionTime, Color transitionColor) {
        this.transitionTime = transitionTime;
        this.transitionColor = transitionColor;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(time < 1) {
            time += delta/transitionTime;
            if (time > 1)
                time = 1;
            renderer.setAutoShapeType(true);
            renderer.begin();
            renderer.set(ShapeRenderer.ShapeType.Filled);
            float alpha = (float)Math.pow(time, 2.5);
            renderer.setColor(transitionColor.r, transitionColor.g, transitionColor.b, alpha);
            System.out.printf("%f %f %f %f\n",transitionColor.r, transitionColor.g, transitionColor.b, alpha);
            renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            renderer.end();
        }
        else System.out.println("Transition completed");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
