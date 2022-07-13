package ru.netology;

import java.io.BufferedOutputStream;

@FunctionalInterface
public interface IHandler {
    public void handle(Request request, BufferedOutputStream stream);
}
