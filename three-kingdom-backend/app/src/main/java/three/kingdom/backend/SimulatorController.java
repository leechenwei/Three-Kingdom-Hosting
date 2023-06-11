/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package three.kingdom.backend;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Heng 
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET,
    RequestMethod.POST }, allowedHeaders = "*")
public class SimulatorController {

    @GetMapping("/graph/paths/{end}")
    public ResponseEntity<List<String>> getPaths(@PathVariable int end) {
        int n = 11;
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        // Given Graph
        BreadthFirstSearch.addEdge(adj, 1, 2);
        BreadthFirstSearch.addEdge(adj, 1, 3);
        BreadthFirstSearch.addEdge(adj, 1, 6);
        BreadthFirstSearch.addEdge(adj, 1, 10);
        BreadthFirstSearch.addEdge(adj, 2, 4);
        BreadthFirstSearch.addEdge(adj, 3, 4);
        BreadthFirstSearch.addDirectedEdge(adj, 3, 7);
        BreadthFirstSearch.addEdge(adj, 4, 5);
        BreadthFirstSearch.addEdge(adj, 5, 6);
        BreadthFirstSearch.addEdge(adj, 5, 7);
        BreadthFirstSearch.addEdge(adj, 6, 7);
        BreadthFirstSearch.addEdge(adj, 6, 8);
        BreadthFirstSearch.addEdge(adj, 7, 8);
        BreadthFirstSearch.addEdge(adj, 7, 9);
        BreadthFirstSearch.addEdge(adj, 8, 9);
        BreadthFirstSearch.addEdge(adj, 8, 10);
        BreadthFirstSearch.addEdge(adj, 9, 10);

        int start = 1; // Set the start point to 1

        BreadthFirstSearch bfs = new BreadthFirstSearch();
        List<String> paths = bfs.calculatePaths(adj, n, start, end);
        return ResponseEntity.ok(paths);
    }
}

class BreadthFirstSearch {

    // two vertices src and dest
    public static void addEdge(ArrayList<ArrayList<Integer>> adj, int src, int dest) {
        adj.get(src).add(dest);
        adj.get(dest).add(src);

    }

    public static void addDirectedEdge(ArrayList<ArrayList<Integer>> adj, int src, int dest) {
        adj.get(src).add(dest);
    }

    // Find all the paths and store the paths in paths array
    private static void find_paths(ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path,
            ArrayList<ArrayList<Integer>> parent, int n, int currentVertex) {

        // Base case
        if (currentVertex == -1) {
            paths.add(new ArrayList<>(path));
            return;
        }

        for (int pa : parent.get(currentVertex)) {

            // add the current vertex
            path.add(currentVertex);

            // recursive call for its parent
            find_paths(paths, path, parent, n, pa);

            // remove the current vertex
            path.remove((path.size() - 1));
        }
    }

    private static void bfs(ArrayList<ArrayList<Integer>> adj, ArrayList<ArrayList<Integer>> parent, int n, int start) {

        // shortestDistances array contain the path with the shortest distance from
        // start to end
        int[] shortestDistances = new int[n];
        Arrays.fill(shortestDistances, Integer.MAX_VALUE);

        Queue<Integer> queue = new LinkedList<>();

        // Insert source vertex in queue and make its parent and distance 0
        queue.offer(start);

        parent.get(start).clear();
        parent.get(start).add(-1);

        shortestDistances[start] = 0;

        // until the queue is empty
        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();

            for (int v : adj.get(currentVertex)) {
                if (shortestDistances[v] > shortestDistances[currentVertex] + 1) {

                    // Remove all the previous parents when shorter distance is found
                    shortestDistances[v] = shortestDistances[currentVertex] + 1;
                    queue.offer(v);
                    parent.get(v).clear();
                    parent.get(v).add(currentVertex);
                } else if (shortestDistances[v] == shortestDistances[currentVertex] + 1) {

                    // Another candidate parent for the shortest path found
                    parent.get(v).add(currentVertex);
                }
            }
        }
    }

    // Function which prints all the paths from start to end
    public List<String> calculatePaths(ArrayList<ArrayList<Integer>> adj, int n, int start, int end) {
        // ArrayList<ArrayList<Integer>> listOfList = new ArrayList<>(); // Initialize
        // listOfList

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<ArrayList<Integer>> parent = new ArrayList<>();
        List<String> pathsList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            parent.add(new ArrayList<>());
        }

        // Function call to bfs
        bfs(adj, parent, n, start);

        // Function call to find_paths
        find_paths(paths, path, parent, n, end);

        for (ArrayList<Integer> v : paths) {
            Collections.reverse(v);

            // Build the path with arrows
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 0; i < v.size() - 1; i++) {
                pathBuilder.append(v.get(i)).append(" ---> ");
            }
            pathBuilder.append(v.get(v.size() - 1));

            System.out.println("Best Path: ");
            System.out.println(pathBuilder.toString());
            pathsList.add(pathBuilder.toString());
        }
        return pathsList;
    }
}


@Configuration
class CorsConfig2 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}