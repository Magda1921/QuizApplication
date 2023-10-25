package org.example;

import org.example.model.Student;
import org.example.repository.StudentRepository;

import java.util.List;
import java.util.Scanner;

public class StudentService {
    Scanner scanner = new Scanner(System.in);
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Student retrieveStudentFromUser() {
        System.out.println("Enter your name");
        String studentName = scanner.nextLine();

        Student student = new Student();
        student.setName(studentName);
        return student;
    }

    void addAndSaveNewStudent(Student student) {
        studentRepository.saveNewStudent(student);
    }

    Student findStudentByStudentName(String studentName) {
        List<Student> students = studentRepository.getListOfStudentsByStudentNameFromDB(studentName);

        Student student = new Student();
        if (students.size() > 1) {
            System.out.println("There is more than 1 student with " + studentName + " name. Please provide us with your student id");
            int studentId = scanner.nextInt();
            student = studentRepository.getStudentByStudentIdFromDB(studentId);
        } else if (students.size() == 1) {
            student = students.get(0);
        }
        return student;
    }

    public void showAllStudents() {
        List<Student> students = studentRepository.getListOfStudentsFromDB();
        for (Student student : students) {
            System.out.println("Student name: " + student.getName());
        }
    }
}
