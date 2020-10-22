package service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CipherInfoService {

    private static final char[] ALPHABET = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
    private static final int KEY_LENGTH = 16;

    private BigInteger rsaE = new BigInteger(String.valueOf(65537));
    private BigInteger rsaN = new BigInteger(String.valueOf(126));
    private String sessionKey;
    private String encryptedSessionKey = "defaultkey000000";
    private BigInteger encryptedSessionKeyNumber;
    private byte[] encryptedSessionKeyBytes;

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

        Random random = ThreadLocalRandom.current();
        encryptedSessionKeyBytes = new byte[16];
        random.nextBytes(encryptedSessionKeyBytes);

        sessionKey = bytesToString(encryptedSessionKeyBytes);

        encryptedSessionKeyBytes = encrypt(encryptedSessionKeyBytes);
        encryptedSessionKey = bytesToString(encryptedSessionKeyBytes);
    }

    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            //test += Byte.toString(b);
            test += (char)(int)(b);
        }
        return test;
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
