package org.flagsmith;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor

        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("GET".equals(t.getRequestMethod())) {
                handleGetBooksRequest(t);
            } else if ("POST".equals(t.getRequestMethod())) {
                handlePostBooksRequest(t);
            }
        }

        private void handleGetBooksRequest(HttpExchange t) throws IOException {

        }


        private void handlePostBooksRequest(HttpExchange t) throws IOException {

        }
    }
}