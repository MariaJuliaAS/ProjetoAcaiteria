package br.edu.ufersa.model.interfaces;

import java.util.List;

public interface DAOInterface<T> {

    void cadastrar(T entidade);

    void editar(T entidade);

    void excluir(T entidade);

    List<T> buscarTodos();
}
