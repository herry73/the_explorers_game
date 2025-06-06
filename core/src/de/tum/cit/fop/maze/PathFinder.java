package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Vector2;
import java.util.*;

public class PathFinder {
    private static class Node implements Comparable<Node> {
        Vector2 position;
        Node previous;
        float distance;

        Node(Vector2 position) {
            this.position = position;
            this.distance = Float.POSITIVE_INFINITY;
            this.previous = null;
        }

        @Override
        public int compareTo(Node other) {
            return Float.compare(this.distance, other.distance);
        }
    }

    public static List<Vector2> findPath(Level_1 level, Vector2 start, Vector2 goal) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<String, Node> nodes = new HashMap<>();
        Set<String> visited = new HashSet<>();

        Node startNode = new Node(start);
        startNode.distance = 0;
        queue.add(startNode);
        nodes.put(getKey(start), startNode);

        // Main Dijkstra loop
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String currentKey = getKey(current.position);

            if (visited.contains(currentKey)) {
                continue;
            }

            visited.add(currentKey);

            if (current.position.epsilonEquals(goal, 0.1f)) {
                return reconstructPath(current);
            }

            for (Vector2 neighborPos : getNeighbors(current.position)) {
                String neighborKey = getKey(neighborPos);
                if (visited.contains(neighborKey) ||
                        !level.isPathAtPosition(neighborPos.x * 32, neighborPos.y * 32)) {
                    continue;
                }

                Node neighbor = nodes.computeIfAbsent(neighborKey, k -> new Node(neighborPos));

                float newDist = current.distance + 1; // Using 1 as the cost between adjacent tiles

                if (newDist < neighbor.distance) {
                    neighbor.distance = newDist;
                    neighbor.previous = current;

                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }

    private static String getKey(Vector2 position) {
        return position.x + "," + position.y;
    }

    private static List<Vector2> getNeighbors(Vector2 position) {
        List<Vector2> neighbors = new ArrayList<>();
        neighbors.add(new Vector2(position.x + 1, position.y)); // Right
        neighbors.add(new Vector2(position.x - 1, position.y)); // Left
        neighbors.add(new Vector2(position.x, position.y + 1)); // Up
        neighbors.add(new Vector2(position.x, position.y - 1)); // Down
        return neighbors;
    }

    private static List<Vector2> reconstructPath(Node endNode) {
        List<Vector2> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, current.position);
            current = current.previous;
        }

        return path;
    }
}