package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;

public class ManhattanDistanceHeuristic implements Heuristic<Node> {

    public World world;

    public ManhattanDistanceHeuristic(World world) {
        this.world = world;
    }

    @Override
    public float estimate(Node node, Node endNode) {
        int startIndex = node.getIndex();
        int endIndex = endNode.getIndex();

        int nodeY = startIndex / world.getTiledMap().getProperties().get("width", Integer.class);
        int nodeX = startIndex % world.getTiledMap().getProperties().get("width", Integer.class);

        int endNodeY = endIndex / world.getTiledMap().getProperties().get("width", Integer.class);
        int endNodeX = endIndex % world.getTiledMap().getProperties().get("width", Integer.class);

        // Manhattan Distance.
        float distance = Math.abs(nodeX - endNodeX) + Math.abs(nodeY - endNodeY);
        return distance;
    }
}
