package me.itsrishi.ld39;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rishi Raj
 */

public class GameScreen implements Screen, InputProcessor {
    public static final int WIDTH = 1120;
    public static final int HEIGHT = 600;
    public static final int SCREEN_PHONE_COUNT = 7;
    private static HashMap<Integer, Sprite> phoneSprites;
    private static HashMap<Integer, Sprite> chargerHeads;
    private final ScreenChangeCommunicator communicator;
    ShapeRenderer renderer;
    Texture phoneTexture, chargerTexture;
    SpriteBatch batch;
    BitmapFont font = new BitmapFont();
    private boolean begun = false;
    private ArrayList<Phone> phones;
    private ArrayList<Charger> chargers;
    private int focussed = 0;
    private boolean focussedTop = true;
    private Charger chargerFocussed;


    public GameScreen(ScreenChangeCommunicator communicator) {
        this.communicator = communicator;
    }

    public static HashMap<Integer, Sprite> getPhoneSprites() {
        return phoneSprites;
    }

    public static HashMap<Integer, Sprite> getChargerHeadSprites() {
        return chargerHeads;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        initPhoneSprites();
        initChargerSprites();// Charger sprite has a dependency on phone sprite. so call phone sprite init first
        Phone phone = new Phone(0, Phone.COLOR_WHITE + Phone.BEZEL_LESS);
        Phone phone2 = new Phone(4, Phone.COLOR_BLACK + Phone.BEZEL_LESS);
        Phone phone3 = new Phone(5, Phone.COLOR_BLACK + Phone.OLD_SCHOOL);
        phones = new ArrayList<Phone>();
        chargers = new ArrayList<Charger>();
        phones.add(phone);
        phones.add(phone2);
        phones.add(phone3);

        Charger charger2 = new Charger(3, Charger.COLOR_BLACK + Charger.OLD_SCHOOL);
        Charger charger4 = new Charger(0, Charger.COLOR_WHITE + Charger.USB);
        Charger charger0 = new Charger(1, Charger.COLOR_BLACK + Charger.USB);
        chargers.add(charger0);
        chargers.add(charger2);
        chargers.add(charger4);
    }

    private void initPhoneSprites() {
        phoneTexture = new Texture("phones.png");
        phoneSprites = new HashMap<Integer, Sprite>(10);

        phoneSprites.put(Phone.COLOR_BLACK + Phone.APPUL, new Sprite(new TextureRegion(phoneTexture, 630, 0, 310, 512)));
        phoneSprites.put(Phone.COLOR_BLACK + Phone.DROID, new Sprite(new TextureRegion(phoneTexture, 940, 0, 310, 512)));
        phoneSprites.put(Phone.COLOR_BLACK + Phone.BEZEL_LESS, new Sprite(new TextureRegion(phoneTexture, 1230, 0, 250, 512)));
        phoneSprites.put(Phone.COLOR_BLACK + Phone.BRICK, new Sprite(new TextureRegion(phoneTexture, 1480, 0, 280, 512)));
        phoneSprites.put(Phone.COLOR_BLACK + Phone.OLD_SCHOOL, new Sprite(new TextureRegion(phoneTexture, 1760, 0, 250, 512)));

        phoneSprites.put(Phone.COLOR_WHITE + Phone.APPUL, new Sprite(new TextureRegion(phoneTexture, 630, 512, 310, 512)));
        phoneSprites.put(Phone.COLOR_WHITE + Phone.DROID, new Sprite(new TextureRegion(phoneTexture, 940, 512, 310, 512)));
        phoneSprites.put(Phone.COLOR_WHITE + Phone.BEZEL_LESS, new Sprite(new TextureRegion(phoneTexture, 1230, 512, 250, 512)));
        phoneSprites.put(Phone.COLOR_WHITE + Phone.BRICK, new Sprite(new TextureRegion(phoneTexture, 1480, 512, 280, 512)));
        phoneSprites.put(Phone.COLOR_WHITE + Phone.OLD_SCHOOL, new Sprite(new TextureRegion(phoneTexture, 1760, 512, 250, 512)));
        for (Sprite sprite : phoneSprites.values()) {
            sprite.setOrigin(0, 0);
            float desiredWidth = WIDTH / (2 * SCREEN_PHONE_COUNT + 1);
            float scale = desiredWidth / sprite.getWidth();
            sprite.setScale(scale, scale);
        }
    }

