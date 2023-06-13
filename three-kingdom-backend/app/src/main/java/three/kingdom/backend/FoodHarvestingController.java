/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package three.kingdom.backend;
import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Heng
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET,
    RequestMethod.POST }, allowedHeaders = "*")
public class FoodHarvestingController {
    private List<List<Integer>> paths;
    private ArrayList<Integer> nodeWithoutFood;
    private Graph<Integer, Integer> graph;

    public FoodHarvestingController() {
        graph = createGraph();
    }

    @GetMapping("/food-harvesting/{nodeWithoutFood}")
    public List<List<Integer>> findBestPaths(@PathVariable List<Integer> nodeWithoutFood) {
        paths = new ArrayList<>();
        this.nodeWithoutFood = new ArrayList<>(nodeWithoutFood);

        if (this.nodeWithoutFood.isEmpty()) {
            return paths;
        } else if (containAllNodesExcept1(this.nodeWithoutFood)) {
            List<Integer> path = new ArrayList<>();
            path.add(1);
            path.add(1);
            paths.add(path);
            return paths;
        } else {
            depthFirstSearch(1, new ArrayList<>());
            return findBestPathsHelper(nodeWithoutFood);
        }
    }

    private void depthFirstSearch(int start, List<Integer> currentPath) {
        if (currentPath.isEmpty()) {
            currentPath.add(start);
        }

        int lastNode = currentPath.get(currentPath.size() - 1);

        List<Integer> neighbours = graph.getNeighbours(lastNode);
        for (int neighbour : neighbours) {
            if (neighbour == start) {
                List<Integer> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbour);
                paths.add(newPath);
            }

            if (!currentPath.contains(neighbour)) {
                List<Integer> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbour);
                depthFirstSearch(start, newPath);
            }
        }
    }

    private boolean containAllNodesExcept1(List<Integer> nodeWithoutFood) {
        boolean containAllNodesExcept1 = true;
        for (int i = 2; i <= 10; i++) {
            if (!nodeWithoutFood.contains(i)) {
                containAllNodesExcept1 = false;
                break;
            }
        }
        return containAllNodesExcept1;
    }

    private List<List<Integer>> findBestPathsHelper(List<Integer> nodeWithoutFood) {
        List<List<Integer>> bestPaths = new ArrayList<>();

        for (int i = 0; i <= nodeWithoutFood.size(); i++) {
            for (List<Integer> path : paths) {
                if (containsAllFoodNodes(nodeWithoutFood, path)
                        && path.size() == graph.getSize() + 1 - (nodeWithoutFood.size() - i)) {
                    bestPaths.add(path);
                }
            }
            if (!bestPaths.isEmpty()) {
                return bestPaths;
            }
        }
        return bestPaths;
    }

    private boolean containsAllFoodNodes(List<Integer> nodeWithoutFood, List<Integer> path) {
        for (int i = 1; i <= 10; i++) {
            if (!nodeWithoutFood.contains(i) && !path.contains(i)) {
                return false;
            }
        }
        return true;
    }

    private Graph<Integer, Integer> createGraph() {
        Graph<Integer, Integer> graph = new Graph<>();
        int[] vertices = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int elem : vertices) {
            graph.addVertex(elem);
        }
        graph.addUndirectedEdge(1, 2, null);
        graph.addUndirectedEdge(1, 3, null);
        graph.addUndirectedEdge(1, 6, null);
        graph.addUndirectedEdge(1, 10, null);
        graph.addUndirectedEdge(2, 4, null);
        graph.addUndirectedEdge(3, 4, null);
        graph.addDirectedEdge(3, 7, null);
        graph.addUndirectedEdge(4, 5, null);
        graph.addUndirectedEdge(5, 6, null);
        graph.addUndirectedEdge(5, 7, null);
        graph.addUndirectedEdge(6, 7, null);
        graph.addUndirectedEdge(6, 8, null);
        graph.addUndirectedEdge(7, 9, null);
        graph.addUndirectedEdge(7, 8, null);
        graph.addUndirectedEdge(8, 9, null);
        graph.addUndirectedEdge(8, 10, null);
        graph.addUndirectedEdge(9, 10, null);

        return graph;
    }
}

