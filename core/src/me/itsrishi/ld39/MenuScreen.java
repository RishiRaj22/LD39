package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Rishi Raj
 */

public class MenuScreen implements Screen, InputProcessor {

    SpriteBatch batch = new SpriteBatch();
    ScreenChangeCommunicator communicator;
    boolean firstRun;
    Texture img = new Texture("poster.png");
    Texture help = new Texture("help.png");
    private boolean end, endFirstRun;

    public MenuScreen(ScreenChangeCommunicator communicator) {
        this.communicator = communicator;
        firstRun = true;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        if (end) {
            if (firstRun) {
                if (!endFirstRun) {
                    Gdx.gl.glClearColor(0, 0, 0, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    batch.begin();
                    batch.draw(help, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    batch.end();
                    return;
                }
            }

            batch.dispose();
            communicator.changeScreenTo(new GameScreen(communicator));
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
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
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (end && firstRun && !endFirstRun)
            endFirstRun = true;
        end = true;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (end && firstRun && !endFirstRun)
            endFirstRun = true;
        end = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
