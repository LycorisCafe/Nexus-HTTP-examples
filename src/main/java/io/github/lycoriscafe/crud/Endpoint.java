/*
 * Copyright 2025 Lycoris Café
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

import io.github.lycoriscafe.nexus.http.core.HttpEndpoint;
import io.github.lycoriscafe.nexus.http.core.headers.content.Content;
import io.github.lycoriscafe.nexus.http.core.headers.content.ExpectContent;
import io.github.lycoriscafe.nexus.http.core.headers.content.MultipartFormData;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.DELETE;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.GET;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.POST;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpReq.HttpDeleteRequest;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpReq.HttpGetRequest;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpReq.HttpPostRequest;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpRes.HttpResponse;

import java.util.List;


@HttpEndpoint("/")
public class Endpoint {
    @POST("/addStudent")
    @SuppressWarnings("unchecked")
    @ExpectContent("multipart/form-data")
    public static HttpResponse addStudent(HttpPostRequest request,
                                          HttpResponse response) {
        String name = null, address = null;
        List<MultipartFormData> formData = (List<MultipartFormData>) request.getContent().getData();
        for (MultipartFormData multipartFormData : formData) {
            switch (multipartFormData.getName()) {
                case "name" -> name = new String(multipartFormData.getData());
                case "address" -> address = new String(multipartFormData.getData());
            }
        }

        if (name == null || address == null) {
            return response.setContent(new Content("text/plain", "Invalid data!"));
        }

        Student student = new Student(name, address);
        Student addedStudent = Database.addStudent(student);
        if (addedStudent == null) {
            return response.setContent(new Content("text/plain", "Error!"));
        }

        return response.setContent(new Content("text/plain", addedStudent.toString()));
    }

    @GET("/getAllStudents")
    public static HttpResponse getAllStudents(HttpGetRequest request,
                                              HttpResponse response) {
        List<Student> students = Database.getAllStudents();
        if (students == null) {
            return response.setContent(new Content("text/plain", "Error!"));
        }

        StringBuilder builder = new StringBuilder();
        for (Student student : students) {
            builder.append(student.toString()).append("\n");
        }
        return response.setContent(new Content("text/plain", builder.toString()));
    }

    @POST("/updateStudent")
    @SuppressWarnings("unchecked")
    @ExpectContent("multipart/form-data")
    public static HttpResponse updateStudent(HttpPostRequest request,
                                             HttpResponse response) {
        int id = Integer.parseInt(request.getParameters().get("id"));
        String name = null, address = null;
        List<MultipartFormData> formData = (List<MultipartFormData>) request.getContent().getData();
        for (MultipartFormData multipartFormData : formData) {
            switch (multipartFormData.getName()) {
                case "name" -> name = new String(multipartFormData.getData());
                case "address" -> address = new String(multipartFormData.getData());
            }
        }

        if (name == null || address == null) {
            return response.setContent(new Content("text/plain", "Invalid data!"));
        }

        Student student = new Student(name, address);
        Student updatedStudent = Database.updateStudent(id, student);
        if (updatedStudent == null) {
            return response.setContent(new Content("text/plain", "Error!"));
        }

        return response.setContent(new Content("text/plain", updatedStudent.toString()));
    }

    @DELETE("/deleteStudent")
    public static HttpResponse deleteStudent(HttpDeleteRequest request,
                                             HttpResponse response) {
        int id = Integer.parseInt(request.getParameters().get("id"));
        Database.deleteStudent(id);
        return response.setContent(new Content("text/plain", "Deleted!"));
    }
}
