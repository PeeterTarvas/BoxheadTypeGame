package com.mygdx.game.Weapons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShotgunTest {

    @Test
    public void testShotgunSetBulletLeftDirection() {
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        assertEquals("up-left", shotgun.setBulletLeftDirection("up"));
        assertEquals("left", shotgun.setBulletLeftDirection("up-left"));
        assertEquals("down-left", shotgun.setBulletLeftDirection("left"));
        assertEquals("down", shotgun.setBulletLeftDirection("down-left"));
        assertEquals("down-right", shotgun.setBulletLeftDirection("down"));
        assertEquals("right", shotgun.setBulletLeftDirection("down-right"));
        assertEquals("up-right", shotgun.setBulletLeftDirection("right"));
        assertEquals("up", shotgun.setBulletLeftDirection("up-right"));
    }

    @Test
    public void testShotgunSetBulletRightDirection() {
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        assertEquals("up-right", shotgun.setBulletRightDirection("up"));
        assertEquals("up", shotgun.setBulletRightDirection("up-left"));
        assertEquals("up-left", shotgun.setBulletRightDirection("left"));
        assertEquals("left", shotgun.setBulletRightDirection("down-left"));
        assertEquals("down-left", shotgun.setBulletRightDirection("down"));
        assertEquals("down", shotgun.setBulletRightDirection("down-right"));
        assertEquals("down-right", shotgun.setBulletRightDirection("right"));
        assertEquals("right", shotgun.setBulletRightDirection("up-right"));
    }
}