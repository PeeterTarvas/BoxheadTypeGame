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
    }

    @Test
    public void testMovePistolBulletBasicDirections() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);

        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());

        newBullet.setDirection("left");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());

        newBullet.setDirection("down");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());

        newBullet.setDirection("right");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
    }

    @Test
    public void movePistolBulletDiagonal() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);

        newBullet.setDirection("up-left");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());

        newBullet.setDirection("down-left");
        newBullet.movePistolBullet();
        assertEquals(71, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());

        newBullet.setDirection("down-right");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(73, newBullet.getBoundingBox().getY());

        newBullet.setDirection("up-right");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
    }

    @Test
    public void testCheckIfPistolBulletIsInWorld() {
        // Both coordinates ok
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        assertTrue(newBullet.checkIfPistolBulletIsInWorld());

        // Both coordinates out
        newBullet.getBoundingBox().setPosition(63, 485);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());

        // X coordinate ok
        newBullet.getBoundingBox().setPosition(100, 485);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());

        // Y coordinate ok
        newBullet.getBoundingBox().setPosition(60, 75);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());
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
    }
}