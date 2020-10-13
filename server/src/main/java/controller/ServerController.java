package controller;

import model.BaseResponse;
import org.springframework.web.bind.annotation.*;
import service.AESService;
import service.CipherInfoService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/server")
public class ServerController {

    private static int expirationTme = 45; // seconds ??
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final String EMPTY_ANSWER = "";
    private static final int CODE_SUCCESS = 200;
    private static final int AUTH_FAILURE = 401;
    private static final int KEY_EXPIRED = 403;

    private CipherInfoService cipherInfoService = new CipherInfoService();
    private AESService aesService = new AESService();

    //@GetMapping("/getOpenRsaKey")
    @GetMapping("/openSession")
    public void getOpenRsaKey (@RequestParam(value = "rsaE") String rsaE,
                               @RequestParam(value = "rsaN") String rsaN) {
        cipherInfoService.setRsaE(Integer.parseInt(rsaE));
        cipherInfoService.setRsaN(Integer.parseInt(rsaN));
    }

    @PostMapping(value = "/createSessionKey", produces = "application/json")
    public BaseResponse createSessionKey() {
        cipherInfoService.createSessionKey();
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, cipherInfoService.getSessionKey());
    }

    //@PostMapping(value = "/encryptFile", consumes = "application/json", produces = "application/json")
    @PostMapping(value = "/file", produces = "application/json")
    public BaseResponse encryptFile(@RequestParam(value = "name") String fileName) {
    //@PostMapping(value = "/file", consumes = "application/json", produces = "application/json")
    //public BaseResponse encryptFile(@RequestBody String fileName) {
        if (LocalDateTime.now().minusSeconds(expirationTme).isAfter(cipherInfoService.getKeyCreatedTime())) {
            return new BaseResponse(ERROR_STATUS, KEY_EXPIRED, EMPTY_ANSWER);
        }
        String encryptedFile = "";
        // запуск encrypt из класса AESService
        encryptedFile = aesService.cipherAES(fileName);
        return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS, encryptedFile);
    }

}
