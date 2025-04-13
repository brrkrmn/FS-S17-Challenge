package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.model.CourseGpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final List<Course> courses = new ArrayList<>();
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;

    @Autowired
    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("highCourseGpa") CourseGpa highCourseGpa
    ) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @GetMapping
    public List<Course> getAll() {
        return courses;
    }

    @GetMapping("/{name}")
    public Course get(@PathVariable String name) {
        return courses.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course could not be found", HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int create(@RequestBody Course course) {
        if (course.getCredit() == null || course.getId() == null || course.getName() == null || course.getGrade() == null) {
            throw new ApiException("Course fields cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (course.getCredit() < 1 || course.getCredit() > 4) {
            throw new ApiException("Course credit must be between 1 and 4", HttpStatus.BAD_REQUEST);
        }

        courses.add(course);

        return course.getGrade().getCoefficient() * course.getCredit() * (course.getCredit() <= 2
                ? lowCourseGpa.getGpa()
                : course.getCredit() == 3
                ? mediumCourseGpa.getGpa()
                : highCourseGpa.getGpa());
    }

    @PutMapping("{id}")
    public Course update(@PathVariable int id, @RequestBody Course course) {
        Course current = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course could not be found", HttpStatus.NOT_FOUND));

        current.setName(course.getName());
        current.setCredit(course.getCredit());
        current.setGrade(course.getGrade());

        return current;


    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        Course course = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course could not be found", HttpStatus.NOT_FOUND));

        courses.remove(course);
    }
}
