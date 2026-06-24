package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Funcionario;

public interface CardInterface<T> {
    void setDados(T entidade);
    void setOnAlterado(Runnable onAlterado);
}
