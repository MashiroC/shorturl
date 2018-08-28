package team.redrock.shorturl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.redrock.shorturl.mapper.UrlMapper;

/**
 * @Description 清理数据库超时的数据
 * @Author 余歌
 * @Date 2018/8/28
 **/
@Component
public class CleanDbTasks {

    @Autowired
    private UrlMapper urlMapper;

    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000)
    public void cleanDb(){
        urlMapper.clean(String.valueOf(System.currentTimeMillis()));
    }

}
