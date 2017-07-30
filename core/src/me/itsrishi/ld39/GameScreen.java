package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Rishi Raj
 */

public class GameScreen implements Screen, InputProcessor {
    public static final int WIDTH = 1120;
    public static final int HEIGHT = 600;
    public static final int SCREEN_PHONE_COUNT = 7;
    public static Color bgColor = new Color(192f / 255, 108f / 255, 53f / 255, 1);
    private static HashMap<Integer, Sprite> phoneSprites;
    private static HashMap<Integer, Sprite> chargerHeads;
    private float gameSpeed;
    private final ScreenChangeCommunicator communicator;
    ShapeRenderer renderer;
    Texture phoneTexture, chargerTexture, bgTexture;
    SpriteBatch batch;
    BitmapFont font = new BitmapFont(Gdx.files.internal("40.fnt"),false);
    Sound pluggedIn, pluggedOut,phoneAdd;
    private Random random = new Random();
    private boolean begun = false;
    private ArrayList<Phone> phones;
    private ArrayList<Charger> chargers;
    private int focussed = 0;
    private boolean focussedTop = true;
    private Charger chargerFocussed;
    private int score;
    private int lives = 3;
    private float time = 0;
    private static final float TEXT_DISP_TIME = 3;
    private ArrayList<Float> addPhones = new ArrayList<Float>();
    // change (e.g. + 100 -1 for life) , position(0 - 5), time (timeBegan)
    private ArrayList<Float> textDisp = new ArrayList<Float>();
    private Sprite handSprite;


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
        font.setColor(Color.BLACK);
        pluggedIn = Gdx.audio.newSound(Gdx.files.internal("plug_in.ogg"));
        pluggedOut = Gdx.audio.newSound(Gdx.files.internal("plug_out.ogg"));
        phoneAdd = Gdx.audio.newSound(Gdx.files.internal("phone_add.ogg"));
        Gdx.input.setInputProcessor(this);
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        bgTexture = new Texture("bg.png");
        textDisp = new ArrayList<Float>();
        handSprite = new Sprite(new Texture("hand.png"));
        initPhoneSprites();
        initChargerSprites();// Charger sprite has a dependency on phone sprite. so call phone sprite init first
        phones = new ArrayList<Phone>();
        chargers = new ArrayList<Charger>();
        ArrayList<Integer> poses = new ArrayList<Integer>();
        poses.add(0);
        poses.add(1);
        poses.add(2);
        poses.add(3);
        poses.add(4);
        poses.add(5);
        poses.add(6);
        schedulePhoneAdd(chooseRandomFromArrayList(poses),0);
        schedulePhoneAdd(chooseRandomFromArrayList(poses),0);
        schedulePhoneAdd(chooseRandomFromArrayList(poses),0);
        schedulePhoneAdd(chooseRandomFromArrayList(poses), 5);
        schedulePhoneAdd(chooseRandomFromArrayList(poses), 12);
        schedulePhoneAdd(chooseRandomFromArrayList(poses), 9);
        schedulePhoneAdd(chooseRandomFromArrayList(poses), 16);

        int val = random.nextBoolean() ? Charger.COLOR_BLACK : Charger.COLOR_WHITE;
        Charger charger0 = new Charger(0, blackOrWhite() + Charger.USB);
        Charger charger1 = new Charger(1, blackOrWhite() + Charger.USB);
        Charger charger2 = new Charger(2, blackOrWhite() + Charger.USB);
        Charger charger3 = new Charger(3, blackOrWhite() + Charger.APPUL);
        Charger charger4 = new Charger(4, blackOrWhite() + Charger.OLD_SCHOOL);


