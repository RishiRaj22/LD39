package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rishi Raj
 */

public class GameScreen implements Screen {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int SCREEN_PHONE_COUNT = 5;
    private static HashMap<Integer, Sprite> phoneSprites;
    private static HashMap<Integer, Sprite> chargerHeads;
    ShapeRenderer renderer;
    Texture phoneTexture, chargerTexture;
    SpriteBatch batch;
    private ArrayList<Phone> phones;
    private ArrayList<Charger> chargers;

    public static HashMap<Integer, Sprite> getPhoneSprites() {
        return phoneSprites;
    }

    public static HashMap<Integer, Sprite> getChargerHeadSprites() {
        return chargerHeads;
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        initPhoneSprites();
        initChargerSprites();// Charger sprite has a dependency on phone sprite. so call phone sprite init first
        Phone phone = new Phone(0, 100, Phone.COLOR_WHITE + Phone.BEZEL_LESS, 4);
        Phone phone2 = new Phone(1, 100, Phone.COLOR_WHITE + Phone.BRICK, 4);
        Phone phone3 = new Phone(2, 100, Phone.COLOR_WHITE + Phone.OLD_SCHOOL, 4);
        Phone phone4 = new Phone(3, 100, Phone.COLOR_WHITE + Phone.APPUL, 4);
        Phone phone5 = new Phone(4, 100, Phone.COLOR_BLACK + Phone.DROID, 4);
        phones = new ArrayList<Phone>();
        chargers = new ArrayList<Charger>();
        phones.add(phone);
        phones.add(phone2);
        phones.add(phone3);
        phones.add(phone4);
        phones.add(phone5);
        Charger charger = new Charger(3, Charger.COLOR_WHITE + Charger.APPUL);
        Charger charger2 = new Charger(2, Charger.COLOR_WHITE + Charger.OLD_SCHOOL);
        Charger charger3 = new Charger(0, Charger.COLOR_BLACK + Charger.USB);
        Charger charger4 = new Charger(1, Charger.COLOR_BLACK + Charger.OLD_SCHOOL);
        chargers.add(charger);
        chargers.add(charger2);
        chargers.add(charger3);
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

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(192f / 255, 108f / 255, 53f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawWires(batch); //Batch begins inside drawWires method
        drawBg(batch);
        drawPhones(batch);
        batch.end();
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
        for (Charger charger : chargers) {
            Sprite sprite = charger.getSprite();
            if((charger.getType() & Charger.COLOR_WHITE) != 0)
                renderer.setColor(Color.WHITE);
            else renderer.setColor(Color.BLACK);
            System.out.println("y: "+sprite.getY());
            renderer.rect(sprite.getX() + charger.getWireXOffset() , 100 + sprite.getOriginY(), 5,HEIGHT - 135);
        }
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        for(Charger charger: chargers) {
            charger.getSprite().draw(batch);
        }
    }

    private void drawPhones(SpriteBatch batch) {
        for (Phone phone : phones) {
            phone.getSprite().draw(batch);
        }
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
