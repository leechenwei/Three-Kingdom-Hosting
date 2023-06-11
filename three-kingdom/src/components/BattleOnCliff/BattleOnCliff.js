import React, { useState } from "react";
import styles from "./BattleOnCliff.module.module.css";

function BattleOnCliff() {
    const [matrix, setMatrix] = useState([]);
    const [clusterCount, setClusterCount] = useState(null);
    const [clusterInfoList, setClusterInfoList] = useState([]);
    const [clusterColors, setClusterColors] = useState({});

    const handleGenerateMatrix = (rows, cols) => {
        const newMatrix = Array.from({ length: rows }, () =>
            Array.from({ length: cols }, () => 0)
        );
        setMatrix(newMatrix);
    };

    const handleMatrixChange = (rowIndex, colIndex) => {
        const updatedMatrix = [...matrix];
        updatedMatrix[rowIndex][colIndex] = updatedMatrix[rowIndex][colIndex] === 0 ? 1 : 0;
        setMatrix(updatedMatrix);
    };

    const handleFindCluster = () => {
        // Make the API call to the backend and get the cluster information
        fetch("http://localhost:8080/api/CliffOnFire", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(matrix),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data); // Log the response data to the console
            const { clusterCount, clusterInfoList, clusterMatrix } = data; // Define the data variable using destructuring assignment
            setClusterCount(clusterCount);
            setClusterInfoList(clusterInfoList);
      
            // Generate random colors for each cluster
            const uniqueClusters = [...new Set(clusterMatrix.flat())];
            const colors = {};
            uniqueClusters.forEach((cluster, index) => {
              colors[cluster] = getRandomColor();
            });
            setClusterColors(colors);
      
            // Update the matrix with cluster numbers
            setMatrix(clusterMatrix);
          })
          .catch((error) => console.log(error));
      };

    const handleResetMatrix = () => {
        setMatrix([]);
        setClusterCount(null);
        setClusterInfoList([]);
        setClusterColors({});
    };

    const getRandomColor = () => {
        const letters = "0123456789ABCDEF";
        let color = "#";
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    };

    const getCellColor = (cluster) => {
        return cluster > 0 ? `cluster-${cluster}` : "";
      };

    return (
        <div className={styles.body}>
            <div className={styles.container}>
                <h2>Cluster Finder</h2>
                <div className={styles.inputContainer}>
                    <label htmlFor="rows">Number of Rows:</label>
                    <input type="number" id="rows" />
                    <label htmlFor="cols">Number of Columns:</label>
                    <input type="number" id="cols" />
                </div>
                <div>
                    <button
                        onClick={() => {
                            const rows = parseInt(document.getElementById("rows").value);
                            const cols = parseInt(document.getElementById("cols").value);
                            handleGenerateMatrix(rows, cols);
                        }}
                    >
                        Generate Matrix
                    </button>
                </div>
                {matrix.length > 0 && (
                    <div>
                        <h3>Matrix</h3>
                        <table>
                            <tbody>
                                {matrix.map((row, rowIndex) => (
                                    <tr key={rowIndex}>
                                        {row.map((cell, colIndex) => {
                                            const cluster = matrix[rowIndex][colIndex];
                                            const cellClass = getCellColor(cluster);

                                            return (
                                                <td
                                                    key={colIndex}
                                                    onClick={() => handleMatrixChange(rowIndex, colIndex)}
                                                    className={`${styles.cell} ${styles[cellClass]}`}
                                                >
                                                    {cell}
                                                </td>
                                            );
                                        })}
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        <button onClick={handleFindCluster}>Find Cluster</button>
                        <button onClick={handleResetMatrix}>Reset Matrix</button>
                    </div>
                )}
                {clusterCount !== null && (
                    <div>
                        <h3>Cluster Count: {clusterCount}</h3>
                        {clusterInfoList.length > 0 && (
                            <div>
                                <h3>Cluster Info:</h3>
                                <ul>
                                    {clusterInfoList.map((clusterInfo, index) => (
                                        <li key={index}>
                                            Cluster {index + 1} - Size: {clusterInfo.size}, Max Row: {clusterInfo.maxRow}, Max Col: {clusterInfo.maxCol}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}

export default BattleOnCliff;
