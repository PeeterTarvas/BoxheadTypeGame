package com.mygdx.game.Weapons;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PistolBulletTest {

    @Test
    public void testMakePistolBullet() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals(5, newBullet.getBoundingBox().getWidth());
        assertEquals(5, newBullet.getBoundingBox().getHeight());
        assertEquals(1, newBullet.getDamage());
        assertEquals("Fire Bullet", newBullet.getBulletTextureString());
        assertEquals("up", newBullet.getDirection());
    }

    @Test
    public void testPistolBulletReset() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        newBullet.reset();
        assertEquals(0, newBullet.getBoundingBox().getX());
        assertEquals(0, newBullet.getBoundingBox().getY());
        assertEquals(0, newBullet.getBoundingBox().getWidth());
        assertEquals(0, newBullet.getBoundingBox().getHeight());
        assertNull(newBullet.getDirection());
    }
}