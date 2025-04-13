package com.workintech.spring17challenge.entity;

import com.workintech.spring17challenge.exceptions.ApiException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Course {
    private Integer id;
    private String name;
    private Integer credit;
    private Grade grade;
}
