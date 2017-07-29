package me.itsrishi.ld39;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Logger;

import static me.itsrishi.ld39.GameScreen.SCREEN_PHONE_COUNT;

/**
 * @author Rishi Raj
 */

class Phone {

    public static final int COLOR_WHITE = 1;
    public static final int COLOR_BLACK = 2;
    public static final int OLD_SCHOOL = 4;
    public static final int BEZEL_LESS = 8;
    public static final int BRICK = 16;
    public static final int DROID = 32;
    public static final int APPUL = 64;
    // Current state
    private int position;
    private float charge;
    private Sprite sprite;
    private int retentionTime = 4000; // In ms
    //Object permanent state
    private int model;

    public Phone(int position, float charge, int model, int retentionTime) {
        this.charge = charge;
        this.model = model;
        this.retentionTime = retentionTime;
        Logger logger = new Logger("phone");
        logger.debug("model #" + model);
        System.out.println("model #" + model);
        this.sprite = new Sprite(GameScreen.getPhoneSprites().get(model));
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + position * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
        System.out.println(sprite.getY());
    }

    public void updateCharge(float delta, boolean connected) throws ChargeException {
        charge -= delta / retentionTime * 100;
        if (charge >= 100)
            throw new ChargeException(this, ChargeException.OVERCHARGE);
        if (charge < 0)
            throw new ChargeException(this, ChargeException.DISCHARGE);
    }

    public int getCharge() {
        return (int) (charge + 1);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
