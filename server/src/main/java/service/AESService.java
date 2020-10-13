package service;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class AESService {

    private final int[] SBOX = {
            0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
            0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
            0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
            0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
            0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
            0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
            0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
            0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
            0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
            0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
            0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
            0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
            0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
            0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    private final int[][] RCON = {
            {0x01, 0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1b,0x36},
            {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00},
            {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00},
            {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}
    };

    // cfb - вектор инициализации, весь текст на блоки по 128 байт (в конце добить 0?)
    // 1ый раз в пред - вект иниц, цикл по входным блокам открытого текста
    // к пред применить сам aes, потом этот результат xor входной блок откр текста,
    // полученный результат xor-а  в пред положить

    private final int KEY_LENGTH = CipherInfoService.getKeyLength();
    private final int ROWS_CNT = 4;
    private final int Nb = 4; // COLS_CNT
    private final int Nk = 4; // KEY_LENGTH (IN 8-BYTE WORDS)
    private final int Nr = 10; // ROUNDS_CNT
    private final String KEY = CipherInfoService.getEncryptedSessionKey();

    public String cipherAES(String fileName) throws IOException {
        byte[] fileContent = readFromFile(fileName);

        String initialVector = "0000000000000000"; // ??
        // по 128 бит (16 байт), последний добить (0?)
        //String s = new String(answerInBytes);
        //return s;
    }

    private int[] algorithmAES(byte[] input) {

        int[][] state = new int[ROWS_CNT][Nb];
        for (int i = 0; i < ROWS_CNT; i++) {
          for (int j = 0; j < Nb; j++) {
                state[i][j] = input[i + 4 * j];
            }
        }

        int[][] keySchedule = keyExpansion(KEY);
        state = addRoundKey(state, keySchedule, 0);

        int round;
        for (round = 1; round < Nr; round++) {
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, keySchedule, round);
        }

        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, keySchedule, round + 1);

        int[] output = new int[4 * Nb];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                output[i + 4 * j] = state[i][j];
            }
        }

        return output;
    }

    private int[][] keyExpansion(String key) {

        //key_symbols = [ord(symbol) for symbol in key]
        int[] keySymbols = new int[KEY_LENGTH];
        for (int i = 0; i < KEY_LENGTH; i++) {
            keySymbols[i] = key.charAt(i);
        }

        int[][] keySchedule = new int[4][Nk]; // ?? Nr * 4 ??
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nk; j++) {
                keySchedule[i][j] = keySymbols[i + 4 * j];
            }
        }

        for (int col = Nk; col < Nb * (Nr + 1); col++) {
            if (col % Nk == 0) {
                int[] tmp = new int[4];
                for (int i = 1; i < 4; i++) {
                    tmp[i - 1] = keySchedule[i][col - 1];
                }
                tmp[3] = keySchedule[0][col - 1];

                for (int j = 0; j < 4; j++) {
                    int s_row = tmp[j];
                    int s_col = tmp[j] % 0x10;
                    tmp[j] = SBOX[16 * s_row + s_col];
                }

                for (int i = 0; i < 4; i++) {
                    int s = keySchedule[i][col - 4] ^ tmp[i] ^ RCON[i][col / Nk - 1];
                    keySchedule[i][col] = s;
                }
            }
            else {
                for (int i = 0; i < 4; i++) {
                    int s = keySchedule[i][col - 4] ^ keySchedule[i][col - 1];
                    keySchedule[i][col] = s;
                }
            }
        }
        return keySchedule;
    }

    private int[][] addRoundKey(int[][] state, int[][] keySchedule, int round) {
        for (int j = 0; j < Nk; j++) {
            //nb * round is a shift which indicates start of a part of the KeySchedule
            int s0 = state[0][j] ^ keySchedule[0][Nb * round + j];
            int s1 = state[1][j] ^ keySchedule[1][Nb * round + j];
            int s2 = state[2][j] ^ keySchedule[2][Nb * round + j];
            int s3 = state[3][j] ^ keySchedule[3][Nb * round + j];

            state[0][j] = s0;
            state[1][j] = s1;
            state[2][j] = s2;
            state[3][j] = s3;
        }
        return state;
    }

    private int[][] subBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int row = state[i][j];
                int col = state[i][j] % 0x10;
                state[i][j] = SBOX[16 * row + col];
            }
        }

        return state;
    }

    private int[][] shiftRows(int[][] state) {
        int count = 1;
        for (int i = 1; i < Nb; i++) {
            state[i] = leftShift(state[i], count);
            count++;
        }

        return state;
    }

    private int[][] mixColumns(int[][] state) {
        for (int i = 0; i < Nb; i++) {
            int s0 = mulBy02(state[0][i]) ^ mulBy03(state[1][i]) ^ state[2][i] ^ state[3][i];
            int s1 = state[0][i] ^ mulBy02(state[1][i]) ^ mulBy03(state[2][i]) ^ state[3][i];
            int s2 = state[0][i] ^ state[1][i] ^ mulBy02(state[2][i]) ^ mulBy03(state[3][i]);
            int s3 = mulBy03(state[0][i]) ^ state[1][i] ^ state[2][i] ^ mulBy02(state[3][i]);

            state[0][i] = s0;
            state[1][i] = s1;
            state[2][i] = s2;
            state[3][i] = s3;
        }

        return state;
    }

    private byte[] readFromFile(String fileName) throws IOException {
        //File file = new File(fileName);
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        byte[] fileContent = IOUtils.toByteArray(inputStream);
        String s = new String(fileContent);
        //System.out.println("File content: " + s);

        return fileContent;
    }

    private int[] leftShift(int [] array, int count) {
        int [] ans = new int[array.length];
        for (int i = count; i < array.length; i++) {
            ans[i - count] = array[i];
        }
        for (int i = 0; i < count; i++)
            ans[array.length - count + i] = array[i];
        return ans;
    }


    private int mulBy02(int num) {
        int res;
        if (num < 0x80) {
            res = (num << 1);
        }
        else {
            res = (num << 1) ^ 0x1b;
        }

        return res % 0x100;
    }

    private int mulBy03(int num) {
        return mulBy02(num) ^ num;
    }

    private int mulBy09(int num) {
        return mulBy02(mulBy02(mulBy02(num))) ^ num;
    }

    private int mulBy0b(int num) {
        return mulBy02(mulBy02(mulBy02(num))) ^ mulBy02(num) ^ num;
    }

    private int mulBy0d(int num) {
        return mulBy02(mulBy02(mulBy02(num))) ^ mulBy02(mulBy02(num)) ^ num;
    }

    private int mulBy0e(int num) {
        return mulBy02(mulBy02(mulBy02(num))) ^ mulBy02(mulBy02(num)) ^ mulBy02(num);
    }

}
