package team.redrock.shorturl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.redrock.shorturl.util.StringUtil;
import team.redrock.shorturl.been.ResponseEntity;
import team.redrock.shorturl.been.ShortUrl;
import team.redrock.shorturl.service.AdminService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @Description
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Controller
public class AdminController {

    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private AdminService adminService;

    private ResponseEntity PARAM_ERROR = new ResponseEntity(403, "param error");

    private ResponseEntity KEYWORD_EXIST = new ResponseEntity(406, "keyword exist");

    private ResponseEntity USE_PASSWORD = new ResponseEntity(403,"please use passwd");

    private ResponseEntity OK = new ResponseEntity(200,"ok");

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity getShortUrl(String url, String keyword, Integer expiration, String password) throws NoSuchAlgorithmException {
        if (url != null) {

            //如果不是
            if (!url.matches("^(https?)://.*?")) {
                url = "http://" + url;
            }

            if (!url.endsWith("/")) {
                url = url + "/";
            }

            if (expiration != null) {
                if (expiration < 0 || expiration > 365) {
                    return PARAM_ERROR;
                }
            } else {
                //如果没设置过期时间那么默认一天
                expiration = 1;
            }
            if (!(keyword == null || keyword.length() == 0)) {
                if (adminService.isKeywordExist(keyword)) {
                    return KEYWORD_EXIST;
                }
            }

            ShortUrl shortUrl = adminService.creatShortUrl(url, keyword, expiration, password);

            if (shortUrl != null) {
                return new ResponseEntity(200, "ok", toUrl(shortUrl));
            }
        }
        return PARAM_ERROR;
    }

    @GetMapping("/u/{url}")
    @ResponseBody
    public ResponseEntity redirect(@PathVariable("url") String code, HttpServletResponse response) throws IOException {
        System.out.println("get "+code);
        ShortUrl shortUrl = adminService.findOriginUrl(code);
        if (shortUrl != null) {
            if (!StringUtil.isEmpty(shortUrl.getPassword())) {
                response.sendRedirect("/p/" + code);
                return USE_PASSWORD;
            }
            response.sendRedirect(shortUrl.getOriginUrl());
            return OK;
        }
        return PARAM_ERROR;
    }

    @PostMapping("/u/{url}")
    @ResponseBody
    public ResponseEntity redirectWithPasswd(@PathVariable("url") String code, @RequestParam("passwd") String password, HttpServletResponse response) throws IOException {
        ShortUrl shortUrl = adminService.findOriginUrl(code);
        if (shortUrl != null) {
            if (!StringUtil.isEmpty(shortUrl.getPassword()) && !StringUtil.isEmpty(code)) {
                if(shortUrl.getPassword().equals(password)){
                    String url = shortUrl.getOriginUrl();
                    return new ResponseEntity(200,"ok",url);
                }
            }
        }
        return PARAM_ERROR;
    }

    @GetMapping("/p/{url}")
    public String usePasswd() {
        return "usePasswd";
    }

    public String toUrl(ShortUrl shortUrl) {
        if (shortUrl != null) {
            System.out.println(shortUrl.getShortCode());
            StringBuilder sb = new StringBuilder(serverUrl);
            sb.append("u/").append(shortUrl.getShortCode());
            return sb.toString();
        }
        return null;
    }

}
