package com.mygdx.game.Characters;

import com.badlogic.gdx.utils.Pool;

public class ZombiePool extends Pool<Zombie> {  // Siin hoitakse Zombie'sid, et neid hiljem uuesti joonistada.

    @Override
    protected Zombie newObject() {
        System.out.println("Creating new Zombie in ZombiePool.");
        return new Zombie();
    }
}
