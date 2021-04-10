package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AI.Algorithm.AStarPathFinding;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;

public class Zombie extends GameCharacter {

    private int zombieId;
    private static int zombiesCreated;
    private AStarPathFinding aStarPathFinding;
    private int playerGameCharacterId;  // Zombie starts searching PlayerGameCharacter with the given id.
    private static int playerGameCharacterIdCount;
    private Node currentNode;
    private Node nextNode;
    private float deltaX;
    private float deltaY;
    private direction xMovingDirection;
    private direction yMovingDirection;
    private enum direction {
        LEFT, RIGHT, UP, DOWN;
    }

    public Zombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                  float height, String characterTextureString, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, characterTextureString, world);
        this.health = 10;
        this.zombieId = getAndIncrementNextId();
        this.playerGameCharacterId = findPlayerGameCharacterId();
        this.aStarPathFinding = new AStarPathFinding(world, this, world.getGameCharacter(playerGameCharacterId));
        // System.out.println("Zombie " + getZombieId() + "playerGameCharacterId " + playerGameCharacterId);
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public int getZombieId() {
        return zombieId;
    }

    public static int getAndIncrementNextId() {
        return ++zombiesCreated;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public static int getPlayerGameCharacterId() {
        ++playerGameCharacterIdCount;
        if (playerGameCharacterIdCount >= 4) {
            playerGameCharacterIdCount = 1;
        }
        return playerGameCharacterIdCount;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public boolean zombieHasLives() {
        return health > 0;
    }

    public int findPlayerGameCharacterId() {
        int id = 1;
        if (world.getClients().size() == 3) {
            id = getPlayerGameCharacterId();
        }
        return id;
    }

    public void findNextNode() {
        aStarPathFinding.calculatePath();
        if (aStarPathFinding.getSolutionGraphPath().getCount() == 1) {
            nextNode = currentNode;
        } else {
            nextNode = aStarPathFinding.getSolutionGraphPath().get(1);
        }
    }

    public void findNextXAndY() {
        if (nextNode.equals(currentNode)) {
            if (boundingBox.overlaps(world.getGameCharacter(playerGameCharacterId).getBoundingBox())) {
                deltaX = 0f;
                deltaY = 0f;
            } else {
                switch (xMovingDirection) {
                    case LEFT:
                        deltaX = -0.35f;
                        break;
                    case RIGHT:
                        deltaX = 0.35f;
                        break;
                    default:
                        deltaX = 0f;
                        break;
                }

                switch (yMovingDirection) {
                    case DOWN:
                        deltaY = -0.35f;
                        break;
                    case UP:
                        deltaY = 0.35f;
                        break;
                    default:
                        deltaX = 0;
                        break;
                }
            }
        } else {
            if (nextNode.x < currentNode.x) {  // Moves left.
                deltaX = -0.35f;
                xMovingDirection = direction.LEFT;
            } else if (nextNode.x > currentNode.x) {  // Moves right.
                deltaX = 0.35f;
                xMovingDirection = direction.RIGHT;
            } else {
                deltaX = 0f;
            }

            if (nextNode.y < currentNode.y) {  // Moves down.
                deltaY = -0.35f;
                yMovingDirection = direction.DOWN;
            } else if (nextNode.y > currentNode.y) {  // Moves up.
                deltaY = 0.35f;
                yMovingDirection = direction.UP;
            } else {
                deltaY = 0f;
            }
        }
        System.out.println(zombieId);
        System.out.println(boundingBox.overlaps(world.getGameCharacter(playerGameCharacterId).getBoundingBox()));
        System.out.println(xMovingDirection);
        System.out.println(yMovingDirection);
        System.out.println(deltaX);
        System.out.println(deltaY);
    }
}
