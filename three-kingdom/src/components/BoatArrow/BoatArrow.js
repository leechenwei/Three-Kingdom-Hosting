import React, { useEffect, useState } from 'react';
import './BoatArrow.css';
import gif from './BoatArrowBorrow.gif';
import sound from './AudioArrow.mp3'

const BoatArrow = () => {
  const [fadeIn, setFadeIn] = useState(false);
  const [strawMen, setStrawMen] = useState(['', '', '', '']);
  const [arrows, setArrows] = useState('');
  const [loadingMessage, setLoadingMessage] = useState('');
  const [showResponse, setShowResponse] = useState(false);
  const [responseJson, setResponseJson] = useState('');
  const [showGif, setShowGif] = useState(false); // Set initial state to false
  const [playSound, setPlaySound] = useState(false);

  useEffect(() => {
    setFadeIn(true);
  }, []);

  const handleInputChange = (index, event) => {
    const value = event.target.value;
    if (index < 4) {
      const newStrawMen = [...strawMen];
      newStrawMen[index] = value;
      setStrawMen(newStrawMen);
    }
  };

  const handleArrowsChange = (event) => {
    setArrows(event.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Display "Collecting Arrows..." initially
    setLoadingMessage('Collecting Arrows...');
    setShowResponse(false);
    setShowGif(true);
    setPlaySound(true);

    // Convert the input values to arrays
    const strawMenArray = strawMen
      .map((value) => parseInt(value))
      .filter((value) => !isNaN(value));
    const arrowsArray = arrows
      .split(',')
      .map((value) => parseInt(value))
      .filter((value) => !isNaN(value));

    // Create the request body
    const requestBody = {
      strawMenNumber: strawMenArray,
      arrowNumber: arrowsArray,
    };

    // Make a POST request to the backend endpoint with the request body
    fetch('http://localhost:8080/api/calculate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          throw new Error(response.statusText);
        }
      })
      .then((data) => {
        // Hide the loading message and show the JSON response after 3 seconds
        setTimeout(() => {
          setLoadingMessage('');
          setShowResponse(true);
          setResponseJson(JSON.stringify(data, null, 2));
        }, 1500);
      })
      .catch((error) => {
        // Handle the error
        console.error(error);
        setLoadingMessage('');
        setShowResponse(true);
        setResponseJson('Please Input Right Value for Collecting Arrows');
      })
      .finally(() => {
        // Hide the GIF animation after 4 seconds
        setTimeout(() => {
          setShowGif(false);
          setPlaySound(false);
        }, 2000);
      });
  };

  const handleBackButtonClick = () => {
    setShowResponse(false);
  };

  // Function to format the JSON response for display
  const formatResponse = (response) => {
    const formattedResponse = [];
    formattedResponse.push(`Boat Direction: ${response.boatDirection.join(', ')}`);
    formattedResponse.push(`Arrow Received: ${response.arrowReceived.join(', ')}`);
    formattedResponse.push(`Total: ${response.total}`);
    return formattedResponse.join('\n');
  };


  return (
    <div className={`boatarrow-container ${fadeIn ? 'fade-in' : ''}`}>
      {/* Form */}
      <form onSubmit={handleSubmit} className={'arrowForm'}>
        {/* Number of Straw Men (Top) */}
        <div className="input-group">
          <div className="top-input">
            <label>Number of Straw Men (Top):</label>
            <input
              type="text"
              value={strawMen[0]}
              onChange={(e) => handleInputChange(0, e)}
            />
          </div>
        </div>

        {/* Number of Straw Men (Right) */}
        <div className="input-group">
          <div className="right-input">
            <label>Number of Straw Men (Right):</label>
            <input
              type="text"
              value={strawMen[1]}
              onChange={(e) => handleInputChange(1, e)}
            />
          </div>
        </div>

        {/* Number of Straw Men (Bottom) */}
        <div className="input-group">
          <div className="btm-input">
            <label>Number of Straw Men (Bottom):</label>
            <input
              type="text"
              value={strawMen[2]}
              onChange={(e) => handleInputChange(2, e)}
            />
          </div>
        </div>

        {/* Number of Straw Men (Left) */}
        <div className="input-group">
          <div className="left-input">
            <label>Number of Straw Men (Left):</label>
            <input
              type="text"
              value={strawMen[3]}
              onChange={(e) => handleInputChange(3, e)}
            />
          </div>
        </div>

        {/* Number of Arrows */}
        <div className="input-group">
          <label>Number of Arrows:</label>
          <input
            type="text"
            value={arrows}
            onChange={handleArrowsChange}
            placeholder="Eg. [1000, 500, 200, 300]"
          />
        </div>

        {/* Submit button */}
        <button type="submit">Collect</button>
      </form>

      {/* Show loading message or JSON response */}
      {showGif && (
        <div className="gif-animation">
          {playSound && <audio autoPlay src={sound} />}
          <img src={gif} alt="Boat Arrow Borrow Animation" />
        </div>
      )}
      <div className="response-container">
        {loadingMessage && <div className="loading-message">{loadingMessage}</div>}
        {showResponse && (
          <div className="json-response">
            <h1>Arrows Collected</h1>
            {responseJson !== "Please Input Right Value for Collecting Arrows" ? (
              <pre>{formatResponse(JSON.parse(responseJson))}</pre>
            ) : (
              <div>{responseJson}</div>
            )}
            <br />
            <button type="button" onClick={handleBackButtonClick} style={{ marginTop: '20px' }}>Back</button>
          </div>
        )}
      </div>

      {/* Rectangles */}
      <div
        className={`rectangle ${fadeIn ? 'slide-in' : ''}`}
        style={{ top: '190px', left: '35%' }}
      ></div>
      <div
        className={`rectangle ${fadeIn ? 'slide-in' : ''}`}
        style={{ top: '190px', left: '58.5%' }}
      ></div>
      <div
        className={`rectangle-horizontal ${fadeIn ? 'slide-in' : ''}`}
        style={{ top: '90px', left: '42%' }}
      ></div>
      <div
        className={`rectangle-horizontal ${fadeIn ? 'slide-in' : ''}`}
        style={{ top: '575px', left: '42%' }}
      ></div>

      {/* Cloud Banners */}
      <div className={`cloud-top-left ${fadeIn ? 'fade-in' : ''}`}></div>
      <div className={`cloud-top-right ${fadeIn ? 'fade-in' : ''}`}></div>
      <div className={`cloud-bottom-left ${fadeIn ? 'fade-in' : ''}`}></div>
      <div className={`cloud-bottom-right ${fadeIn ? 'fade-in' : ''}`}></div>
    </div>
  );
};

export default BoatArrow;
