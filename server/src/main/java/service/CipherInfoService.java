package service;


import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

public class CipherInfoService {

    private static final char[] ALPHABET = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
    private static final int KEY_LENGTH = 16;

    private int rsaE = 7;
    private int rsaN = 3;
    private String sessionKey;
    private String encryptedSessionKey = "defaultkey000000";
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

    public void createSessionKey() throws UnsupportedEncodingException {
        encryptedSessionKey = "";
        sessionKey = "";
        for (int i = 0; i < KEY_LENGTH; i++) {
            int randomNumber = new Random().nextInt(ALPHABET.length);
            sessionKey += ALPHABET[randomNumber];
            encryptedSessionKey += (char)(binPow((((int)sessionKey.charAt(i))), rsaE, rsaN) % rsaN);
        }
        encryptedSessionKeyBytes = encryptedSessionKey.getBytes("UTF-8");
        keyCreatedTime = LocalDateTime.now();
    }

    private static long binPow(long a, long b, int m) {
        a %= m;
        if (b == 0) return 1;
        else if (b % 2 == 0) {
            return binPow((a * a) % m, b / 2, m);
        }
        else return (a * binPow(a, b - 1, m)) % m;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public LocalDateTime getKeyCreatedTime() {
        return keyCreatedTime;
    }

    public static int getKeyLength() {
        return KEY_LENGTH;
    }
}
