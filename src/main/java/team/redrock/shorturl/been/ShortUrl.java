package team.redrock.shorturl.been;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Data
public class ShortUrl implements Serializable {
    private String originUrl;
    private String shortCode;
    private String password;
    private Integer expiration;
    private String timestamp;

    public ShortUrl() {

    }

    public ShortUrl(String originUrl, String shortCode, String password, Integer expiration, String timestamp) {
        this.originUrl = originUrl;
        this.shortCode = shortCode;
        this.password = password;
        this.expiration = expiration;
        this.timestamp = timestamp;
    }

    public ShortUrl(String originUrl, String shortCode, Integer expiration, String timestamp) {
        this.originUrl = originUrl;
        this.shortCode = shortCode;
        this.expiration = expiration;
        this.timestamp = timestamp;
    }
}
