package br.edu.ifba.inf008.interfaces;

import java.util.List;

public interface DatabaseService<T> {
    void create(T entity);
    T read(int id);
    List<T> readAll();
    void update(T entity);
    void delete(int id);
}
