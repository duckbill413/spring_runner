package com.example.runner.global.config;

import com.example.runner.domain.course.application.CourseService;
import io.micrometer.core.instrument.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseTrackerMetricsConfig {
    /**
     * Counter는 증가할 수 있는 개수나 횟수를 의미
     * 아래에서는 어떤 메소드가 호출되는 횟수를 카운팅
     * http://localhost:8081/api/actuator/metrics/api.courses.created.count
     */
    // http://localhost:8081/api/actuator/metrics/api.courses.created.count 에서 확인 가능
    @Bean
    public Counter createCourseCounter(MeterRegistry meterRegistry) {
        return Counter.builder("api.courses.created.count")
                .description("Total number of courses created")
                .register(meterRegistry);
    }

    /**
     * Counter의 단점은 메모리 기반으로 운영되기 때문에 애플리케이션이 재시작되면 초기화 된다.
     * Guage는 카운터와 마찬가지 이지만, 데이터베이스를 이용해서 값을 저장한다.
     * http://localhost:8081/api/actuator/metrics/api.courses.created.gauge
     */
    @Bean
    public Gauge createCoursesGauge(MeterRegistry meterRegistry, CourseService courseService) {
        return Gauge.builder("api.courses.created.gauge", courseService::count)
                .description("Total courses created")
                .register(meterRegistry);
    }

    /**
     * 개수나 횟수를 셀 때 Counter나 Gauge를 사용할 수 있다. 하지만 연산에 소요된느 시간을 측정해야 할 때도 있다.
     * 또한, 수행 시간이 결정적으로 중요한 애플리케이션에서는 SLA에서 정한 시간 내에 작업이 완료되는지 확인해야 한다.
     * Timer 인터페이스의 recordCallable() 메서드를 사용해서 과정을 생성
     * 타이머는 내부적으로 Callable 객체 안에서 과정 생성시 소요되는 시간을 측정
     * recordCallable은 예외를 던질 수 있으므로 @SneakyThrows 애너테이션을 사용해서 처리
     * http://localhost:8081/api/actuator/metrics/api.courses.creation.time
     */
    @Bean
    public Timer createCoursesTimer(MeterRegistry meterRegistry) {
        return Timer.builder("api.courses.creation.time")
                .description("Course creation time")
                .register(meterRegistry);
    }

    /**
     * 분포 요약 (Distribution Summary)은 이벤트의 분포를 측정
     * Timer와 구조적으로 유사하지만 측정 단위가 시간이 아니다.
     * COUNT는 생성된 과정의 수
     * TOTAL은 과정에 매겨진 평점의 총합
     * MAX는 최고 평점을 의미
     * http://localhost:8081/api/actuator/metrics/api.courses.rating.distribution.summary
     */
    @Bean
    public DistributionSummary createDistributionSummary(MeterRegistry meterRegistry) {
        return DistributionSummary.builder("api.courses.rating.distribution.summary")
                .description("Rating distribution summary")
                .register(meterRegistry);
    }
}
