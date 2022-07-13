package ru.netology;

import java.awt.datatransfer.MimeTypeParseException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) throws IOException {

    Server server = new Server();

    // добавление handler'ов (обработчиков)
    //просто отдаст 200
    server.addHandler("GET", "/messages", (request, responseStream) -> {
      try {
        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.write("OK".getBytes());
        responseStream.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }

    });
    //вернет body запроса
    server.addHandler("POST", "/messages", (request, responseStream) -> {
        try {
          var content = request.getBody();
          responseStream.write((
                  "HTTP/1.1 200 OK\r\n" +
                          "Content-Length: " + content.length() + "\r\n" +
                          "Connection: close\r\n" +
                          "\r\n"
          ).getBytes());
          responseStream.write(content.getBytes());
          responseStream.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
    });
    server.startServer();
  }
}


