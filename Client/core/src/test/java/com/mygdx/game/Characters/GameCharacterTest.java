package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameCharacterTest {

    @Test
    public void testGameCharacterMoveToPos() {
        Rectangle boundingBox = new Rectangle(20f, 20f, 10f, 10f);
        GameCharacter gameCharacter = new GameCharacter(1f, boundingBox, 20f, 20f,
                10f, 10f, "idle up1");

        gameCharacter.moveToNewPos(25f, 45f);
        assertEquals(25f, gameCharacter.getBoundingBox().getX());
        assertEquals(45f, gameCharacter.getBoundingBox().getY());
    }
}