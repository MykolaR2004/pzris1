package org.example.pzris1.controllers;

import org.example.pzris1.entity.Student;
import org.example.pzris1.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class StudentController {

    private final StudentService studentService;
    private List<Student> previousState = new ArrayList<>();

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String getStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "index";
    }

    @PostMapping("/database/query")
    @ResponseBody
    public ResponseEntity<?> executeQuery(
            @RequestParam("sql") String sql,
            @RequestParam("connectionType") String connectionType) {
        try {
            previousState = studentService.getAllStudents();
            Object result = studentService.executeQuery(sql, connectionType);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error executing query: " + e.getMessage());
        }
    }

    @GetMapping("/students")
    @ResponseBody
    public List<Student> getUpdatedStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/previous")
    @ResponseBody
    public List<Student> getPreviousStudents() {
        return previousState;
    }
}






