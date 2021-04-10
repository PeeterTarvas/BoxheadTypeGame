package com.mygdx.game.Screens;

import com.badlogic.gdx.Input;

public class MyTextInputListner implements Input.TextInputListener {

    private MenuScreen menu;

    public MyTextInputListner(MenuScreen menuScreen) {
        this.menu = menuScreen;

    }

    @Override
    public void input(String s) {
        this.menu.setPlayerName(s);

    }

    @Override
    public void canceled() {

    }

}
