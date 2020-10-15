package controller;

import model.BaseResponse;
import org.springframework.web.bind.annotation.*;
import service.AESService;
import service.CipherInfoService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@RestController
@RequestMapping("/server")
public class ServerController {

    private static int expirationTme = 45;
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final String EMPTY_ANSWER = "";
    private static final int CODE_SUCCESS = 200;
    private static final int AUTH_FAILURE = 401;
    private static final int KEY_EXPIRED = 403;

    private CipherInfoService cipherInfoService = new CipherInfoService();
    private AESService aesService = new AESService();

    //http://localhost:8080/server/openSession?rsaE=7&rsaN=3
    @GetMapping("/openSession")
    public BaseResponse getOpenRsaKey (@RequestParam(value = "rsaE") String rsaE,
                               @RequestParam(value = "rsaN") String rsaN) {
        cipherInfoService.setRsaE(new BigInteger(rsaE));
        cipherInfoService.setRsaN(new BigInteger(rsaN));
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, "Session opened");
    }

    //http://localhost:8080/server/createSessionKey
    //answer - {"status":"success","code":200,"content":"L9DOBLZ8M739T3C1"}
    @GetMapping(value = "/createSessionKey", produces = "application/json")
    public BaseResponse createSessionKey() throws UnsupportedEncodingException {
        cipherInfoService.createSessionKey();
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, cipherInfoService.getSessionKey());
    }

    //http://localhost:8080/server/file?name=testFile1.txt
    //answer - {"status":"success","code":200,"content":"��-\u000F$�������v�x~��������\u0014\u0017�\fA�\u0016"}
    @GetMapping(value = "/file", produces = "application/json")
    public BaseResponse encryptFile(@RequestParam(value = "name") String fileName) throws IOException {
        String encryptedFile = "";
        encryptedFile = aesService.cipherAES(fileName, cipherInfoService.getEncryptedSessionKey());
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, encryptedFile);
    }

}
