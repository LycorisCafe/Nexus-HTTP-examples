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

package io.github.lycoriscafe;

import io.github.lycoriscafe.nexus.http.HttpServer;
import io.github.lycoriscafe.nexus.http.core.headers.auth.scheme.basic.BasicAuthentication;
import io.github.lycoriscafe.nexus.http.helper.configuration.HttpServerConfiguration;
import io.github.lycoriscafe.nexus.http.helper.scanners.ScannerException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ScannerException, SQLException, IOException {
        // HTTP server initialization
        var httpServerConfiguration = new HttpServerConfiguration("io.github.lycoriscafe.endpoints", "NexusTemp")
                .addDefaultAuthentication(new BasicAuthentication("Protected Realm"));
        var httpServer = new HttpServer(httpServerConfiguration);
        httpServer.initialize();

        // HTTPS server initialization
//        var httpsServerConfiguration = new HttpsServerConfiguration("io.github.lycoriscafe.endpoints", "NexusTemp",
//                "myTrustStore", "password".toCharArray(), "myKeyStore", "password".toCharArray());
//        var httpsServer = new HttpsServer(httpsServerConfiguration);
//        httpsServer.initialize();
    }
}