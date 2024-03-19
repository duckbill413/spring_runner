package com.example.runner.domain.course.application;

import com.example.runner.domain.course.entity.Course;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseInfoContributor implements InfoContributor {
    private final CourseService courseService;

    /**
     * CourseTracker 애플리케이션의 과정별 이름과 평점이 info 엔드포인트를 통해 표시되도록 하는 커스텀 InfoContributor
     * contribute() 메서드를 재정의하면서 InfoContributor 인터페이스를 구현하고, courseService를 통해 모든 과정 정보를 읽어와서 과정 이름과 평점 목록을 구성하고 Info.Builder 인스턴스에 추가
     * 실제 서비스 운영시 액추에이터 엔드포인트로 비즈니스 도메인 정보를 노출하거나 수정하는 것은 좋지않다. 실무적으로 비지니스 도메인 정보는 REST API 웹 서비스로 관리하는 것이 좋다.
     * @param builder
     */
    @Override
    public void contribute(Info.Builder builder) {
        List<CourseNameRating> courseNameRatingList = new ArrayList<>();
        for (Course course : courseService.findAllCourses()) {
            courseNameRatingList.add(CourseNameRating.builder().name(course.getName()).rating(course.getRating()).build());
        }
        builder.withDetail("courses", courseNameRatingList);
    }
    @Builder
    @Data
    private static class CourseNameRating {
        String name;
        int rating;
    }
}
