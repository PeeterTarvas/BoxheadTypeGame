package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZombieTest {

    @Test
    public void testZombieFindPlayerGameCharacterId() {
        World world = mock(World.class);
        world.addGameCharacter(1, mock(PlayerGameCharacter.class));
        world.addGameCharacter(2, mock(PlayerGameCharacter.class));
        world.addGameCharacter(3, mock(PlayerGameCharacter.class));
        Zombie zombie1 = mock(Zombie.class);
        Zombie zombie2 = mock(Zombie.class);
    }
}