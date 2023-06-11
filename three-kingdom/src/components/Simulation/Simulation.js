import React, { useState } from 'react';
import styles from './Simulation.module.css';
import GraphMapImage from './GraphMap.png';

const GraphMap = () => {
  const [paths, setPaths] = useState([]);
  const [showGraphMap, setShowGraphMap] = useState(true); // State to control the visibility of the graph map
  const [selectedNodes, setSelectedNodes] = useState(
    Array.from({ length: 9 }, (_, index) => ({
      node: index + 1,
      selected: false,
    }))
  );

  const handleClick = async (end) => {
    try {
      const response = await fetch(`http://localhost:8080/api/graph/paths/${end}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setPaths(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleNodeClick = (node) => {
    setSelectedNodes((prevSelectedNodes) => {
      // Reset the previously selected node's background color
      const updatedSelectedNodes = prevSelectedNodes.map((prevNode) => ({
        ...prevNode,
        selected: false,
      }));
  
      // Toggle the selected state of the current node
      const updatedNode = updatedSelectedNodes.find((n) => n.node === node);
      if (updatedNode) {
        updatedNode.selected = !updatedNode.selected;
      }
  
      return updatedSelectedNodes;
    });
  };
  

  const handleFoodHarvestingClick = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/food-harvesting', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nodes: selectedNodes }),
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      console.log(data);
      // Handle the response data as needed
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className={styles['simulation-container']}>
      <div className={styles['graph-container']}>
        <h1>Enemy Fortress Attack Simulation</h1>
        <div className={styles['graph-canvas']}>
          {showGraphMap && (
            <div className={styles['graph-map-container']}>
              <img src={GraphMapImage} alt="Graph Map" className={styles['graph-map-image']} />
              <p className={styles['buttonContainer']}>Choose the enemy base camp:</p>
            </div>
          )}
          <div className={styles.graph}>
            {Array.from({ length: 9 }, (_, index) => index + 1).map((node) => (
              <button
              key={node}
              onClick={() => handleClick(node)}
              className={`${styles['graph-node']} ${
                selectedNodes.find((n) => n.node === node && n.selected)
                  ? styles['graph-node-selected']
                  : ''
              }`}
              style={
                selectedNodes.find((n) => n.node === node && n.selected)
                  ? { backgroundColor: '#4CAF50' }
                  : {}
              }
            >
              {node}
            </button>
            ))}
          </div>
          <div className={styles.paths}>
            <div className={styles['best-path-container']}>
              <p className={styles['best-path-label']}>Best Path:</p>
              {paths.map((path, index) => (
                <p key={index} className={styles['best-path']}>
                  {path}
                </p>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GraphMap;
