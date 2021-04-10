package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

class ManhattanDistanceHeuristicTest {

    @Test
    public void testManhattanDistanceHeuristicEstimateSimple() {
        World world = mock(World.class);
        TiledMap tiledMap = new TiledMap();
        Node startNode = new Node(new TiledMap(), 1, 0);
        Node endNode = new Node(new TiledMap(), 10, 0);
        ManhattanDistanceHeuristic manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(world);

        assertEquals(10, manhattanDistanceHeuristic.estimate(startNode, endNode));
    }
}