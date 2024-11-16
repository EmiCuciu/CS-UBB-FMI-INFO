package Utils;

import java.util.*;

public class Graf {
    private final HashMap<Long, List<Long>> adiacenta;

    public Graf() {
        adiacenta = new HashMap<>();
    }

    /**
     * Adds a new node to the graph
     *
     * @param vertex - the node to be added
     */
    public void addNode(Long vertex) {
        adiacenta.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Adds an edge between two nodes
     *
     * @param vertex1 - the first node
     * @param vertex2 - the second node
     */
    public void addEdge(Long vertex1, Long vertex2) {
        if (adiacenta.containsKey(vertex1) && adiacenta.containsKey(vertex2)) {
            if (!adiacenta.get(vertex1).contains(vertex2)) {
                adiacenta.get(vertex1).add(vertex2);
            }
            if (!adiacenta.get(vertex2).contains(vertex1)) {
                adiacenta.get(vertex2).add(vertex1);
            }
        }
    }

    /**
     * Finds the farthest node from a given node
     *
     * @param vertex - the node from which to start the search
     * @return an array containing the farthest node and the distance from the starting node
     */
    private Long[] dfsFarthest(Long vertex, Set<Long> visited, Long distance) {
        visited.add(vertex);
        Long farthestNode = vertex;
        Long maxDistance = distance;

        for (Long neighbor : adiacenta.get(vertex)) {
            if (!visited.contains(neighbor)) {
                Long[] result = dfsFarthest(neighbor, visited, distance + 1);
                if (result[1] > maxDistance) {
                    farthestNode = result[0];
                    maxDistance = result[1];
                }
            }
        }
        return new Long[]{farthestNode, maxDistance};
    }

    /**
     * Finds the longest path in a connected component
     *
     * @param startNode - the node from which to start the search
     * @return the length of the longest path
     */
    private Long findLongestPathInComponent(Long startNode) {
        Set<Long> visited = new HashSet<>();
        Long[] farthestFromStart = dfsFarthest(startNode, visited, 0L);
        Long farthestNode = farthestFromStart[0];

        visited.clear();
        Long[] longestPathResult = dfsFarthest(farthestNode, visited, 0L);

        return longestPathResult[1];
    }

    /**
     * Counts the number of connected components and finds the longest road in the graph
     *
     * @return an array containing the number of connected components,
     * the length of the longest road and the node from which the longest road starts
     */
    public Long[] countComponentsAndGetLongestRoad() {
        Set<Long> visited = new HashSet<>();
        Long connectedComponents = 0L;
        Long longestRoad = 0L;
        Long componentNode = -1L;

        for (Long vertex : adiacenta.keySet()) {
            if (!visited.contains(vertex)) {
                connectedComponents++;

                Long currentLongestPath = findLongestPathInComponent(vertex);

                if (currentLongestPath > longestRoad) {
                    longestRoad = currentLongestPath;
                    componentNode = vertex;
                }

                dfsMarkVisited(vertex, visited);
            }
        }
        return new Long[]{connectedComponents, longestRoad, componentNode};
    }

    /**
     * Marks a node as visited in a depth-first search
     *
     * @param vertex  - the node to be marked as visited
     * @param visited - the set of visited nodes
     */
    private void dfsMarkVisited(Long vertex, Set<Long> visited) {
        visited.add(vertex);
        for (Long neighbor : adiacenta.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsMarkVisited(neighbor, visited);
            }
        }
    }

    /**
     * Prints the graph
     */
    public void printGraph() {
        for (Long vertex : adiacenta.keySet()) {
            System.out.println("Node " + vertex + ": " + adiacenta.get(vertex));
        }
    }

    /**
     * Removes a node from the graph
     *
     * @param vertex - the node to be removed
     */
    public void removeNode(Long vertex) {
        adiacenta.remove(vertex);
        for (Long key : adiacenta.keySet()) {
            adiacenta.get(key).remove(vertex);
        }
    }

    /**
     * Removes an edge between two nodes
     *
     * @param vertex1 - the first node
     * @param vertex2 - the second node
     */
    public void removeEdge(Long vertex1, Long vertex2) {
        adiacenta.get(vertex1).remove(vertex2);
        adiacenta.get(vertex2).remove(vertex1);
    }

    /**
     * Gets all the nodes in a connected component
     *
     * @param componentNode - the node from which to start the search
     * @return a list containing all the nodes in the connected component
     */
    public List<Long> getComponentNodes(Long componentNode) {
        Set<Long> visited = new HashSet<>();
        List<Long> componentNodes = new ArrayList<>();
        dfsGetNodes(componentNode, visited, componentNodes);
        return componentNodes;
    }

    /**
     * Gets all the nodes in a connected component
     *
     * @param vertex         - the node from which to start the search
     * @param visited        - the set of visited nodes
     * @param componentNodes - the list of nodes in the connected component
     */
    private void dfsGetNodes(Long vertex, Set<Long> visited, List<Long> componentNodes) {
        visited.add(vertex);
        componentNodes.add(vertex);
        for (Long neighbor : adiacenta.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsGetNodes(neighbor, visited, componentNodes);
            }
        }
    }
}
