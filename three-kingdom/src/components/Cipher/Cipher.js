import React, { useState } from 'react';
import styles from './Cipher.module.css';

function App() {
  const [inputTextEncrypt, setInputTextEncrypt] = useState('');
  const [inputTextDecrypt, setInputTextDecrypt] = useState('');
  const [encryptionKey, setEncryptionKey] = useState('');
  const [decryptionKey, setDecryptionKey] = useState('');
  const [encryptedText, setEncryptedText] = useState('');
  const [decryptedText, setDecryptedText] = useState('');
  const [isLoadingEncrypt, setIsLoadingEncrypt] = useState(false);
  const [isLoadingDecrypt, setIsLoadingDecrypt] = useState(false);
  const [errorEncrypt, setErrorEncrypt] = useState('');
  const [errorDecrypt, setErrorDecrypt] = useState('');

  const handleInputChangeEncrypt = (e) => {
    setInputTextEncrypt(e.target.value);
  };

  const handleInputChangeDecrypt = (e) => {
    setInputTextDecrypt(e.target.value);
  };

  const handleEncryptionKeyChange = (e) => {
    setEncryptionKey(e.target.value);
  };

  const handleDecryptionKeyChange = (e) => {
    setDecryptionKey(e.target.value);
  };

  const handleEncryptClick = () => {
    if (inputTextEncrypt.trim() === '' || encryptionKey.trim() === '') {
      setErrorEncrypt('Please Input Valid Value for Text and Key');
      return;
    }

    const requestBody = {
      inputText: inputTextEncrypt,
      encryptionKey: encryptionKey
    };

    setIsLoadingEncrypt(true);
    setErrorEncrypt('');

    fetch('http://localhost:8080/api/encrypt', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })
      .then(response => {
        if (response.ok) {
          return response.json(); // Return the promise itself
        } else {
          throw new Error('Error occurred while encrypting the text.');
        }
      })
      .then(data => {
        console.log(data);
        const { encryptedText } = data;
        setEncryptedText(encryptedText);
        setIsLoadingEncrypt(false);
      })
      .catch(error => {
        setErrorEncrypt('Error occurred while encrypting the text.');
        setIsLoadingEncrypt(false);
      });
  };

  const handleDecryptClick = () => {
    if (inputTextDecrypt.trim() === '' || decryptionKey.trim() === '') {
      setErrorDecrypt('Please Input Valid Value for Text and Key');
      return;
    }

    const requestBody = {
      encryptedText: inputTextDecrypt,
      decryptionKey: decryptionKey
    };

    setIsLoadingDecrypt(true);
    setErrorDecrypt('');

    fetch('http://localhost:8080/api/decrypt', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })
      .then(response => {
        if (response.ok) {
          return response.json(); // Return the promise itself
        } else {
          throw new Error('Error occurred while decrypting the text.');
        }
      })
      .then(data => {
        const { decryptedText } = data;
        setDecryptedText(decryptedText);
      })
      .catch(error => {
        setErrorDecrypt('Error occurred while decrypting the text.');
      })
      .finally(() => {
        setIsLoadingDecrypt(false);
      });
  };

  return (
    <div className={styles.app}>
      <h1 className={styles.title}>AES Encryption and Decryption</h1>
      <div className={styles.contentWrapper}>
        <div className={styles.content}>
          <h1>Text Encryption</h1>
          <div className={styles['input-container']}>
            <label>Enter Text:</label>
            <input
              type="text"
              value={inputTextEncrypt}
              onChange={handleInputChangeEncrypt}
              placeholder="Enter your text..."
            />
          </div>
          <div className={styles['input-container']}>
            <label>Encryption Key:</label>
            <input
              type="text"
              value={encryptionKey}
              onChange={handleEncryptionKeyChange}
              placeholder="Enter encryption key..."
            />
          </div>
          <div className={styles['button-container']}>
            <button onClick={handleEncryptClick} disabled={isLoadingEncrypt}>
              {isLoadingEncrypt ? 'Encrypting...' : 'Encrypt'}
            </button>
          </div>
          {errorEncrypt ? (
            <div className={styles['error-message']}>
              <span>{errorEncrypt}</span>
            </div>
          ) : (
            encryptedText && (
              <div className={styles['encrypted-text']}>
                <label>Encrypted Text:</label>
                <div>{encryptedText}</div>
              </div>
            )
          )}
        </div>

        <div className={styles.content}>
          <h1>Text Decryption</h1>
          <div className={styles['input-container']}>
            <label>Enter Encrypted Text:</label>
            <input
              type="text"
              value={inputTextDecrypt}
              onChange={handleInputChangeDecrypt}
              placeholder="Enter encrypted text..."
            />
          </div>
          <div className={styles['input-container']}>
            <label>Decryption Key:</label>
            <input
              type="text"
              value={decryptionKey}
              onChange={handleDecryptionKeyChange}
              placeholder="Enter decryption key..."
            />
          </div>
          <div className={styles['button-container']}>
            <button onClick={handleDecryptClick} disabled={isLoadingDecrypt}>
              {isLoadingDecrypt ? 'Decrypting...' : 'Decrypt'}
            </button>
          </div>
          {errorDecrypt ? (
            <div className={styles['error-message']}>
              <span>{errorDecrypt}</span>
            </div>
          ) : (
            decryptedText && (
              <div className={styles['encrypted-text']}>
                <label>Decrypted Text:</label>
                <div>{decryptedText}</div>
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
