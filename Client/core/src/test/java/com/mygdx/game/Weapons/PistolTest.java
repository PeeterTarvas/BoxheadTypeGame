package com.mygdx.game.Weapons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PistolTest {

    /**
     * Test class Pistol static method createPistol.
     *
     * Method makes a Pistol instance and makes a PistolBullet instance for it.
     */
    @Test
    public void testPistolCreatePistol() {
        Pistol pistol = Pistol.createPistol(50f, 70f);

        assertEquals(PistolBullet.class, pistol.getBulletStraight().getClass());
        assertEquals(50f, pistol.getBulletStraight().getBoundingBox().getX());
        assertEquals(70f, pistol.getBulletStraight().getBoundingBox().getY());
        assertEquals("Fire Bullet", pistol.getBulletStraight().getBulletTextureString());
        assertEquals("up", pistol.getBulletStraight().getDirection());
        assertEquals(1, pistol.getBulletStraight().getDamage());
    }
}