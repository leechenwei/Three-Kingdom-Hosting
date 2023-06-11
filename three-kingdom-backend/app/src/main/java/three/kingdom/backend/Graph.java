/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package three.kingdom.backend;
import java.util.*;

public class Graph<T, U> {
    
    private Map<T, List<Edge<T, U>>> adjacencyList;
    private int size;

    public Graph() {
        adjacencyList = new HashMap<>();
        size = 0;
    }

    public void addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
            size++;
        }
    }

    public void addUndirectedEdge(T source, T destination, U weight) {
        addDirectedEdge(source, destination, weight);
        addDirectedEdge(destination, source, weight);
    }

    public void addDirectedEdge(T source, T destination, U weight) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            List<Edge<T, U>> edges = adjacencyList.get(source);
            edges.add(new Edge<>(destination, weight));
        }
    }

    public List<T> getNeighbours(T vertex) {
        List<T> neighbours = new ArrayList<>();
        if (adjacencyList.containsKey(vertex)) {
            List<Edge<T, U>> edges = adjacencyList.get(vertex);
            for (Edge<T, U> edge : edges) {
                neighbours.add(edge.getDestination());
            }
        }
        return neighbours;
    }

    public U getWeight(T source, T destination) {
        if (adjacencyList.containsKey(source)) {
            List<Edge<T, U>> edges = adjacencyList.get(source);
            for (Edge<T, U> edge : edges) {
                if (edge.getDestination().equals(destination)) {
                    return edge.getWeight();
                }
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public T getVertex(int index) {
        if (index >= 0 && index < size) {
            for (T vertex : adjacencyList.keySet()) {
                if (index == 0) {
                    return vertex;
                }
                index--;
            }
        }
        return null;
    }

    private static class Edge<T, U> {
        private T destination;
        private U weight;

        public Edge(T destination, U weight) {
            this.destination = destination;
            this.weight = weight;
        }

        public T getDestination() {
            return destination;
        }

        public U getWeight() {
            return weight;
        }
    }
}