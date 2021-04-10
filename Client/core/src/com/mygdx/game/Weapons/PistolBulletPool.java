package com.mygdx.game.Weapons;

import com.badlogic.gdx.utils.Pool;

public class PistolBulletPool extends Pool<PistolBullet> {  // Siin hoitakse PistolBulleteid, et neid hiljem uuesti joonistada.

    @Override
    protected PistolBullet newObject() {
        return new PistolBullet();
    }
}
