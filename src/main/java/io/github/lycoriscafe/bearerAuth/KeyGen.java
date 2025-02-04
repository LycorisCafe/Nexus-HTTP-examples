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
import io.github.lycoriscafe.nexus.http.core.headers.auth.scheme.bearer.*;
import io.github.lycoriscafe.nexus.http.core.requestMethods.annotations.POST;

@HttpEndpoint("/")
public class KeyGen {
    @BearerEndpoint(@POST("/generateToken"))
    public static BearerTokenResponse generateBearerToken(BearerTokenRequest request) {
        if (request.getParams() == null) {
            return new BearerTokenFailResponse(BearerTokenRequestError.INVALID_REQUEST);
        }
        switch (request.getGrantType()) {
            case "credentials" -> {
                if (request.getParams().get("username").equals("root") && request.getParams().get("password").equals("root")) {
                    return new BearerTokenSuccessResponse("thisissamplebearertoken1234").setRefreshToken("thisissamplebearertoken1234")
                            .setExpiresIn(3600);
                } else {
                    return new BearerTokenFailResponse(BearerTokenRequestError.INVALID_CLIENT);
                }
            }
            case "refresh_token" -> {
                if (request.getParams().get("refresh_token").equals("thisissamplebearertoken1234")) {
                    return new BearerTokenSuccessResponse("thisissamplebearertoken1234").setRefreshToken("thisissamplebearertoken1234")
                            .setExpiresIn(3600);
                } else {
                    return new BearerTokenFailResponse(BearerTokenRequestError.INVALID_CLIENT);
                }
            }
            default -> {
                return new BearerTokenFailResponse(BearerTokenRequestError.INVALID_GRANT);
            }
        }
    }
}
