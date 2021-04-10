package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.World.World;

public class AStarPathFinding {

    private IndexedAStarPathFinder<Node> aStarPathFinder;
    private World world;
    // Pass in the path and the algorithm will fill it with data.
    private SolutionGraphPath solutionGraphPath;
    private ManhattanDistanceHeuristic manhattanDistanceHeuristic;

    // Characters.
    private Zombie zombie;
    private PlayerGameCharacter playerGameCharacter;

    public AStarPathFinding(World world, Zombie zombie, PlayerGameCharacter playerGameCharacter) {
        this.world = world;
        this.zombie = zombie;
        this.playerGameCharacter = playerGameCharacter;
        this.aStarPathFinder = new IndexedAStarPathFinder<Node>(world.getGraph(), false);
        this.solutionGraphPath = new SolutionGraphPath();
        this.manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(world);
    }

    public SolutionGraphPath getSolutionGraphPath() {
        return solutionGraphPath;
    }

    public void calculatePath() {
        // Clear graph.
        solutionGraphPath.clear();
        // Zombie x and y.
        int startX = (int) zombie.getBoundingBox().getX();
        int startY = (int) zombie.getBoundingBox().getY();
        // Target (PlayerGameCharacter) x and y.
        int targetX = (int) playerGameCharacter.getBoundingBox().getX();
        int targetY = (int) playerGameCharacter.getBoundingBox().getY();

        // System.out.println("Zombie x: " + startX + " y " + startY);
        // System.out.println("Target x: " + targetX + " y " + targetY);

        world.getGraph().setWorld(world);
        // Convert Zombie x and y into a node.
        Node startNode = world.getGraph().getNodeByXAndY(startX, startY);
        zombie.setCurrentNode(startNode);
        // Convert PlayerGameCharacter x and y into a node:
        Node endNode = world.getGraph().getNodeByXAndY(targetX, targetY);

        // System.out.println("Start node " + startNode);
        // System.out.println("End node " + endNode);

        aStarPathFinder.searchNodePath(startNode, endNode, manhattanDistanceHeuristic, solutionGraphPath);
        // System.out.println("Path: " + solutionGraphPath.getCount());
    }
}
