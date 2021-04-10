package com.mygdx.game.Weapons;

public class Pistol {

    PistolBullet bullet;

    public Pistol(PistolBullet bullet) {
        this.bullet = bullet;
    }

    public PistolBullet getBulletStraight() { return bullet; }
}
