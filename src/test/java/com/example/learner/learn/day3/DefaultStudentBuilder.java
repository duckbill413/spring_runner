package com.example.learner.learn.day3;

public class DefaultStudentBuilder implements StudentBuilder{
    private String name;
    private int grade;
    @Override
    public StudentBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public StudentBuilder age(int grade) {
        this.grade = grade;
        return this;
    }

    @Override
    public Student build() {
        // 기본 생성자가 반드시 필요한 것을 확인할 수 있다.
        Student student = new Student();
        student.setName(this.name);
        student.setGrade(this.grade);
        return student;
    }
}
