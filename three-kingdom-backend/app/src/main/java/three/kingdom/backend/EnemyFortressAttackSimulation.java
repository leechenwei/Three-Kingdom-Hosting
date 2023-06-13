package three.kingdom.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.*;

@RestController
@RequestMapping("/api/enemy-fortress")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.OPTIONS, RequestMethod.POST }, allowedHeaders = "*")
public class EnemyFortressAttackSimulation {

    @GetMapping("/{fortress}")
    public Map<String, Object> calculateShortestTime(@PathVariable int fortress) {
        weightedGraph graph = new weightedGraph(11);

        graph.addWeightedEdge(1, 2, 10, "Forest");
        graph.addWeightedEdge(1, 3, 18, "Flat road");
        graph.addWeightedEdge(1, 6, 20, "Flat road");
        graph.addWeightedEdge(1, 10, 16, "Flat road");
        graph.addWeightedEdge(2, 4, 10, "Swamp");
        graph.addWeightedEdge(3, 4, 12, "Swamp");
        graph.addWeightedEdge(3, 7, 28, "Plank road");
        graph.addWeightedEdge(4, 5, 12, "Swamp");
        graph.addWeightedEdge(5, 6, 17, "Flat road");
        graph.addWeightedEdge(5, 7, 10, "Forest");
        graph.addWeightedEdge(6, 7, 23, "Forest");
        graph.addWeightedEdge(6, 8, 35, "Plank road");
        graph.addWeightedEdge(7, 9, 17, "Flat road");
        graph.addWeightedEdge(7, 8, 19, "Flat road");
        graph.addWeightedEdge(8, 9, 7, "Swamp");
        graph.addWeightedEdge(8, 10, 12, "Plank road");
        graph.addWeightedEdge(9, 10, 18, "Forest");

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> generals = new ArrayList<>();

        for (int general = 0; general < 3; general++) {
            Map<String, Object> generalResult = new HashMap<>();
            String generalName = graph.getGeneralName(general);
            generalResult.put("general", generalName);
            generalResult.put("shortestTime", graph.getShortestTime(fortress, general));
            generalResult.put("shortestPath", graph.getShortestPath(1, fortress, general));
            generals.add(generalResult);
        }

        result.put("generals", generals);
        return result;
    }
}

class weightedGraph {
    private int V;
    private List<List<Edge>> adj;

    public weightedGraph(int vertices) {
        V = vertices;
        adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; ++i)
            adj.add(new ArrayList<>());
    }

    public void addWeightedEdge(int source, int destination, int distance, String terrain) {
        adj.get(source).add(new Edge(destination, distance, terrain));
        adj.get(destination).add(new Edge(source, distance, terrain));
    }

    public double getShortestTime(int fortress, int general) {
        double[][] time = new double[V][3];
        int[][] prev = new int[V][3];
        for (int i = 0; i < V; i++) {
            Arrays.fill(time[i], Double.MAX_VALUE);
            Arrays.fill(prev[i], -1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(V, Comparator.comparingDouble(node -> node.time));
        time[1][general] = 0;
        pq.offer(new Node(1, 0, general));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int u = node.vertex;
            int currentGeneral = node.general;

            for (Edge edge : adj.get(u)) {
                int v = edge.destination;
                int distance = edge.distance;
                String terrain = edge.terrain;
                double speed = getGeneralSpeed(terrain, currentGeneral);

                double travelTime = distance / speed;
                if (time[u][currentGeneral] + travelTime < time[v][currentGeneral]) {
                    time[v][currentGeneral] = time[u][currentGeneral] + travelTime;
                    prev[v][currentGeneral] = u;
                    pq.offer(new Node(v, time[v][currentGeneral], currentGeneral));
                }
            }
        }

        return time[fortress][general];
    }

     public String getGeneralName(int general) {
        switch (general) {
            case 0:
                return "Cavalry";
            case 1:
                return "Archer";
            case 2:
                return "Infantry";
            default:
                return "";
        }
    }

    private double getGeneralSpeed(String terrain, int general) {
        switch (terrain) {
            case "Flat road":
                if (general == 0) { // Cavalry
                    return 2.0 * 3;
                } else if (general == 1) { // Archer
                    return 1.0 * 2;
                } else { // Infantry
                    return 1.0 * 2;
                }
            case "Forest":
                if (general == 0) { // Cavalry
                    return 2.0 * 0.8;
                } else if (general == 1) { // Archer
                    return 1.0 * 1;
                } else { // Infantry
                    return 1.0 * 2.5;
                }
            case "Swamp":
                if (general == 0) { // Cavalry
                    return 2.0 * 0.3;
                } else if (general == 1) { // Archer
                    return 1.0 * 2.5;
                } else { // Infantry
                    return 1.0 * 1;
                }
            case "Plank road":
                if (general == 0) { // Cavalry
                    return 2.0 * 0.5;
                } else if (general == 1) { // Archer
                    return 1.0 * 0.5;
                } else { // Infantry
                    return 1.0 * 0.5;
                }
            default:
                return 1.0; // Default speed if terrain is not recognized
        }
    }

    public List<Integer> getShortestPath(int source, int fortress, int general) {
        List<Integer> path = new ArrayList<>();
        int[][] prev = new int[V][3];
        double[][] time = new double[V][3];

        for (int i = 0; i < V; i++) {
            Arrays.fill(prev[i], -1);
            Arrays.fill(time[i], Double.MAX_VALUE);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(V, Comparator.comparingDouble(node -> node.time));
        time[source][general] = 0;
        pq.offer(new Node(source, 0, general));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int u = node.vertex;
            int currentGeneral = node.general;

            if (u == fortress) {
                break;  // Reached the destination fortress, exit the loop
            }

            for (Edge edge : adj.get(u)) {
                int v = edge.destination;
                int distance = edge.distance;
                String terrain = edge.terrain;
                double speed = getGeneralSpeed(terrain, currentGeneral);

                double travelTime = distance / speed;
                if (time[u][currentGeneral] + travelTime < time[v][currentGeneral]) {
                    time[v][currentGeneral] = time[u][currentGeneral] + travelTime;
                    prev[v][currentGeneral] = u;
                    pq.offer(new Node(v, time[v][currentGeneral], currentGeneral));
                }
            }
        }

        int current = fortress;
        while (current != -1) {
            path.add(current);
            current = prev[current][general];
        }
        Collections.reverse(path);
        return path;
    }

    private static class Edge {
        int destination;
        int distance;
        String terrain;

        public Edge(int destination, int distance, String terrain) {
            this.destination = destination;
            this.distance = distance;
            this.terrain = terrain;
        }
    }

    private static class Node {
        int vertex;
        double time;
        int general;

        public Node(int vertex, double time, int general) {
            this.vertex = vertex;
            this.time = time;
            this.general = general;
        }
    }
}

@Configuration
class CorsConfig10 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}