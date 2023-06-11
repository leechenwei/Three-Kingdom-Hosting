package three.kingdom.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.OPTIONS,
        RequestMethod.POST }, allowedHeaders = "*")
public class CryptorController {

    private static final String ALGORITHM = "AES";
    private static final int KEY_LENGTH = 16;

    @PostMapping("/encrypt")    
    public EncryptionResponse encrypt(@RequestBody EncryptionRequest request) {
        String inputText = request.getInputText();
        String encryptionKey = request.getEncryptionKey();
        String encryptedText = encryptText(inputText, encryptionKey);
        return new EncryptionResponse(encryptedText);
    }

    @PostMapping("/decrypt")
    public DecryptionResponse decrypt(@RequestBody DecryptionRequest request) {
        String encryptedText = request.getEncryptedText();
        String decryptionKey = request.getDecryptionKey();
        String decryptedText = decryptText(encryptedText, decryptionKey);
        return new DecryptionResponse(decryptedText);
    }

    private String encryptText(String input, String key) {
        try {
            // Process special symbols: "^", "$", "()"
            StringBuilder processedInput = new StringBuilder();
            boolean capitalizeNext = false;
            boolean invertNext = false;
            StringBuilder invertedText = new StringBuilder();
    
            for (int i = 0; i < input.length(); i++) {
                char currentChar = input.charAt(i);
    
                if (capitalizeNext) {
                    currentChar = Character.toUpperCase(currentChar);
                    capitalizeNext = false;
                }
    
                if (invertNext) {
                    invertedText.append(currentChar);
                } else {
                    if (invertedText.length() > 0) {
                        processedInput.append(invertedText.reverse());
                        invertedText.setLength(0);
                    }
                    processedInput.append(currentChar);
                }
    
                if (currentChar == '^') {
                    capitalizeNext = true;
                    continue; // Skip the current char '^'
                } else if (currentChar == '$') {
                    currentChar = ' ';
                } else if (currentChar == '(') {
                    invertNext = true;
                    continue; // Skip the current char '('
                } else if (currentChar == ')') {
                    invertNext = false;
                    processedInput.append(invertedText.reverse());
                    invertedText.setLength(0);
                    continue; // Skip the current char ')'
                }
            }
    
            if (invertedText.length() > 0) {
                processedInput.append(invertedText.reverse());
            }
    
            // Remove special symbols
            processedInput = new StringBuilder(processedInput.toString()
                    .replaceAll("\\^", "")
                    .replaceAll("\\$", " ")
                    .replaceAll("\\(.*?\\)", ""));
    
            // Adjust the key length if necessary
            if (key.length() < KEY_LENGTH) {
                key = padKey(key, KEY_LENGTH);
            } else if (key.length() > KEY_LENGTH) {
                key = trimKey(key, KEY_LENGTH);
            }
    
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    
            byte[] encryptedBytes = cipher.doFinal(processedInput.toString().getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error: " + e.getMessage());
        }
    }

    private String decryptText(String input, String key) {
        try {
            // Adjust the key length if necessary
            if (key.length() < KEY_LENGTH) {
                key = padKey(key, KEY_LENGTH);
            } else if (key.length() > KEY_LENGTH) {
                key = trimKey(key, KEY_LENGTH);
            }

            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(input);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error: " + e.getMessage());
        }
    }

    // Helper method to pad the key if it is too short
    private String padKey(String key, int length) {
        StringBuilder paddedKey = new StringBuilder(key);
        while (paddedKey.length() < length) {
            paddedKey.append(' ');
        }
        return paddedKey.toString();
    }

    // Helper method to trim the key if it is too long
    private String trimKey(String key, int length) {
        return key.substring(0, length);
    }

    public static class EncryptionRequest {
        private String inputText;
        private String encryptionKey;

        public String getInputText() {
            return inputText;
        }

        public void setInputText(String inputText) {
            this.inputText = inputText;
        }

        public String getEncryptionKey() {
            return encryptionKey;
        }

        public void setEncryptionKey(String encryptionKey) {
            this.encryptionKey = encryptionKey;
        }
    }

    public static class DecryptionRequest {
        private String encryptedText;
        private String decryptionKey;

        public String getEncryptedText() {
            return encryptedText;
        }

        public void setEncryptedText(String encryptedText) {
            this.encryptedText = encryptedText;
        }

        public String getDecryptionKey() {
            return decryptionKey;
        }

        public void setDecryptionKey(String decryptionKey) {
            this.decryptionKey = decryptionKey;
        }
    }

    public static class EncryptionResponse {
        private String encryptedText;

        public EncryptionResponse(String encryptedText) {
            this.encryptedText = encryptedText;
        }

        public String getEncryptedText() {
            return encryptedText;
        }

        public void setEncryptedText(String encryptedText) {
            this.encryptedText = encryptedText;
        }
    }

    public static class DecryptionResponse {
        private String decryptedText;

        public DecryptionResponse(String decryptedText) {
            this.decryptedText = decryptedText;
        }

        public String getDecryptedText() {
            return decryptedText;
        }

        public void setDecryptedText(String decryptedText) {
            this.decryptedText = decryptedText;
        }
    }
}

@Configuration
class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}