package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Rishi Raj
 */

public class GameOverScreen implements Screen, InputProcessor {
    private final ScreenChangeCommunicator communicator;
    ShapeRenderer renderer = new ShapeRenderer();
    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();
    private boolean restart;
    private boolean isHighScore;
    private int hiscore;
    private int score;

    public GameOverScreen(ScreenChangeCommunicator communicator, int score) {
        this.communicator = communicator;
        this.score = score;
        Preferences preferences = Gdx.app.getPreferences("stats");
        hiscore = preferences.getInteger("hiscore");
        if (score > hiscore) {
            hiscore = score;
            isHighScore = true;
        }
        preferences.putInteger("hiscore", hiscore);
        preferences.flush();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (restart) {
            dispose();
            GameScreen screen = new GameScreen(communicator);
            communicator.changeScreenTo(screen);
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setAutoShapeType(true);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //Add render shapes
        renderer.end();
        batch.begin();
        font.draw(batch, "Game over: Press R to restart", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        if (isHighScore)
            font.draw(batch, "New High Score: " + score, Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 + 40);
        else {
            font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 + 40);
            font.draw(batch, "High Score: " + hiscore, Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 + 100);
        }
        batch.end();

        Gdx.input.setInputProcessor(this);
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
        font.dispose();
        batch.dispose();
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.R)
            restart = true;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        restart = true;
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
