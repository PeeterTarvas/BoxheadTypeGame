package com.mygdx.game.AI.MapInfo;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {
    private Node toNode;
    private Node fromNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }

    @Override
    public String toString() {
        return "ConnectionImp{" +
                "toNode=" + toNode +
                ", fromNode=" + fromNode +
                ", cost=" + cost +
                '}';
    }
}
