package three.kingdom.backend;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.OPTIONS,
    RequestMethod.POST }, allowedHeaders = "*")
public class HuaRongRoad {

    @PostMapping("/find-path")
    public List<String> findPath(@RequestBody int[][] maze) {

        int startX = -1, startY = -1; // Starting position of Cao Cao
        int exitX = -1, exitY = -1; // Exit position of the maze

        int rows = maze.length;
        int cols = maze[0].length;

        // Find the starting and exit positions in the maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 2) {
                    startX = i;
                    startY = j;
                } else if (maze[i][j] == 3) {
                    exitX = i;
                    exitY = j;
                }
            }
        }

        // Perform breadth-first search to find the path
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows][cols];

        // Add the starting position to the queue
        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;

        // Define the possible movements (up, down, left, right)
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};
        String[] direction = {"RIGHT", "LEFT", "UP", "DOWN"};

        // Perform breadth-first search
        while (!queue.isEmpty()) {
            int[] currentPosition = queue.poll();
            int x = currentPosition[0];
            int y = currentPosition[1];

            // Check if the exit is reached
            if (x == exitX && y == exitY) {
                break;
            }

            // Explore neighboring cells
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // Check if the new position is within bounds and not visited
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && maze[newX][newY] != 1
                        && !visited[newX][newY]) {
                    queue.offer(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    parent[newX][newY] = i; // Store the movement direction to backtrack later
                }
            }
        }

        // Backtrack from the exit position to the starting position
        List<String> path = new ArrayList<>();
        int x = exitX;
        int y = exitY;

        while (x != startX || y != startY) {
            int directionIndex = parent[x][y];
            path.add(direction[directionIndex]);
            int prevX = x - dx[directionIndex];
            int prevY = y - dy[directionIndex];
            x = prevX;
            y = prevY;
        }

        // Reverse the path to get the correct order
        Collections.reverse(path);

        return path;
    }
}

@Configuration
class CorsConfig7 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}