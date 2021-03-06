package me.itsrishi.ld39;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    private static Sound highBattery,lowBattery;

    static {
        highBattery = Gdx.audio.newSound(Gdx.files.local("high_battery.ogg"));
        lowBattery = Gdx.audio.newSound(Gdx.files.local("low_battery.ogg"));
    }
    // Current state
    private int position;
    private float charge;
    private boolean fullCharged;
    private Sprite sprite;
    public boolean isCharging;
    private int retentionTime; // In ms
    private int chargingTime; // In ms
    private float instatntiationTime;

    //Object permanent state
    private int model;

    public Phone(int position, int model, float time) {
        this.instatntiationTime = time;
        this.model = model;
        this.sprite = new Sprite(GameScreen.getPhoneSprites().get(model));
        setPosition(position);
        if((model & Phone.APPUL) != 0) {
            retentionTime = 30;
            chargingTime = 18;
        }
        if((model & Phone.DROID) != 0) {
            retentionTime = 34;
            chargingTime = 18;
        }
        if((model & Phone.OLD_SCHOOL) != 0) {
            retentionTime = 60;
            chargingTime = 24;
        }
        if((model & Phone.BEZEL_LESS) != 0) {
            retentionTime = 42;
            chargingTime = 22;
        }
        if((model & Phone.BRICK) != 0) {
            retentionTime = 70;
            chargingTime = 35;
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
        isCharging = connected;
        if(fullCharged && !isCharging) {
            charge -= delta / retentionTime;
            fullCharged = false;
            return;
        }
        if(connected && charge < 1)
            charge += delta / chargingTime;
        else charge -= delta / retentionTime;

        if(charge < 0.2f && charge + delta /retentionTime > 0.2f) {
            lowBattery.play(1);
        }
        if(charge > 1) {
            if(!fullCharged)
                highBattery.play(0.4f);
            charge = 1.001f;
            fullCharged = true;
        }
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

    public float getInstatntiationTime() {
        return instatntiationTime;
    }

    public boolean isCharging() {
        return isCharging;
    }
}
