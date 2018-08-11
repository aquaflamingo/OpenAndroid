package com.robertsimoes.conscious.utility.interfaces;

public interface IOable<T> {
    void write(String fileName,T saveObject);
    T read(String fileName);
    boolean destroy(String fileName);
}
