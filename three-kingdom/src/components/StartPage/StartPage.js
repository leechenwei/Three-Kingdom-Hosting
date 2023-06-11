import React, { useEffect, useState } from 'react';
import './StartPage.css';
import { faHeart, faUtensils, faBolt } from '@fortawesome/free-solid-svg-icons';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import strawman from './Strawman.png';
import cipher from './Cipher.jpg';
import general from './sunquan2.png';
import simulator from './Simulation.png';
import food from './Food.png';
import road from './HuaRongRoad.jpg';
import battle from './BattleOnCliff.jpg';

const App = () => {
  const [fadeIn, setFadeIn] = useState(false);

  useEffect(() => {
    setFadeIn(true);
  }, []);

  const handleCircleClick = (url) => {
    window.location.href = url; // Redirect to the specified URL
  };

  const circlePositions = [
    { left: 1200, top: 150 },
    { left: 350, top: 50 },
    { left: 700, top: 250 },
    { left: 150, top: 400 },
    { left: 1100, top: 450 },
    { left: 500, top: 500},
    { left: 900, top: 10},
  ];

  const icons = [
    { label: 'Health', value: 100, icon: faHeart },
    { label: 'Food', value: 100, icon: faUtensils },
    { label: 'Energy', value: 100, icon: faBolt },
  ];

  const circles = circlePositions.map((position, index) => {
    // Generate random URLs for redirection
    const urls = [
      '/BoatArrow',
      '/Cipher',
      '/CharacterInfo',
      '/Simulator',
      '/FoodHarvest',
      '/HuaRongRoad',
      '/BattleOnCliff',
    ];

  // Define the background image and position for each circle
  let backgroundImage = '';
  let backgroundPosition = '';

  if (index === 0) {
    backgroundImage = `url(${strawman})`;
    backgroundPosition = 'center top';
  } else if (index === 1) {
    backgroundImage = `url(${cipher})`;
    backgroundPosition = 'center top';
  } else if (index === 2) {
    backgroundImage = `url(${general})`; // Add the background image for the third circle
    backgroundPosition = 'center top';
  } else if (index === 3){
    backgroundImage = `url(${simulator})`;
    backgroundPosition = 'center top';
  } else if (index === 4){
    backgroundImage = `url(${food})`;
    backgroundPosition = 'center top';
  } else if (index === 5){
    backgroundImage = `url(${road})`;
    backgroundPosition = 'center top';
  } else if (index === 6){
    backgroundImage = `url(${battle})`;
    backgroundPosition = 'bottom';
  }

    return (
      <div
        key={index}
        className={`circle ${fadeIn ? 'fade-in' : ''}`}
        style={{ left: position.left, top: position.top, backgroundImage: backgroundImage, backgroundPosition: backgroundPosition, backgroundSize: 'cover',}}
        onClick={() => handleCircleClick(urls[index])}
      ></div>
    );
  });

  const iconsList = icons.map((icon, index) => (
    <div key={index} className={`icon ${fadeIn ? 'fade-in' : ''}`}>
      <div className={`icon-box ${fadeIn ? 'fade-in' : ''}`}>
        {icon.label === 'Health' ? (
          <FontAwesomeIcon icon={icon.icon} className="icon-icon" style={{ color: 'red' }} />
        ) : icon.label === 'Energy' ? (
          <FontAwesomeIcon icon={icon.icon} className="icon-icon" style={{ color: 'blue' }} />
        ) : icon.label === 'Food' ? (
          <FontAwesomeIcon icon={icon.icon} className="icon-icon" style={{ color: 'orange' }} />
        ) : (
          <FontAwesomeIcon icon={icon.icon} className="icon-icon" />
        )}
        <span className="icon-label" style={{ marginLeft: '12px' }}>
          {icon.label}:
        </span>
        <span className="icon-value">{icon.value}</span>
      </div>
    </div>
  ));

  return (
    <div className={`App ${fadeIn ? 'fade-in' : ''}`}>
      <header className={`App-header ${fadeIn ? 'fade-in' : ''}`}>
        <div className={`icons-container ${fadeIn ? 'fade-in' : ''}`}>{iconsList}</div>
        {circles}
        <Link to="/BoatArrow"></Link>
        <Link to="/Cipher"></Link>
        <Link to="/CharacterInfo"></Link>
        <Link to="/Simulator"></Link>
        <Link to="/FoodHarvest"></Link>
        <Link to="/HuaRongRoad"></Link>
        <Link to="/BattleOnCliff"></Link>
        <div class="cloud cloud-1"></div>
        <div class="cloud cloud-2"></div>
        <div class="cloud cloud-3"></div>
        <div class="cloud cloud-4"></div>
        <div class="cloud cloud-5"></div>
      </header>
    </div>
  );
};

export default App;