package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Student;

import java.util.Scanner;

public class AccountOperations {
    public Student checkIfAccountExist() {
        EntityManager entityManager;
        EntityManagerProvider entityManagerProvider = new EntityManagerProvider();
        entityManager = entityManagerProvider.getEntityManager();

        System.out.println("Do you have an account? Please enter true/false");
        Scanner scanner = new Scanner(System.in);
        boolean accountIsActive = scanner.nextBoolean();
        Student activeStudent = new Student();
        StudentConsole studentConsole = new StudentConsole(entityManager);

        if (accountIsActive) {
            scanner.nextLine();
            System.out.println("Please enter your name");
            String studentName = scanner.nextLine();
            activeStudent = studentConsole.findStudentByStudentName(studentName);
        } else if (!accountIsActive) {
            System.out.println("Please add a new account");
            studentConsole.addNewStudent();
            activeStudent = new Student();

        }
        return activeStudent;
    }
}
