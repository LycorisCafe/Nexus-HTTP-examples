/*
 * Copyright 2024 Lycoris Cafe
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
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.GET;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.POST;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpReq.HttpGetRequest;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpReq.HttpPostRequest;
import io.github.lycoriscafe.nexus.http.engine.ReqResManager.httpRes.HttpResponse;

import java.util.List;

@HttpEndpoint("/")
public class SampleEndpoints {
    @GET("/")
    public static HttpResponse sampleGetEndpoint(HttpGetRequest request,
                                                    HttpResponse response) {
        return response;
    }

    @POST("/samplePostEndpoint")
    @SuppressWarnings("unchecked")
    public static HttpResponse samplePostEndpoint(HttpPostRequest request,
                                                  HttpResponse response) {
        System.out.println("called!");
        String name = null, from = null;
        if (request.getContent() != null && request.getContent().getContentType().equals("multipart/form-data")) {
            List<MultipartFormData> formData = (List<MultipartFormData>) request.getContent().getData();
            for (MultipartFormData multipartFormData : formData) {
                switch (multipartFormData.getName()) {
                    case "name" -> name = new String(multipartFormData.getData());
                    case "from" -> from = new String(multipartFormData.getData());
                }
            }
        }
        return response.setContent(new Content("text/plain", "Hello " + name + " from " + from));
    }


}
