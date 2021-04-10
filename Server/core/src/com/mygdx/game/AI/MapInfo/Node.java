package com.mygdx.game.AI.MapInfo;


import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

public class Node {

    public final int x;
    public final int y;
    private boolean isWall;
    private final int index;
    private int type;
    private Array<Connection<Node>> connections;
    public static Integer id = 0;


    public Node(TiledMap map, int x, int y) {
        this.x = x;
        this.y = y;
        this.index = id;
        this.isWall = false;
        this.type = 0;
        this.connections = new Array<>();
        id++;
    }

    public int getIndex () {
        return index;
    }

    public void createConnection(Node toNode, float cost) {
        this.connections.add(new ConnectionImp(this, toNode, cost));
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    @Override
    public String toString() {
        return "Node: ( x: " + x + ", y: " + y + ", index: " + index +")";
    }
    public void setTypeToRegular() {
        this.type = Type.getRegular();
    }

    public static class Type {
        private static final int REGULAR = 1;

        public static int getRegular() {
            return REGULAR;
        }
    }


}
