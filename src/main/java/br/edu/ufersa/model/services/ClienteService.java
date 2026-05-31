package br.edu.ufersa.model.services;

import br.edu.ufersa.model.DAO.ClienteDAO;
import br.edu.ufersa.model.entities.Cliente;

import java.sql.ResultSet;
import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public void cadastrar(Cliente c) {

        validarCliente(c);

        clienteDAO.cadastrar(c);
    }

    public void editar(Cliente c) {
        if (c.getId() <= 0) {
            throw new RuntimeException("Cliente sem ID para edição");
        }

        validarCliente(c);

        clienteDAO.editar(c);
    }

    public void excluir(Cliente c) {
        if (c == null) {
            throw new RuntimeException("Cliente inválido para exclusão");}

        if (c.getId() <= 0) {
            throw new RuntimeException("Cliente sem ID para exclusão");
        }

        clienteDAO.excluir(c);
    }

    private void validarCliente(Cliente c) {

        if (c == null) {
            throw new RuntimeException("Cliente não pode ser nulo");
        }

        if (c.getNome() == null || c.getNome().isEmpty()) {
            throw new RuntimeException("Nome do cliente é obrigatório");
        }

        if (c.getTelefone() == null || c.getTelefone().isEmpty()) {
            throw new RuntimeException("Telefone é obrigatório");
        }

        if(c.getEndereco() == null || c.getEndereco().isEmpty()){
            throw new RuntimeException("Endereço é obrigatório");
        }
    }

    public List<Cliente> buscarPorNome(String nome) {

        if (nome == null || nome.isEmpty()) {
            throw new RuntimeException("Nome inválido para busca");
        }

        return clienteDAO.buscarPorNome(nome);

    }
    public List<Cliente> buscarTodos() {
        return clienteDAO.buscarTodos();
    }
}
