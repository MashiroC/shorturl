package team.redrock.shorturl.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.shorturl.been.ShortUrl;

/**
 * @Description
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Mapper
@Component
public interface UrlMapper {

    @Select("SELECT * FROM url WHERE short_code = #{code}")
    ShortUrl findShortUrlByCode(@Param("code") String code);

    @Select("SELECT * FROM url WHERE origin_url=#{originUrl} AND password = ''")
    ShortUrl findShortUrlByOriginUrl(String originUrl);

    @Insert("INSERT INTO url(short_code,origin_url,password,expiration,timestamp) VALUE(#{shortCode},#{originUrl},#{password},#{expiration},#{timestamp})")
    void insert(ShortUrl shortUrl);

    @Update("UPDATE url SET timestamp=#{timestamp} WHERE short_code=#{shortCode}")
    void update(ShortUrl shortUrl);

    @Delete("DELETE FROM url WHERE timestamp+expiration*24*60*60*1000<#{timestamp}")
    void clean(String timestamp);
}
