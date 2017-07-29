package me.itsrishi.ld39;

import com.badlogic.gdx.graphics.g2d.Sprite;

import static me.itsrishi.ld39.GameScreen.SCREEN_PHONE_COUNT;

/**
 * @author Rishi Raj
 */

public class Charger {
    public static final int COLOR_WHITE = 1;
    public static final int COLOR_BLACK = 2;
    public static final int OLD_SCHOOL = 4;
    public static final int APPUL = 8;
    public static final int USB = 16;
    private int position;
    private int type;
    private Sprite sprite;

    public Charger(int position, int type) {
        this.type = type;
        sprite = new Sprite(GameScreen.getChargerHeadSprites().get(type));
        setPosition(position);
    }

    public int getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        if (position != -1)
            sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + this.position * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getWireXOffset() {
        if ((type & Charger.OLD_SCHOOL) != 0)
            return sprite.getWidth() * 0.18f;
        if ((type & Charger.APPUL) != 0)
            return sprite.getWidth() * 0.17f;
        if ((type & Charger.USB) != 0 && (type & Charger.COLOR_BLACK) != 0)
            return sprite.getWidth() * 0.095f;
        if ((type & Charger.USB) != 0)
            return sprite.getWidth() * 0.16f;
        ;
        System.out.println("x offset not implemented");
        return 0;
    }

    public boolean isModelCompatible(int model) {
        if (model % 4 != type % 4)
            return false;
        if ((model & Phone.BRICK) != 0 || (model & Phone.DROID) != 0 || (model & Phone.BEZEL_LESS) != 0) {
            if ((type & USB) != 0)
                return true;
            return false;
        }
        if ((model & Phone.APPUL) != 0) {
            if ((type & APPUL) != 0)
                return true;
            else return false;
        }
        if ((model & Phone.OLD_SCHOOL) != 0) {
            if ((type & OLD_SCHOOL) != 0)
                return true;
            else return false;
        }
        System.out.println("Model compatibility not implemented");
        return false;
    }
}
