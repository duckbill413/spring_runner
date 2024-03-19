package com.example.runner;

import com.example.runner.global.actuator.release.model.ReleaseItem;
import com.example.runner.global.actuator.release.model.ReleaseNote;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Log4j2
@SpringBootApplication
public class SpringRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRunnerApplication.class, args);
    }

    /**
     * 애플리케이션에 특화된 비즈니스 상세 정보를 제공할 수 있는 액추에이터 엔드포인트 작성
     * 커스텀 엔드포인트를 추가하려면 @EndPoint 애너테이션을 붙여서 엔드포인트ㅔ 해당하는 자바 클래스를 정의하고
     *
     * @return
     * @ReadOperation, @WriteOperation, @DeleteOperation 애너테이션을 붙인다.
     */
    @Bean(name = "releaseNotes")
    public Collection<ReleaseNote> loadReleaseNotes() {
        Set<ReleaseNote> releaseNotes = new LinkedHashSet<>();
        ReleaseNote releaseNote1 = ReleaseNote.builder()
                .version("v1.2.1")
                .releaseDate(LocalDate.now().minusMonths(1L))
                .commitTag("a7d2ea3")
                .bugFixes(Set.of(
                        getReleaseItem("SBIP-123", "The name of the matching-strategy property is incorrect in the action message of the failure analysis for a PatternParseException #28839"),
                        getReleaseItem("SBIP-124", "ErrorPageSecurityFilter prevents deployment to a Servlet 3.1 compatible container #28790")))
                .build();

        ReleaseNote releaseNote2 = ReleaseNote.builder()
                .version("v1.2.0")
                .releaseDate(LocalDate.now())
                .commitTag("44047f3")
                .newReleases(Set.of(getReleaseItem("SBIP-125", "Support both kebab-case and camelCase as Spring init CLI Options #28138")))
                .bugFixes(Set.of(getReleaseItem("SBIP-126", "Profiles added using @ActiveProfiles have different precedence #28724")))
                .build();
        releaseNotes.addAll(Set.of(releaseNote1, releaseNote2));
        return releaseNotes;
    }

    private ReleaseItem getReleaseItem(String itemId, String itemDescription) {
        return ReleaseItem
                .builder()
                .itemId(itemId)
                .itemDescription(itemDescription)
                .build();
    }

    /**
     * MeterRegistryCustomizer를 사용한 커스텀 MeterRegistry
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags("application", "course-tracker");
    }
}
