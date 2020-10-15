package service;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Random;

public class CipherInfoService {

    private static final char[] ALPHABET = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
    private static final int KEY_LENGTH = 16;

    private BigInteger rsaE = new BigInteger(String.valueOf(7));
    private BigInteger rsaN = new BigInteger(String.valueOf(3));
    private String sessionKey;
    private String encryptedSessionKey = "defaultkey000000";
    private byte[] encryptedSessionKeyBytes;
    private LocalDateTime keyCreatedTime;

    public BigInteger getRsaE() {
        return rsaE;
    }

    public void setRsaE(BigInteger rsaE) {
        this.rsaE = rsaE;
    }

    public BigInteger getRsaN() {
        return rsaN;
    }

    public void setRsaN(BigInteger rsaEN) {
        this.rsaN = rsaEN;
    }

    public void createSessionKey() throws UnsupportedEncodingException {
        encryptedSessionKey = "";
        sessionKey = "";
        for (int i = 0; i < KEY_LENGTH; i++) {
            int randomNumber = new Random().nextInt(ALPHABET.length);
            sessionKey += ALPHABET[randomNumber];
            encryptedSessionKey += (binPow(((new BigInteger(String.valueOf((int)sessionKey.charAt(i))))), rsaE, rsaN).mod(rsaN)).toString();
        }
        encryptedSessionKeyBytes = encryptedSessionKey.getBytes("UTF-8");
        keyCreatedTime = LocalDateTime.now();
    }

    private static BigInteger binPow(BigInteger a, BigInteger b, BigInteger m) {
        a = a.mod(m);
        if (b.equals(BigInteger.valueOf(0))) return BigInteger.valueOf(1);
        else if (b.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
            return binPow((a.multiply(a)).mod(m), b.divide(BigInteger.valueOf(2)), m);
        }
        else return (a.multiply(binPow(a, b.subtract(BigInteger.valueOf(1)), m))).mod(m);
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
