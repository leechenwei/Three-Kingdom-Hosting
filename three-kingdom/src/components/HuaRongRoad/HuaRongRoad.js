import React, { useState } from 'react';
import styles from './HuaRongRoad.module.css';

function HuaRongRoad() {
    const [numRows, setNumRows] = useState(0);
    const [numCols, setNumCols] = useState(0);
    const [maze, setMaze] = useState([]);
    const [path, setPath] = useState([]);
    const [error, setError] = useState('');

    const handleNumRowsChange = (event) => {
        setNumRows(parseInt(event.target.value, 10));
    };

    const handleNumColsChange = (event) => {
        setNumCols(parseInt(event.target.value, 10));
    };

    const handleGenerateClick = () => {
        if (numRows > 0 && numCols > 0) {
            const newMaze = Array(numRows)
                .fill()
                .map(() => Array(numCols).fill(0));

            setMaze(newMaze);
        }
    };

    const handleCellValueChange = (row, col, value) => {
        const updatedMaze = [...maze];
        updatedMaze[row][col] = value;
        setMaze(updatedMaze);
    };

    const prepareMazeForBackend = () => {
        const preparedMaze = maze.map((row) => [...row]);
        return preparedMaze;
    };

    const handleSolveClick = async () => {
        if (maze.length === 0) {
            setError('Please generate a maze first');
            setPath([]); // Clear the path array
            return;
        }

        const preparedMaze = prepareMazeForBackend();

        // Count the occurrences of "2" and "3" in the maze
        let startCount = 0;
        let exitCount = 0;

        for (let i = 0; i < preparedMaze.length; i++) {
            for (let j = 0; j < preparedMaze[i].length; j++) {
                if (preparedMaze[i][j] === 2) {
                    startCount++;
                } else if (preparedMaze[i][j] === 3) {
                    exitCount++;
                }
            }
        }

        if (startCount !== 1 || exitCount !== 1) {
            setError('Must have one starting point and one exit and only one of each');
            setPath([]); // Clear the path array
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/find-path', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(preparedMaze),
            });

            if (response.ok) {
                const data = await response.json();
                console.log(data)
                if (data && data.length > 0) {
                    setPath(data);
                    setError(''); // Clear the error message
                } else {
                    setPath([]);
                    setError('Please Create a Valid Maze or No Path Found');
                }
            } else {
                setPath([]);
                setError('Please Create a Valid Maze or No Path Found');
            }
        } catch (error) {
            console.error('Error:', error);
            setPath([]);
            setError('Please Create a Valid Maze or No Path Found');
        }
    };

    const renderMaze = () => {
        return (
            <table className={styles.mazeTable}>
                <tbody>
                    {maze.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((cell, colIndex) => (
                                <td
                                    key={colIndex}
                                    className={`${styles.cell} ${cell === 0 ? styles.empty : cell === 1 ? styles.wall : cell === 2 ? styles.start : styles.exit
                                        }`}
                                    onClick={() => handleCellValueChange(rowIndex, colIndex, (cell + 1) % 4)}
                                >
                                    {cell}
                                </td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };

    const renderPath = () => {
        if (path.length === 0 && error === '') {
            return null; // Hide the container if there is no path or error
        }

        const showContainer = path.length > 0 || error !== '';

        return (
            <div className={`${styles.path} ${showContainer ? styles.show : styles.hidden}`}>
                {error !== '' && <p className={styles.error}>{error}</p>}
                {path.length > 0 && (
                    <>
                        <p>Path:</p>
                        <ul>
                            {path.map((direction, index) => (
                                <li key={index}>{direction}</li>
                            ))}
                        </ul>
                    </>
                )}
            </div>
        );
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Hua Rong Road</h1>
            <h2>The path of Cao Cao's retreat from Hua Rong Road</h2>
            <div className={styles.inputSection}>
                <label htmlFor="numRows">Rows: </label>
                <input type="number" id="numRows" value={numRows} onChange={handleNumRowsChange} />

                <label htmlFor="numCols"> Columns: </label>
                <input type="number" id="numCols" value={numCols} onChange={handleNumColsChange} />
            </div>
            <button onClick={handleGenerateClick} style={{ margin: '40px' }}>Generate Maze</button>

            <div className={styles.MazeContainer}>
                <h2>(0) No Battleship (1) Battleship</h2>
                <h2>(2) Start (3) Exit</h2>
                <div className={styles.maze}>{renderMaze()}</div>
                <button onClick={handleSolveClick} className={styles.solveButton}>
                    Solve Maze
                </button>
            </div>

            <div className={styles.path}>{renderPath()}</div>
        </div>
    );
}

export default HuaRongRoad;
