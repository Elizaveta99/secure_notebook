package service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CipherInfoService {

    private static final int KEY_LENGTH = 16;

    private BigInteger rsaE = new BigInteger(String.valueOf(65537));
    private BigInteger rsaN = new BigInteger(String.valueOf(126));
    private String sessionKey;
    private String encryptedSessionKey = "defaultkey000000";
    private BigInteger encryptedSessionKeyNumber;
    private byte[] encryptedSessionKeyBytes;
    private static byte[] sessionKeyBytes;

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

    public static byte[] getSessionKeyBytes() {
        return sessionKeyBytes;
    }

    public void createSessionKey() throws UnsupportedEncodingException {
        encryptedSessionKey = "";
        sessionKey = "";

        Random random = ThreadLocalRandom.current();
        sessionKeyBytes = new byte[16];
        random.nextBytes(sessionKeyBytes);

        encryptedSessionKeyBytes = encrypt(sessionKeyBytes);
        encryptedSessionKey = new String(encryptedSessionKeyBytes, "UTF-16");
    }

    public byte[] encrypt(byte[] message)
    {
        byte[] ans = new byte[16];
        for (int i = 0; i < message.length; i++) {
            ans[i] = Byte.parseByte((new BigInteger(String.valueOf(message[i]))).modPow(rsaE, rsaN).toString());
        }
        return ans;
        //return (new BigInteger(message)).modPow(rsaE, rsaN).toByteArray();

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

    public static int getKeyLength() {
        return KEY_LENGTH;
    }

    public BigInteger getEncryptedSessionKeyNumber() {
        return encryptedSessionKeyNumber;
    }
}
