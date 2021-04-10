package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AStarPathFindingTest {

    @Test
    public void testAStarPathFindingCalculatePathSimple() {
        World world = new World();
        // Zombie
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 500f, 20f, 10f, 10f,
                "idle up1", world);
        world.addZombieToServerWorldMap(1, zombie);
        // PlayerGameCharacter
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(25f, 25f, 10f, 10f);
        playerGameCharacterRectangle.set(playerGameCharacterRectangle);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                playerGameCharacterRectangle,20f, 20f, 10f, 10f,
                "idle up1", newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(1);

        AStarPathFinding aStarPathFinding = new AStarPathFinding(world, zombie, playerGameCharacter);

        aStarPathFinding.calculatePath();  // Calculate path form node
        assertEquals(17, aStarPathFinding.getSolutionGraphPath().getCount());
    }
}