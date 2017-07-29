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
        sprite = GameScreen.getChargerHeadSprites().get(type);
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
        sprite.setX(GameScreen.WIDTH / (2 * SCREEN_PHONE_COUNT + 1) + this.position * GameScreen.WIDTH / (SCREEN_PHONE_COUNT));
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getWireXOffset() {
        if((type & Charger.OLD_SCHOOL) != 0)
            return sprite.getWidth()* 0.18f;
        if((type & Charger.APPUL) != 0)
            return sprite.getWidth()* 0.17f;
        if((type & Charger.USB) != 0)
            return sprite.getWidth()* 0.09f;;
        System.out.println("x offset not implemented");
        return 0;
    }
}
