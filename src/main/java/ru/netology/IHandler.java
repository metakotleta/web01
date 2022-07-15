package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;

@FunctionalInterface
public interface IHandler {
    public void handle(Request request, BufferedOutputStream stream) throws IOException;
}
