package com.example.runner.domain.course.decorator;

import io.micrometer.core.instrument.DistributionSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistributionSummaryWrapper {
    private final DistributionSummary distributionSummary;
    public void record(int rating) {
        distributionSummary.record(rating);
    }
}
