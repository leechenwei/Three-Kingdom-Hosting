package three.kingdom.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.OPTIONS, RequestMethod.POST }, allowedHeaders = "*")
public class Cliff_Fire {
    private static final int[][] DIRECTIONS = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    @PostMapping("/CliffOnFire")
    public ClusterResult findClusters(@RequestBody int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int clusterCount = 0;
        List<ClusterInfo> clusterInfoList = new ArrayList<>();

        int[][] clusterMatrix = new int[rows][cols]; // Matrix to store cluster information

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 1 && !visited[i][j]) {
                    ClusterInfo clusterInfo = new ClusterInfo();
                    dfs(matrix, visited, i, j, clusterInfo, clusterMatrix, clusterCount + 1);
                    clusterCount++;

                    clusterInfoList.add(clusterInfo);
                }
            }
        }

        return new ClusterResult(clusterCount, clusterInfoList, clusterMatrix);
    }

    private void dfs(int[][] matrix, boolean[][] visited, int row, int col, ClusterInfo clusterInfo,
                     int[][] clusterMatrix, int clusterIndex) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        visited[row][col] = true;

        clusterInfo.setSize(clusterInfo.getSize() + 1);
        clusterInfo.setMaxRow(Math.max(clusterInfo.getMaxRow(), row));
        clusterInfo.setMaxCol(Math.max(clusterInfo.getMaxCol(), col));

        clusterMatrix[row][col] = clusterIndex; // Assign cluster index to clusterMatrix

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (isValid(rows, cols, newRow, newCol) && matrix[newRow][newCol] == 1 &&
                    !visited[newRow][newCol]) {
                dfs(matrix, visited, newRow, newCol, clusterInfo, clusterMatrix, clusterIndex);
            }
        }
    }

    private boolean isValid(int rows, int cols, int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    static class ClusterInfo {
        private int size;
        private int maxRow;
        private int maxCol;

        public ClusterInfo() {
            size = 0;
            maxRow = Integer.MIN_VALUE;
            maxCol = Integer.MIN_VALUE;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getMaxRow() {
            return maxRow;
        }

        public void setMaxRow(int maxRow) {
            this.maxRow = maxRow;
        }

        public int getMaxCol() {
            return maxCol;
        }

        public void setMaxCol(int maxCol) {
            this.maxCol = maxCol;
        }
    }

    static class ClusterResult {
        private int clusterCount;
        private List<ClusterInfo> clusterInfoList;
        private int[][] clusterMatrix;

        public ClusterResult(int clusterCount, List<ClusterInfo> clusterInfoList, int[][] clusterMatrix) {
            this.clusterCount = clusterCount;
            this.clusterInfoList = clusterInfoList;
            this.clusterMatrix = clusterMatrix;
        }

        public int getClusterCount() {
            return clusterCount;
        }

        public void setClusterCount(int clusterCount) {
            this.clusterCount = clusterCount;
        }

        public List<ClusterInfo> getClusterInfoList() {
            return clusterInfoList;
        }

        public void setClusterInfoList(List<ClusterInfo> clusterInfoList) {
            this.clusterInfoList = clusterInfoList;
        }
        
        public int[][] getClusterMatrix() {
            return clusterMatrix;
        }

        public void setClusterMatrix(int[][] clusterMatrix) {
            this.clusterMatrix = clusterMatrix;
        }
    }
}

@Configuration
class CorsConfig4 implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
