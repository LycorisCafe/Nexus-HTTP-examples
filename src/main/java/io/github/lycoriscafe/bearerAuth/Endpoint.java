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

package io.github.lycoriscafe.bearerAuth;

import io.github.lycoriscafe.nexus.http.core.HttpEndpoint;
import io.github.lycoriscafe.nexus.http.core.headers.auth.AuthScheme;
import io.github.lycoriscafe.nexus.http.core.headers.auth.Authenticated;
import io.github.lycoriscafe.nexus.http.core.headers.auth.scheme.bearer.BearerAuthentication;
import io.github.lycoriscafe.nexus.http.core.headers.auth.scheme.bearer.BearerAuthorization;
import io.github.lycoriscafe.nexus.http.core.headers.auth.scheme.bearer.BearerAuthorizationError;
import io.github.lycoriscafe.nexus.http.core.headers.content.Content;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.GET;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpReq.HttpGetRequest;
import io.github.lycoriscafe.nexus.http.engine.reqResManager.httpRes.HttpResponse;

@HttpEndpoint("/")
public class Endpoint {
    @GET("/")
    public static HttpResponse home(HttpGetRequest request,
                                    HttpResponse response) {
        return response.setContent(new Content("text/plain", "Hello World!"));
    }

    @GET("/authEndpoint")
    @Authenticated
    public static HttpResponse authEndpoint(HttpGetRequest request,
                                            HttpResponse response) {
        if (request.getAuthorization().getAuthScheme() == AuthScheme.BEARER) {
            BearerAuthorization auth = (BearerAuthorization) request.getAuthorization();
            if (auth.getAccessToken().equals("thisissamplebearertoken1234")) {
                return response.setContent(new Content("text/plain", "Authenticated!"));
            }
        }

        return response.addAuthentication(new BearerAuthentication(BearerAuthorizationError.INVALID_TOKEN));
    }
}
