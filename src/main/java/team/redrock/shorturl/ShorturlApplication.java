package team.redrock.shorturl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import team.redrock.shorturl.been.ShortUrl;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class ShorturlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShorturlApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(ShorturlApplication.class);
    }
}
