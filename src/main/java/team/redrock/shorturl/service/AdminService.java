package team.redrock.shorturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.shorturl.been.ShortUrl;
import team.redrock.shorturl.mapper.UrlMapper;
import team.redrock.shorturl.util.StringUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Service
@Component
public class AdminService {

    @Autowired
    private UrlMapper urlMapper;

    private MessageDigest md;

    public AdminService() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
    }

    @Cacheable(value = "shortUrl", key = "'shortUrl'+#text+#password")
    public synchronized ShortUrl creatShortUrl(String text, String keyword, int expiration, String password) {
        ShortUrl shortUrl = urlMapper.findShortUrlByOriginUrl(text);
        //如果找到了则直接返回找到的 没有进行下一步操作
        if (shortUrl == null || !StringUtil.isEmpty(password)) {
            if (!StringUtil.isEmpty(keyword)) {
                //如果keyword已存在就返回null
                if (urlMapper.findShortUrlByCode(keyword) != null) {
                    return null;
                } else {
                    //如果keyword不存在就储存
                    shortUrl = new ShortUrl(text, keyword, password, expiration, String.valueOf(System.currentTimeMillis()));
                }
            } else {
                //生成短链接code
                shortUrl = createShortUrl(text, password, expiration);
            }
            urlMapper.insert(shortUrl);

        } else {
            shortUrl.setTimestamp(String.valueOf(System.currentTimeMillis()));
            int dbExpiration = shortUrl.getExpiration();
            shortUrl.setExpiration(dbExpiration > expiration ? dbExpiration : expiration);
            urlMapper.update(shortUrl);
        }
        return shortUrl;
    }

    private ShortUrl createShortUrl(String text, String password, int expiration) {
        String code = md5(text);
        if (!StringUtil.isEmpty(password)) {
            code = code.substring(4, 8);
        } else {
            code = code.substring(0, 4);
        }
        ShortUrl dbShortUrl = urlMapper.findShortUrlByCode(code);
        if (dbShortUrl != null) {
            code = dbShortUrl.getShortCode() + (char) (ThreadLocalRandom.current().nextInt(97, 122));
        }
        return new ShortUrl(text, code, password, expiration, String.valueOf(System.currentTimeMillis()));
    }

    public ShortUrl findOriginUrl(String code) {
        return urlMapper.findShortUrlByCode(code);
    }


    public boolean isKeywordExist(String keyword) {
        return urlMapper.findShortUrlByCode(keyword) != null;
    }

    private String md5(String text) {
        md.update(text.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }
}
