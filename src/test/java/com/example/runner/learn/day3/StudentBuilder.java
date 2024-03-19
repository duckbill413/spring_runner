package com.example.runner.learn.day3;

public interface StudentBuilder {
    StudentBuilder name(String name);
    StudentBuilder age(int grade);
    Student build();
}