import React, { useState } from 'react';
import styles from './FoodHarvesting.module.css';
import GraphMapImage from './GraphMap.png';

const FoodHarvesting = () => {
    const [paths, setPaths] = useState([]);
    const [selectedNodes, setSelectedNodes] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    const handleClick = async (node) => {
        if (selectedNodes.includes(node)) {
            // Node already selected, remove it from the list
            setSelectedNodes((prevSelectedNodes) =>
                prevSelectedNodes.filter((selectedNode) => selectedNode !== node)
            );
        } else {
            // Node not selected, add it to the list
            setSelectedNodes((prevSelectedNodes) => [...prevSelectedNodes, node]);
        }
    };

    const handleFoodHarvestingClick = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/api/food-harvesting/${selectedNodes.join(',')}`
            );
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            setPaths(data);
            setErrorMessage('');
        } catch (error) {
            console.error(error);
            setErrorMessage('Please choose at least one node without food!');
        }
    };

    return (
        <div className={styles.container}>
            <h1>Food Harvesting</h1>
            <div className={styles.containerContext}>
                <div className={styles['graph-map-container']}>
                    <img src={GraphMapImage} alt="Graph Map" className={styles['graph-map-image']} />
                </div>
                <h2>Please Select the Node Without Food (Can be more than one)</h2>
                <div className={styles['graph-map-label']}>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(2) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(2) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(2)}
                    >
                        2
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(3) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(3) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(3)}
                    >
                        3
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(4) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(4) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(4)}
                    >
                        4
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(5) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(5) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(5)}
                    >
                        5
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(6) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(6) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(6)}
                    >
                        6
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(7) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(7) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(7)}
                    >
                        7
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(8) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(8) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(8)}
                    >
                        8
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(9) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(9) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(9)}
                    >
                        9
                    </button>
                    <button
                        style={{
                            margin: '5px',
                            padding: '10px',
                            backgroundColor: selectedNodes.includes(10) ? '#ffa500' : '#eaeaea',
                            color: selectedNodes.includes(10) ? '#fff' : '#000',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                        onClick={() => handleClick(10)}
                    >
                        10
                    </button>
                </div>
                <div style={{margin:'15px'}}>
                    <button onClick={handleFoodHarvestingClick}>Find Best Paths</button>
                </div>
                <div>
                    {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                    {!errorMessage && (
                        <>
                            <div className={styles.pathscontainer}>
                                <p>Best Paths:</p>
                                {paths.map((path, index) => (
                                    <p key={index}>{path.join('-->')}</p>
                                ))}
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default FoodHarvesting;