        chargers.add(charger0);
        chargers.add(charger1);
        chargers.add(charger2);
        chargers.add(charger3);
        chargers.add(charger4);
    }
    private int chooseRandomFromArrayList(ArrayList<Integer> integers) {
        int pos = random.nextInt(integers.size());
        int val = integers.get(pos);
        integers.remove(pos);
        return val;
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

    private int blackOrWhite() {
        return random.nextBoolean() ? Charger.COLOR_BLACK : Charger.COLOR_WHITE;
    }

    private void addPhone(int position) {
        phoneAdd.play();
        int old = 0, usb = 0, appul = 0;
        for (Phone phone : phones) {
            if ((phone.getModel() & Phone.APPUL) != 0)
                appul++;
            else if ((phone.getModel() & Phone.OLD_SCHOOL) != 0)
                old++;
            else usb++;
        }
        int val = blackOrWhite();
        boolean choose = random.nextBoolean();
        if (choose && appul == 0)
            val += Phone.APPUL;
        else if (!choose && old == 0)
            val += Phone.OLD_SCHOOL;
        else
            val += random.nextBoolean() ? (random.nextBoolean() ? Phone.DROID : Phone.BRICK) : random.nextBoolean() ? Phone.BEZEL_LESS : (random.nextBoolean() ? Phone.DROID : Phone.BRICK);
        Phone phone = new Phone(position, val, time);
        phones.add(phone);
    }

    private void schedulePhoneAdd(int position, float delay) {
        addPhones.add(time + delay);
        addPhones.add((float) position);
    }

    private void submitPhone(Phone phone) {
        if(phone.getCharge() < 1 && phone.getCharge() > 0)
            return;
        int pos = phone.getPosition();
        int rate = 1;
        if (time - phone.getInstatntiationTime() < 15)
            rate = 10;
        else if (time - phone.getInstatntiationTime() < 18)
            rate = 5;
        else if (time - phone.getInstatntiationTime() < 24)
            rate = 3;
        else if (time - phone.getInstatntiationTime() < 50)
            rate = 2;

        float change = 100 * rate;

        if(phone.getCharge() > 0) {
            score += change;
            textDisp.add((float) change);
            textDisp.add((float) phone.getPosition());
            textDisp.add((float) time);
        }

        phones.remove(phone);
        schedulePhoneAdd(pos, 2 + (random.nextBoolean() ? 0 : 1));
    }

    private void initChargerSprites() {
        chargerTexture = new Texture("chargers.png");
        chargerHeads = new HashMap<Integer, Sprite>(6);
        chargerHeads.put(Charger.USB + Charger.COLOR_BLACK, new Sprite(new TextureRegion(phoneTexture, 0, 230, 350, 670)));
        Sprite oldSchoolWhite = new Sprite(new TextureRegion(chargerTexture, 0, 0, 340, 240));
        chargerHeads.put(Charger.OLD_SCHOOL + Charger.COLOR_WHITE, oldSchoolWhite);
        Sprite oldSchoolBlack = new Sprite(new TextureRegion(chargerTexture, 370, 0, 323, 240));
        chargerHeads.put(Charger.OLD_SCHOOL + Charger.COLOR_BLACK, oldSchoolBlack);
        chargerHeads.put(Charger.APPUL + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 70, 500, 185, 410)));
        chargerHeads.put(Charger.APPUL + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 70, 500, 185, 410)));
        chargerHeads.put(Charger.APPUL + Charger.COLOR_BLACK, new Sprite(new TextureRegion(chargerTexture, 400, 500, 200, 410)));
        chargerHeads.put(Charger.USB + Charger.COLOR_WHITE, new Sprite(new TextureRegion(chargerTexture, 740, 500, 200, 450)));
        for (Sprite sprite : chargerHeads.values()) {
            float desiredWidth = WIDTH / (2 * SCREEN_PHONE_COUNT + 1);
            sprite.setOrigin(0, 0);
            float scale = desiredWidth / sprite.getWidth();
            sprite.setScale(scale, scale);
            sprite.setY(HEIGHT - sprite.getHeight() * scale + 30);
        }
        oldSchoolBlack.setY(oldSchoolBlack.getY() - 10);
        oldSchoolWhite.setY(oldSchoolWhite.getY() - 10);

    }

    private void update(float delta) {
        if (!begun) {
            return;
        }
        time += delta;
        updatePhones(delta);
        float SPEED_CHANGE_TIME = 240;
        float pro = (float) (time / SPEED_CHANGE_TIME * 0.5);
        if(pro > 1) pro = 1;
        gameSpeed = 1 + pro;
    }

    private void updatePhones(float delta) {
        if (addPhones != null && addPhones.size() != 0) {
            if (time > addPhones.get(0)) {
                float val = addPhones.get(1);
                addPhone((int) val);
                addPhones.remove(0);
                addPhones.remove(0);
            }
        }
        outer:
        for (int i = 0; i < phones.size(); i++) {
            Phone phone = phones.get(i);
            try {
                for (Charger charger : chargers) {
                    if (charger.getPosition() == phone.getPosition() && charger.isModelCompatible(phone.getModel())) {
                        phone.updateCharge(delta * gameSpeed, true);
                        continue outer;
                    }
                }
                phone.updateCharge(delta * gameSpeed, false);
            } catch (ChargeException e) {
                submitPhone(phone);
                    lives--;
                if (lives <= 0) {
                    GameOverScreen screen = new GameOverScreen(communicator, score);
                    dispose();
                    communicator.changeScreenTo(screen);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(bgTexture, 0, 0, WIDTH, HEIGHT);
        batch.end();
        drawWires(batch); //Batch begins inside drawWires method
        if (!begun)
            font.draw(batch, "Press any key to start game", WIDTH / 2, HEIGHT / 2);
        else {
            font.draw(batch, "Score :" + score, 20, HEIGHT - 30);
            font.draw(batch, "Lives :" + lives, WIDTH - 180, HEIGHT - 30);
        }
        drawPhones(batch); //Batch ends inside drawPhones method

        batch.begin();
        drawHand();
        drawPopupText();
        batch.end();

        update(delta);
    }

    private void drawPopupText() {
        if(textDisp.size() <= 0) {
            return;
        }
        if(textDisp.get(2) + TEXT_DISP_TIME < time) {
            textDisp.remove(0);
            textDisp.remove(0);
            textDisp.remove(0);
        }
            for(int i = 0; i < textDisp.size(); i+=3) {
            float position = textDisp.get(i + 1);
            float x = GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + position * GameScreen.WIDTH / (SCREEN_PHONE_COUNT);
            font.draw(batch,(textDisp.get(0) < 0) ?"Life lost":String.format("+%4.0f",textDisp.get(i)),x,HEIGHT/3);
        }
    }


    private void drawHand() {
        float x = GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + focussed * GameScreen.WIDTH / (SCREEN_PHONE_COUNT);
        handSprite.setX(x - 10);
        handSprite.setY(5);
        if (focussedTop) {
            handSprite.setY(HEIGHT - handSprite.getHeight() - 20);
        }
        if (focussedTop && chargerFocussed != null)
            handSprite.setY(handSprite.getY() - 85);
        handSprite.draw(batch);
        handSprite.setY(handSprite.getY() + 85);
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
            if (!hasPhone)
                continue;
            Sprite sprite = charger.getSprite();
            if ((charger.getType() & Charger.COLOR_WHITE) != 0)
                renderer.setColor(Color.WHITE);
            else renderer.setColor(Color.BLACK);
            renderer.rect(sprite.getX() + charger.getWireXOffset(), 20, 5, HEIGHT - 35 );
        }
        renderer.end();
        batch.begin();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        for (Charger charger : chargers) {
            if (charger.getPosition() < 0) {
                Sprite sprite = charger.getSprite();
                float y = sprite.getY();
                sprite.setY(HEIGHT - 200);
                sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + focussed * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
                charger.getSprite().draw(batch);
                sprite.setY(y);
            } else charger.getSprite().draw(batch);
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
            renderer.setColor(1 - phone.getCharge(),phone.getCharge(),0,1);
            if((int)phone.getCharge() == 1) {
                renderer.rect(sprite.getX() + phone.getBatteryXOffset(), 160, 10, 30);
                renderer.rect(sprite.getX() + phone.getBatteryXOffset(), 160 - 10, 10, 5);
            }
            renderer.rect(sprite.getX() + phone.getBatteryXOffset(), phone.getBatteryYOffset(), 25, phone.getBatteryMaxHeight() * phone.getCharge());

            if (phone.getCharge() < 0.2f && !phone.isCharging) {
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
                Phone temp = null;
                for (int i = 0; i < phones.size(); i++) {
                    Phone phone = phones.get(i);
                    if (phone.getPosition() == focussed) {
                        temp = phone;
                    }
                }
                if (temp != null)
                    submitPhone(temp);
                return true;
            }
            if (chargerFocussed == null) {
                for (Charger charger : chargers) {
                    if (charger.getPosition() == focussed) {
                        pluggedOut.play();
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
                                pluggedIn.play();
                                chargerFocussed = charger;
                                chargerFocussed.setPosition(-1);
                                return true;
                            }
                        }
                    }
                    chargerFocussed.setPosition(focussed);
                    chargerFocussed = charger;
                    pluggedIn.play();
                    chargerFocussed.setPosition(-1);
                    return true;
                }
            }
            for (Phone phone : phones) {
                if (phone.getPosition() == focussed) {
                    if (chargerFocussed.isModelCompatible(phone.getModel())) {
                        chargerFocussed.setPosition(focussed);
                        chargerFocussed = null;
                        pluggedIn.play();
                        return true;
                    }
                }
            }
            chargerFocussed.setPosition(focussed);
            pluggedIn.play();
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
