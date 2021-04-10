package com.mygdx.game.Weapons;

public class Shotgun extends Pistol {

    PistolBullet bulletLeft;
    PistolBullet bulletStraight;
    PistolBullet bulletRight;

    public Shotgun(PistolBullet bulletLeft, PistolBullet bulletStraight, PistolBullet bulletRight) {
        super(bulletStraight);
        this.bulletLeft = bulletLeft;
        this.bulletStraight = bulletStraight;
        this.bulletRight = bulletRight;
    }

    public PistolBullet getBulletLeft() {
        return bulletLeft;
    }

    public PistolBullet getBulletStraight() {
        return bulletStraight;
    }

    public PistolBullet getBulletRight() {
        return bulletRight;
    }

    public String setBulletLeftDirection(String direction) {
        String newDirection = "";
        switch (direction) {
            case "up":
                newDirection = "up-left";
                break;
            case "up-left":
                newDirection = "left";
                break;
            case "left":
                newDirection = "down-left";
                break;
            case "down-left":
                newDirection = "down";
                break;
            case "down":
                newDirection = "down-right";
                break;
            case "down-right":
                newDirection = "right";
                break;
            case "right":
                newDirection = "up-right";
                break;
            case "up-right":
                newDirection = "up";
                break;
        }
        return newDirection;
    }

    public String setBulletRightDirection(String direction) {
        String newDirection = "";

        switch (direction) {
            case "up":
                newDirection = "up-right";
                break;
            case "up-right":
                newDirection = "right";
                break;
            case "right":
                newDirection = "down-right";
                break;
            case "down-right":
                newDirection = "down";
                break;
            case "down":
                newDirection = "down-left";
                break;
            case "down-left":
                newDirection = "left";
                break;
            case "left":
                newDirection = "up-left";
                break;
            case "up-left":
                newDirection = "up";
                break;
        }
        return newDirection;
    }
}
