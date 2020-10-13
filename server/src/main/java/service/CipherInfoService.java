package service;

import java.time.LocalDateTime;
import java.util.Random;

public class CipherInfoService {

    private static final char[] ALPHABET = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
    private static final int KEY_LENGTH = 16;

    private int rsaE;
    private int rsaN;
    private String sessionKey;
    private static String encryptedSessionKey;
    private byte[] encryptedSessionKeyBytes;
    private LocalDateTime keyCreatedTime;

    public int getRsaE() {
        return rsaE;
    }

    public void setRsaE(int rsaE) {
        this.rsaE = rsaE;
    }

    public int getRsaN() {
        return rsaN;
    }

    public void setRsaN(int rsaEN) {
        this.rsaN = rsaEN;
    }

    public void createSessionKey() {
        // check random - random numbers must not be the same all the time
        sessionKey = "";
        for (int i = 0; i < KEY_LENGTH; i++) {
            int randomNumber = new Random().nextInt(ALPHABET.length);
            sessionKey += ALPHABET[randomNumber];
            encryptedSessionKey += (Math.pow((((int) ALPHABET[randomNumber]) % rsaN), rsaE) % rsaN);
        }
        encryptedSessionKeyBytes = encryptedSessionKey.getBytes();// ?? getBytes("UTF-8")
        keyCreatedTime = LocalDateTime.now();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public LocalDateTime getKeyCreatedTime() {
        return keyCreatedTime;
    }

    public static int getKeyLength() {
        return KEY_LENGTH;
    }
}
