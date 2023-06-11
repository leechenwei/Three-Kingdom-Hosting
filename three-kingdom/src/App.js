import React, { useState, useEffect,createContext, useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css'; // Import the CSS file for styling
import StartPage from './components/StartPage/StartPage';
import BoatArrow from './components/BoatArrow/BoatArrow';
import Cipher from './components/Cipher/Cipher';
import Character from './components/Character/Character';
import Simulation from './components/Simulation/Simulation';
import Food from './components/FoodHarvesting/FoodHarvesting';
import Road from './components/HuaRongRoad/HuaRongRoad';
import Battle from './components/BattleOnCliff/BattleOnCliff';
import backgroundSong from './TotalWar.mp3';

// Create a context for the audio player
const AudioContext = createContext();

// Custom hook to access the audio context
const useAudio = () => useContext(AudioContext);

function App() {
  const [activeImage, setActiveImage] = useState(0);
  const totalImages = 5; // Total number of background 
  const [songLoaded, setSongLoaded] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveImage((activeImage + 1) % totalImages);
    }, 5000);

    return () => clearInterval(interval);
  }, [activeImage, totalImages]);

  const handlePlaySong = () => {
    if (!songLoaded) {
      const audio = new Audio(backgroundSong);
      audio.loop = true;
      audio.play();
      setSongLoaded(true);
    }
  };

  return (
    <AudioContext.Provider value={handlePlaySong}>
      <div className="container">
        <div className="background-container">
          <div className={`background-image ${activeImage === 0 ? 'active' : ''}`} />
          <div className={`background-image ${activeImage === 1 ? 'active' : ''}`} />
          <div className={`background-image ${activeImage === 2 ? 'active' : ''}`} />
          <div className={`background-image ${activeImage === 3 ? 'active' : ''}`} />
          <div className={`background-image ${activeImage === 4 ? 'active' : ''}`} />
        </div>

        <h1 className="title">Three Kingdom</h1>
        <p className="startDescription">Click to Start</p>
        <p className="startDescription">V</p>
        <Link
          to="/start"
          style={{ textDecoration: 'none', fontWeight: 'bold' }}
          className="startButton"
          onClick={handlePlaySong}
        >
          Start
        </Link>
      </div>
    </AudioContext.Provider>
  );
}

function MainApp() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/start" element={<StartPage />} />
        <Route path="/BoatArrow" element={<BoatArrow />} />
        <Route path="/Cipher" element={<Cipher />} />
        <Route path="/CharacterInfo" element={<Character />} />
        <Route path="/Simulator" element={<Simulation />} />
        <Route path="/FoodHarvest" element={<Food />} />
        <Route path="/HuaRongRoad" element={<Road />} />
        <Route path="/BattleOnCliff" element={<Battle />} />
      </Routes>
    </Router>
  );
}

export default MainApp;
