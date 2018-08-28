package team.redrock.shorturl.been;

import lombok.Data;

/**
 * @Description 响应实体类
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Data
public class ResponseEntity {

    private int status;
    private String success;
    private String shortUrl;

    public ResponseEntity(int status, String success){
        this.status=status;
        this.success=success;
    }

    public ResponseEntity(int status, String success, String shortUrl){
        this.status=status;
        this.success=success;
        this.shortUrl = shortUrl;
    }

}
