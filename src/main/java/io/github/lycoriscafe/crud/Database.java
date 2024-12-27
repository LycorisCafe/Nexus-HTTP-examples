/*
 * Copyright 2024 Lycoris Café
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lycoriscafe.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    // lock is used because the sqlite database cannot be written by multiple threads
    private static final Lock lock = new ReentrantLock();
    private static Connection connection;

    public static void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE student(" +
                    "ROWID INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL" +
                    ")");
        }
    }

    public static Student addStudent(Student student) {
        lock.lock();
        Student insertedStudent = null;
        System.out.println(student);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student (name, address) VALUES (?,?)");
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getAddress());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student WHERE ROWID = (SELECT MAX(ROWID) FROM student)");
            insertedStudent = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.atInfo().log("Error while adding student - ", e);
        }

        lock.unlock();
        return insertedStudent;
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.atInfo().log("Error while getting all student - ", e);
            return null;
        }
        return students;
    }

    public static Student updateStudent(int id,
                                        Student student) {
        lock.lock();
        Student updatedStudent = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student SET name = ?, address = ? WHERE rowid = ?");
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getAddress());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM student WHERE rowid = ?");
            preparedStatement1.setInt(1, id);
            ResultSet resultSet = preparedStatement1.executeQuery();
            updatedStudent = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            resultSet.close();
            preparedStatement1.close();
        } catch (SQLException e) {
            logger.atInfo().log("Error while updating student - ", e);
        }

        lock.unlock();
        return updatedStudent;
    }

    public static void deleteStudent(int id) {
        lock.lock();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM student WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.atInfo().log("Error while deleting student - ", e);
        }

        lock.unlock();
    }
}
