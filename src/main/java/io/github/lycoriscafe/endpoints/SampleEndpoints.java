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

package io.github.lycoriscafe.endpoints;

import io.github.lycoriscafe.nexus.http.core.HttpEndpoint;
import io.github.lycoriscafe.nexus.http.core.headers.content.Content;
import io.github.lycoriscafe.nexus.http.core.headers.content.MultipartFormData;
import io.github.lycoriscafe.nexus.http.core.headers.content.UrlEncodedData;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.GET;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.POST;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpReq.HttpGetRequest;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpReq.HttpPostRequest;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpRes.HttpResponse;

import java.util.List;

@HttpEndpoint("/")
public class SampleEndpoints {
    @GET("/")
    public static HttpResponse home(HttpGetRequest request,
                                    HttpResponse response) {
        return response;
    }

    @POST("/samplePostContentEndpoint")
    public static HttpResponse samplePostContentEndpoint(HttpPostRequest request,
                                                         HttpResponse response) {
        if (request.getContent() != null && request.getContent().getContentType().equals("multipart/form-data")) {
            List<MultipartFormData> formData = ((List<?>) request.getContent().getData()).stream().map(x -> (MultipartFormData) x).toList();
            for (MultipartFormData multipartFormData : formData) {
                switch (multipartFormData.getName()) {
                    case "name" -> System.out.println("Name: " + new String(multipartFormData.getData()));
                    case "from" -> System.out.println("From: " + new String(multipartFormData.getData()));
                }
            }
        }
        return response;
    }

//    @POST("/samplePostContentEndpoint")
//    @SuppressWarnings("unchecked")
//    public static HttpResponse samplePostContentEndpoint(HttpPostRequest request,
//                                                         HttpResponse response) {
//        if (request.getContent() != null && request.getContent().getContentType().equals("multipart/form-data")) {
//            List<MultipartFormData> formData = (List<MultipartFormData>) request.getContent().getData();
//            for (MultipartFormData multipartFormData : formData) {
//                switch (multipartFormData.getName()) {
//                    case "name" -> System.out.println("Name: " + new String(multipartFormData.getData()));
//                    case "from" -> System.out.println("From: " + new String(multipartFormData.getData()));
//                }
//            }
//        }
//        return response;
//    }

    @POST("/sampleGeneralContentEndpoint")
    public static HttpResponse sampleGeneralContentEndpoint(HttpPostRequest request,
                                                            HttpResponse response) {
        if (request.getContent() != null && request.getContent().getContentType().equals("text/plain")) {
            System.out.println(new String((byte[]) request.getContent().getData()));
        }
        return response;
    }

    @POST("/sampleUrlEncodedContentEndpoint")
    public static HttpResponse sampleUrlEncodedContentEndpoint(HttpPostRequest request,
                                                               HttpResponse response) {
        if (request.getContent() != null && request.getContent().getContentType().equals("application/x-www-form-urlencoded")) {
            var urlEncodedData = (UrlEncodedData) request.getContent().getData();
            for (var entry : urlEncodedData.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        return response;
    }

    @GET("/sampleContentResponse")
    public static HttpResponse sampleContentResponse(HttpGetRequest request,
                                                     HttpResponse response) {
        var content = new Content("text/plain", "Hello World");
        response.setContent(content);
        return response;
    }
}
