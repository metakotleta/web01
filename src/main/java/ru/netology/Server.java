package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server {
    private static final int PORT = 9999;
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css",
            "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js", "/messages");
    ExecutorService pool = Executors.newFixedThreadPool(64);
    private ConcurrentHashMap<String, ConcurrentHashMap<String, IHandler>> handlers = new ConcurrentHashMap();

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            var socket = serverSocket.accept();
            if (socket.isBound()) {
                pool.submit(() -> process(socket));
            }
        }
    }

    private void process(Socket socket) {
        while (!socket.isClosed()) {
            try (
                    final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final var out = new BufferedOutputStream(socket.getOutputStream())
            ) {
                if (in.ready()) {
                    String requestLines = parse(in);
                    String bodyLines = getBody(requestLines);

                    if (!requestLines.isEmpty()) {
                        final var parts = requestLines.lines().findFirst().get().split(" ");
                        if (parts.length != 3) {
                            // just close socket
                            continue;
                        }
                        var method = parts[0];
                        var path = parts[1];
                        Request req = new Request(method, path);
                        if (!validPaths.contains(path)) {
                            out.write((
                                    "HTTP/1.1 404 Not Found\r\n" +
                                            "Content-Length: 0\r\n" +
                                            "Connection: close\r\n" +
                                            "\r\n"
                            ).getBytes());
                            out.flush();
                            continue;
                        }
                        if (!bodyLines.isEmpty()) {
                            req.setBody(bodyLines);
                        }

                        handlers.get(method).get(path).handle(req, out);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addHandler(String get, String s, IHandler handler) {
        ConcurrentHashMap<String, IHandler> map = new ConcurrentHashMap<>();
        map.put(s, handler);
        handlers.put(get, map);
    }

    private String parse(BufferedReader in) throws IOException {
        CharBuffer reqBuffer = CharBuffer.allocate(128);
        int count = in.read(reqBuffer);
        if (count != -1) {
            reqBuffer.clear();
            return new String(reqBuffer.array(), 0, count);
        }
        reqBuffer.clear();
        return null;
    }

    private String getBody(String requestLines) {
        List<String> reqLines = requestLines.lines().collect(Collectors.toList());
        for (int i = 0; i < reqLines.size(); i++) {
            if (reqLines.get(i).isEmpty()) {
                return reqLines.get(i + 1);
            }
        }
        return null;
    }
}



