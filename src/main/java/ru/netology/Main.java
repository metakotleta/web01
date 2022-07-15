package ru.netology;

import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();

        // добавление handler'ов (обработчиков)
        //просто отдаст 200
        server.addHandler("POST", "/urlencode", (request, responseStream) -> {
            var content = request.getBody() + "\r\n" + request.getQueryList().toString() + "\r\n";
        //    URLEncodedUtil.
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: " + content.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write((
                    request.getBody() + "\r\n" +
                            request.getQueryList().toString() + "\r\n"
            ).getBytes());
            responseStream.flush();
        });
        //вернет body запроса
        //пока с допущением, что тело будет однострочны
        server.addHandler("POST", "/messages", (request, responseStream) -> {
            try {
                var content = request.getBody();
                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: " + ((content != null) ? content.length() : "0") + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                if (content != null) responseStream.write(content.getBytes());
                responseStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.addHandler("GET", "/classic.html", (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var template = Files.readString(filePath);
            final var content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(content);
            responseStream.flush();
        });

        server.addHandler("GET", "/index.html", (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var template = Files.readString(filePath);
            final var length = Files.size(filePath);
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, responseStream);
            responseStream.flush();
        });
        server.startServer();
    }
}


