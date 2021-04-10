package com.mygdx.game.World;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;


public class Headless {

    public static void loadHeadless(World world) {
        LwjglNativesLoader.load();
        Gdx.files = new LwjglFiles();
    }

}
