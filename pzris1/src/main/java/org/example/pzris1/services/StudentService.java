package org.example.pzris1.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.pzris1.entity.Student;
import org.example.pzris1.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final DataSource dataSource;

    @Autowired
    public StudentService(StudentRepository studentRepository, DataSource dataSource) {
        this.studentRepository = studentRepository;
        this.dataSource = dataSource;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public Object executeQuery(String sql, String connectionType) {
        if ("JDBC".equalsIgnoreCase(connectionType)) {
            return executeQueryJDBC(sql);
        } else if ("JPA".equalsIgnoreCase(connectionType)) {
            return executeQueryJPA(sql);
        } else {
            throw new IllegalArgumentException("Invalid connection type: " + connectionType);
        }
    }

    private Object executeQueryJDBC(String sql) {
        List<Student> result = new ArrayList<>();
        String trimmedSql = sql.trim().toLowerCase();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            if (trimmedSql.startsWith("select")) {
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getLong("id"));
                    student.setName(rs.getString("name"));
                    student.setSurname(rs.getString("surname"));
                    student.setStudentGroup(rs.getInt("student_group"));
                    student.setPaidForm(rs.getString("paid_form"));
                    result.add(student);
                }
                return result;
            } else {
                int affectedRows = statement.executeUpdate(sql);
                return "Query executed successfully, affected rows: " + affectedRows;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing query via JDBC: " + e.getMessage(), e);
        }
    }

    private Object executeQueryJPA(String sql) {
        String trimmedSql = sql.trim().toLowerCase();
        if (trimmedSql.startsWith("select")) {
            List<Student> result = entityManager.createNativeQuery(sql, Student.class).getResultList();
            return result;
        } else {
            int affectedRows = entityManager.createNativeQuery(sql).executeUpdate();
            return "Query executed successfully, affected rows: " + affectedRows;
        }
    }
}
