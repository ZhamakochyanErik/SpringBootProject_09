package friendfinder.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FriendFinderNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendFinderNetApplication.class, args);
    }
}