    private void initChargerSprites() {
        chargerTexture = new Texture("chargers.png");
        chargerHeads = new HashMap<Integer, Sprite>(6);
        chargerHeads.put(Charger.USB + Charger.COLOR_BLACK, new Sprite(new TextureRegion(phoneTexture, 0, 230, 350, 670)));
        chargerHeads.put(Charger.OLD_SCHOOL + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 0, 0, 340, 240)));
        chargerHeads.put(Charger.OLD_SCHOOL + Charger.COLOR_BLACK, new Sprite(new TextureRegion(chargerTexture, 370, 0, 323, 240)));
        chargerHeads.put(Charger.APPUL + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 70, 500, 185, 410)));
        chargerHeads.put(Charger.APPUL + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 70, 500, 185, 410)));
        chargerHeads.put(Charger.APPUL + Charger.COLOR_BLACK, new Sprite(new TextureRegion(chargerTexture, 400, 500, 200, 410)));
        chargerHeads.put(Charger.USB + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 740, 500, 200, 450)));
        for (Sprite sprite : chargerHeads.values()) {
            float desiredWidth = WIDTH / (2 * SCREEN_PHONE_COUNT + 1);
            sprite.setOrigin(0, 0);
            float scale = desiredWidth / sprite.getWidth();
            sprite.setScale(scale, scale);
            sprite.setY(HEIGHT - sprite.getHeight() * scale);
        }
    }

    private void update(float delta) {
        if (!begun) {
            return;
        }
        try {
            updatePhones(delta);
        } catch (ChargeException e) {
            GameOverScreen screen = new GameOverScreen(communicator);
            dispose();
            communicator.changeScreenTo(screen,0.8f,Color.BLACK);
        }
    }

    private void updatePhones(float delta) throws ChargeException {
        outer:
        for (Phone phone : phones) {
            for (Charger charger : chargers) {
                if (charger.getPosition() == phone.getPosition() && charger.isModelCompatible(phone.getModel())) {
                    phone.updateCharge(delta, true);
                    continue outer;
                }
            }
            phone.updateCharge(delta, false);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(192f / 255, 108f / 255, 53f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawFocussed();
        drawWires(batch); //Batch begins inside drawWires method
        drawBg(batch);
        if (!begun)
            font.draw(batch, "Press any key to start game", WIDTH / 2, HEIGHT / 2);
        drawPhones(batch); //Batch ends inside drawPhones method
        update(delta);
    }

    private void drawSideBar(SpriteBatch batch) {

    }

    private void drawFocussed() {
        renderer.setAutoShapeType(true);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (focussedTop)
            renderer.rect(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + focussed * GameScreen.WIDTH / (SCREEN_PHONE_COUNT), HEIGHT - 20, GameScreen.WIDTH / SCREEN_PHONE_COUNT, 20);
        else
            renderer.rect(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + focussed * GameScreen.WIDTH / (SCREEN_PHONE_COUNT), 0, GameScreen.WIDTH / SCREEN_PHONE_COUNT, 20);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawBg(SpriteBatch batch) {
    }

    private void drawWires(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_ARRAY_BUFFER_BINDING);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setAutoShapeType(true);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        outer:
        for (Charger charger : chargers) {
            boolean hasPhone = false;
            if (charger.getPosition() < 0)
                continue;
            for (Phone phone : phones) {
                if (phone.getPosition() == charger.getPosition()) {
                    hasPhone = true;
                    if (!charger.isModelCompatible(phone.getModel()))
                        continue outer;
                }
            }
            if(!hasPhone)
                continue;
            Sprite sprite = charger.getSprite();
            if ((charger.getType() & Charger.COLOR_WHITE) != 0)
                renderer.setColor(Color.WHITE);
            else renderer.setColor(Color.BLACK);
            renderer.rect(sprite.getX() + charger.getWireXOffset(), 100 + sprite.getOriginY(), 5, HEIGHT - 135);
        }
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        for (Charger charger : chargers) {
            if (charger.getPosition() < 0) {
                Sprite sprite = charger.getSprite();
                sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + focussed * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
                charger.getSprite().draw(batch);
            }
            charger.getSprite().draw(batch);
        }
    }

    private void drawPhones(SpriteBatch batch) {
        for (Phone phone : phones) {
            phone.getSprite().draw(batch);
        }
        batch.end();
        Gdx.gl.glEnable(GL20.GL_ARRAY_BUFFER_BINDING);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setAutoShapeType(true);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        for (Phone phone : phones) {
            Sprite sprite = phone.getSprite();
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(sprite.getX() + phone.getBatteryXOffset(), phone.getBatteryYOffset(), 25, phone.getBatteryMaxHeight());
            renderer.rect(sprite.getX() + phone.getBatteryXOffset() + 7, phone.getBatteryYOffset() + phone.getBatteryMaxHeight() + 3, 25 - 2 * 7, 2);
            renderer.setColor(Color.GREEN);
            renderer.rect(sprite.getX() + phone.getBatteryXOffset(), phone.getBatteryYOffset(), 25, phone.getBatteryMaxHeight() * phone.getCharge());

            if (phone.getCharge() > 0.75f || phone.getCharge() < 0.25f) {
                renderer.setColor(Color.RED);
                renderer.rect(sprite.getX() + phone.getBatteryXOffset(), 160, 10, 30);
                renderer.rect(sprite.getX() + phone.getBatteryXOffset(), 160 - 10, 10, 5);
            }
        }
        renderer.end();
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
        chargerTexture.dispose();
        phoneTexture.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!begun)
            begun = true;
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            focussed = --focussed < 0 ? SCREEN_PHONE_COUNT - 1 : focussed;
            return true;
        }
        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            focussed = (focussed + 1) % SCREEN_PHONE_COUNT;
            return true;
        }
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            focussedTop = false;
        }
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            focussedTop = true;
        }
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
            if (!focussedTop) {
                return true;
            }
            if (chargerFocussed == null) {
                for (Charger charger : chargers) {
                    if (charger.getPosition() == focussed) {
                        chargerFocussed = charger;
                    }
                }
                if (chargerFocussed != null)
                    chargerFocussed.setPosition(-1);
                return true;
            }
            for (Charger charger : chargers) {
                if (charger.getPosition() == focussed) {
                    for (Phone phone : phones) {
                        if (phone.getPosition() == focussed) {
                            if (chargerFocussed.isModelCompatible(phone.getModel())) {
                                chargerFocussed.setPosition(focussed);
                                chargerFocussed = charger;
                                chargerFocussed.setPosition(-1);
                                return true;
                            }
                        }
                    }
                    chargerFocussed.setPosition(focussed);
                    chargerFocussed = charger;
                    chargerFocussed.setPosition(-1);
                    return true;
                }
            }
            for (Phone phone : phones) {
                if (phone.getPosition() == focussed) {
                    if (chargerFocussed.isModelCompatible(phone.getModel())) {
                        chargerFocussed.setPosition(focussed);
                        chargerFocussed = null;
                        return true;
                    }
                }
            }
            chargerFocussed.setPosition(focussed);
            chargerFocussed = null;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
        return false;
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
