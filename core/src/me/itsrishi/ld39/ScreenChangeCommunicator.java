package me.itsrishi.ld39;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Rishi Raj
 */

public interface ScreenChangeCommunicator {
    void changeScreenTo(Screen screen, float transitionTime, Color transitionColor);
    void changeScreenTo(Screen screen);
}
