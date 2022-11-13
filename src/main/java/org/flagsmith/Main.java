package org.flagsmith;

import com.flagsmith.FlagsmithClient;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    static List<Book> books = new ArrayList<>();
    static FlagsmithClient fsClient;


    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        populateDummyData();
        fsClient = getFlagsmithClient();
        server.createContext("/books", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    private static void populateDummyData() {
        books.add(new Book("1", "Harry Potter", "J.K. Rowling", "20.99"));
        books.add(new Book("2", "War and Peace", "Leo Tolstoy", "26.99"));
        books.add(new Book("3", "The Kite Runner", "Khaled Hosseini", "30.99"));
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
            OutputStream os = t.getResponseBody();
            String json = new Gson().toJson(books);
            t.sendResponseHeaders(200, json.length());
            os.write(json.getBytes());
            os.close();
        }


        private void handlePostBooksRequest(HttpExchange t) throws IOException {
            Book book = getBookFromRequest(t);
            books.add(book);
            OutputStream os = t.getResponseBody();
            String resp = "book added successfully";
            t.sendResponseHeaders(200, resp.length());
            os.write(resp.getBytes());
            os.close();
        }
        private static Book getBookFromRequest(HttpExchange t) throws IOException {
            InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            Gson gson = new Gson();
            return gson.fromJson(query, Book.class);
        }

    }
    private static FlagsmithClient getFlagsmithClient() {
        String apiKey = readFlagsmithApiKey();
        return FlagsmithClient
                .newBuilder()
                .setApiKey(apiKey)
                .build();
    }
    private static String readFlagsmithApiKey() {
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(propFileName);
        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("config file input stream is null");
        }
        return prop.getProperty("apiKey");
    }
}