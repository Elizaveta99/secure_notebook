package controller;

import model.BaseResponse;
import org.springframework.web.bind.annotation.*;
import service.AESService;
import service.CipherInfoService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RestController
@CrossOrigin
@RequestMapping("/server")
public class ServerController {

    private static int expirationTme = 45;
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final String EMPTY_ANSWER = "";
    private static final int CODE_SUCCESS = 200;
    private static final int AUTH_FAILURE = 401;
    private static final int KEY_EXPIRED = 403;

    private LocalDateTime keyCreatedTime;

    private CipherInfoService cipherInfoService = new CipherInfoService();
    private AESService aesService = new AESService();

    //http://localhost:8080/server/openSession?rsaE=7&rsaN=3
    @GetMapping("/openSession")
    public BaseResponse getOpenRsaKey (@RequestParam(value = "rsaE") String rsaE,
                               @RequestParam(value = "rsaN") String rsaN) {
        cipherInfoService.setRsaE(new BigInteger(rsaE));
        cipherInfoService.setRsaN(new BigInteger(rsaN));
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, new ArrayList<>(Collections.singleton("Session opened")));
    }

    //http://localhost:8080/server/createSessionKey
    //answer - {"status":"success","code":200,"content":""}
    @GetMapping(value = "/createSessionKey", produces = "application/json")
    public BaseResponse createSessionKey() throws UnsupportedEncodingException {
        cipherInfoService.createSessionKey();
        keyCreatedTime = LocalDateTime.now();
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, new ArrayList<>(Collections.singleton(cipherInfoService.getEncryptedSessionKey().toString())));
    }

    //http://localhost:8080/server/file?name=testFile1.txt
    //answer - {"status":"success","code":200,"content":""}
    @GetMapping(value = "/file", produces = "application/json")
    public BaseResponse encryptFile(@RequestParam(value = "name") String fileName) throws IOException {
        if (LocalDateTime.now().minusSeconds(expirationTme).isAfter(keyCreatedTime)) {
            return new BaseResponse(ERROR_STATUS, KEY_EXPIRED, new ArrayList<>(Collections.singleton("")));
        }

        String encryptedFile = "";
        encryptedFile = aesService.cipherAES(fileName, cipherInfoService.getSessionKey());
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, new ArrayList<>(Arrays.asList(encryptedFile, aesService.getInitialVectorString())));
    }

}
