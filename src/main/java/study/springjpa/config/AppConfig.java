package study.springjpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
//@EnableJpaRepositories(basePackages = "study.springjpa.repository")
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID 받음.
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
