package me.itsrishi.ld39;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Logger;

import java.util.Random;

import static me.itsrishi.ld39.GameScreen.SCREEN_PHONE_COUNT;
import static me.itsrishi.ld39.GameScreen.getPhoneSprites;

/**
 * @author Rishi Raj
 */

class Phone {

    public static final int COLOR_WHITE = 1;
    public static final int COLOR_BLACK = 2;//WARNING: It will break on adding more colors. See keyDown method for more details
    public static final int OLD_SCHOOL = 4;
    public static final int BEZEL_LESS = 8;
    public static final int BRICK = 16;
    public static final int DROID = 32;
    public static final int APPUL = 64;
    // Current state
    private int position;
    private float charge;
    private Sprite sprite;
    private int retentionTime; // In ms
    private int chargingTime; // In ms

    //Object permanent state
    private int model;

    public Phone(int position, int model) {
        this.model = model;
        this.sprite = new Sprite(GameScreen.getPhoneSprites().get(model));
        setPosition(position);
        if((model & Phone.APPUL) != 0) {
            retentionTime = 24;
            chargingTime = 14;
        }
        if((model & Phone.DROID) != 0) {
            retentionTime = 24;
            chargingTime = 15;
        }
        if((model & Phone.OLD_SCHOOL) != 0) {
            retentionTime = 45;
            chargingTime = 14;
        }
        if((model & Phone.BEZEL_LESS) != 0) {
            retentionTime = 22;
            chargingTime = 14;
        }
        if((model & Phone.BRICK) != 0) {
            retentionTime = 38;
            chargingTime = 24;
        }
        this.charge = 0.5f;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + position * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
    }

    public void updateCharge(float delta, boolean connected) throws ChargeException {
        if(connected)
            charge += delta / chargingTime;
        else charge -= delta / retentionTime;
        if (charge >= 1)
            throw new ChargeException(this, ChargeException.OVERCHARGE);
        if (charge < 0)
            throw new ChargeException(this, ChargeException.DISCHARGE);
    }

    public int getModel() {
        return model;
    }

    public float getCharge() {
        return charge;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getBatteryYOffset() {
        if((model & Phone.OLD_SCHOOL) != 0)
            return 70;
        if((model & Phone.DROID) != 0)
            return 20;
        else return 35;
    }

    public float getBatteryXOffset() {
        if((model & Phone.BEZEL_LESS) != 0 || (model & Phone.OLD_SCHOOL) != 0)
            return sprite.getWidth()* 0.1f;
        if((model & Phone.OLD_SCHOOL) != 0)
            return sprite.getWidth() * 0.06f;
        return sprite.getWidth() * 0.08f;
    }

    public float getBatteryMaxHeight() {
        if((model & Phone.OLD_SCHOOL) != 0)
            return 30;
        return 50 ;
    }
}
