import React, { useState, useEffect } from 'react';
import styles from './Character.module.module.css';

const Character = () => {
  const [generals, setGenerals] = useState([]);
  const [hierarchicalDiagram, setHierarchicalDiagram] = useState('');
  const [sortedAttribute, setSortedAttribute] = useState('');
  const [searchAttribute, setSearchAttribute] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [error, setError] = useState(null);
  const [selectedAttribute, setSelectedAttribute] = useState('');
  const [selectedLevel, setSelectedLevel] = useState('');
  const [suggestedGenerals, setSuggestedGenerals] = useState([]);
  const [suggestionError, setSuggestionError] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/generals')
      .then(response => response.json())
      .then(data => {
        setGenerals(data);
        console.log(data);
      })
      .catch(error => console.log(error));

    fetch('http://localhost:8080/api/hierarchical-diagram')
      .then(response => response.text())
      .then(data => setHierarchicalDiagram(data))
      .catch(error => console.log(error));
  }, []);

  const handleSort = () => {
    if (sortedAttribute) {
      fetch(`http://localhost:8080/api/generals/sortedBy/${sortedAttribute}`)
        .then(response => response.json())
        .then(data => {
          setGenerals(data);
          console.log(data);
        })
        .catch(error => console.log(error));
    }
  };

  const handleSearch = () => {
    if (searchAttribute && searchValue) {
      fetch(`http://localhost:8080/api/generals/binarySearch/${searchAttribute}/${searchValue}`)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('General not found');
          }
        })
        .then(data => {
          setSearchResult(data);
          setError(null); // Clear error state on successful search
          console.log(data);
        })
        .catch(error => {
          setSearchResult(null);
          setError(error.message); // Set the error message in state
          console.log(error);
        });
    } else {
      setSearchResult(null);
    }
  };

  const handleSuggestGenerals = () => {
    if (selectedAttribute && selectedLevel) {
      fetch(`http://localhost:8080/api/generals/suggestions/${selectedAttribute}/${selectedLevel}`)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error('No suggestions found');
          }
        })
        .then(data => {
          setSuggestedGenerals(data);
          setSuggestionError(null);
          console.log(data);
        })
        .catch(error => {
          setSuggestedGenerals([]);
          setSuggestionError(error.message);
          console.log(error);
        });
    } else {
      setSuggestedGenerals([]);
      setSuggestionError(null);
    }
  };

  const TreeDiagram = ({ diagram }) => {
    return (
      <div className={styles.treeDiagram}>
        {diagram.split('\n').map((line, index) => (
          <div key={index} className={styles.treeItem}>
            {line}
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>General Information of Wu Kingdom's Hierarchy & Solder's Arrangment</h1>

      <div className={styles.sortContainer}>
        <select
          value={sortedAttribute}
          onChange={event => setSortedAttribute(event.target.value)}
          className={styles.sortSelect}
        >
          <option value="">Sort by Attribute in Table Below (Ascending)</option>
          <option value="strength">Strength</option>
          <option value="leadership">Leadership</option>
          <option value="intelligence">Intelligence</option>
          <option value="politic">Politic</option>
          <option value="hitPoint">Hit Point</option>
        </select>
        <button onClick={handleSort} className={styles.sortButton}>
          Sort
        </button>
      </div>

      <div className={styles.searchContainer}>
        <select
          value={searchAttribute}
          onChange={event => setSearchAttribute(event.target.value)}
          className={styles.searchSelect}
        >
          <option value="">Search Attribute by Value</option>
          <option value="strength">Strength</option>
          <option value="leadership">Leadership</option>
          <option value="intelligence">Intelligence</option>
          <option value="politic">Politic</option>
          <option value="hitPoint">Hit Point</option>
        </select>
        <input
          type="text"
          value={searchValue}
          onChange={event => setSearchValue(event.target.value)}
          className={styles.searchInput}
          placeholder="Search Value"
        />
        <button onClick={handleSearch} className={styles.searchButton}>
          Search
        </button>
      </div>
      <div className={styles.suggestionContainer}>
        <select
          value={selectedAttribute}
          onChange={event => setSelectedAttribute(event.target.value)}
          className={styles.suggestionSelect}
        > 
          <option value="">Suggest by Attributes & Level</option>
          <option value="strength">Strength</option>
          <option value="leadership">Leadership</option>
          <option value="intelligence">Intelligence</option>
          <option value="politic">Politic</option>
        </select>
        <select
          value={selectedLevel}
          onChange={event => setSelectedLevel(event.target.value)}
          className={styles.suggestionSelect}
        >
          <option value="">Select Level</option>
          <option value="S">S</option>
          <option value="A">A</option>
          <option value="B">B</option>
          <option value="C">C</option>
        </select>
        <button onClick={handleSuggestGenerals} className={styles.suggestionButton}>
          Suggest
        </button>
      </div>
      {error && (
        <div className={styles.errorContainer}>
          <h3 className={styles.errorTitle}>Error:</h3>
          <div className={styles.error}>{error}</div>
        </div>
      )}
      {searchResult && (
        <div className={styles.searchResult}>
          <h3>Search Result:</h3>
          <div className={styles.resultItem}>
            <span>Name: </span>
            {searchResult.name}
          </div>
          <div className={styles.resultItem}>
            <span>Army Type: </span>
            {searchResult.armyType}
          </div>
          <div className={styles.resultItem}>
            <span>Strength: </span>
            {searchResult.strength}
          </div>
          <div className={styles.resultItem}>
            <span>Leadership: </span>
            {searchResult.leadership}
          </div>
          <div className={styles.resultItem}>
            <span>Intelligence: </span>
            {searchResult.intelligence}
          </div>
          <div className={styles.resultItem}>
            <span>Politic: </span>
            {searchResult.politic}
          </div>
          <div className={styles.resultItem}>
            <span>Hit Point: </span>
            {searchResult.hitPoint}
          </div>
        </div>
      )}
      {suggestionError && (
        <div className={styles.errorContainer}>
          <h3 className={styles.errorTitle}>Suggestion Error:</h3>
          <div className={styles.error}>{suggestionError}</div>
        </div>
      )}

      {suggestedGenerals.length > 0 && (
        <div className={styles.suggestionResult}>
          <h3>Suggested Generals:</h3>
          {suggestedGenerals.map((general, index) => (
            <div key={index} className={styles.resultItem}>
              <span>Name: </span>
              {general.name}
            </div>
          ))}
        </div>
      )}


      <table className={styles.generalsTable}>
        <thead>
          <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Army Type</th>
            <th>Strength</th>
            <th>Leadership</th>
            <th>Intelligence</th>
            <th>Politic</th>
            <th>Hit Point</th>
          </tr>
        </thead>
        <tbody>
          {generals.map((general, index) => (
            <tr key={index}>
              <td className={styles.centerImage}>
                <img src={`http://localhost:8080/images/${general.image}`} alt={general.name} className={styles.characterImage} />
              </td>
              <td>{general.name}</td>
              <td>{general.armyType}</td>
              <td>{general.strength}</td>
              <td>{general.leadership}</td>
              <td>{general.intelligence}</td>
              <td>{general.politic}</td>
              <td>{general.hitPoint}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className={styles.diagramContainer}>
        <h2 className={styles.diagramTitle}>Hierarchical Diagram (Three Levels)</h2>
        <pre className={styles.diagramImage}>
          <TreeDiagram diagram={hierarchicalDiagram} />
        </pre>
      </div>
    </div>
  );
};

export default Character;