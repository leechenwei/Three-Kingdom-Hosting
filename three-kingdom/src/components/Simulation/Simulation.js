import React, { useState } from 'react';
import styles from './Simulation.module.css';
import GraphMapImage from './GraphMap1.png';

const GraphMap = () => {
  const [generals, setGenerals] = useState([]);
  const [showGraphMap, setShowGraphMap] = useState(true); // State to control the visibility of the graph map
  const [selectedNodes, setSelectedNodes] = useState(
    Array.from({ length: 9 }, (_, index) => ({
      node: index + 1,
      selected: false,
    }))
  );

  const handleClick = async (end) => {
    try {
      const response = await fetch(`http://localhost:8080/api/enemy-fortress/${end}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setGenerals(data.generals);
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
                className={`${styles['graph-node']} ${selectedNodes.find((n) => n.node === node && n.selected) ? styles['graph-node-selected'] : ''
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
              <p><b>Paths:</b></p>
              {generals.map((general, index) => (
                <div key={index} className={styles['best-path-item']}>
                  <p className={styles['best-path-label']}>
                    Shortest time for <b>{general.general}</b> to reach the enemy fortress from Node 1: {general.shortestTime} hours
                  </p>
                  <p className={styles['best-path']}>
                    Shortest path: <b>{general.shortestPath.join(' -> ')}</b>
                  </p>
                </div>
              ))}
            </div>
          </div>

          <div className={styles['additional-info']}>
            <h2>Additional Information</h2>
            <p>
              Category <b>Flat Road</b>: [1 → 6], [1 → 3], [5 → 6], [7 → 8], [7 → 9], [1 → 10], [9 → 10]
            </p>
            <p>
              Category <b>Forest</b>: [1 → 2], [5 → 7], [6 → 7], [8 → 10]
            </p>
            <p>
              Category <b>Swamp</b>: [2 → 4], [3 → 4], [4 → 5], [8 → 9]
            </p>
            <p>
              Category <b>Plank Road</b>: [3 → 7], [6 → 8]
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GraphMap;
