package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {

    Server server = new Server();

    // добавление handler'ов (обработчиков)
    server.addHandler("GET", "/messages", (request, responseStream) -> {
      try {
        responseStream.write((
                "HTTP/1.1 502 Sosi Pesos\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }

    });
    server.addHandler("POST", "/messages", new Handler() {
      public void handle(Request request, BufferedOutputStream responseStream) {
        // TODO: handlers code
      }
    });
    server.startServer();
  }
}


