package br.edu.ufersa.model.interfaces;

public interface CardInterface<T> {
    void setDados(T entidade);
    void setOnAlterado(Runnable onAlterado);
}
