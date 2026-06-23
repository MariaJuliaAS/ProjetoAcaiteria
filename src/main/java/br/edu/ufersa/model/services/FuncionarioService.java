package br.edu.ufersa.model.services;

import br.edu.ufersa.model.DAO.FuncionarioDAO;
import br.edu.ufersa.model.entities.Funcionario;

import java.util.List;

public class FuncionarioService {
    private FuncionarioDAO funcionarioDAO;

    private void initFuncionarioDAO() {this.funcionarioDAO = new FuncionarioDAO();}

    public void cadastrarFuncionario(Funcionario f){
        System.out.println("[SERVICE] Validando...");
        initFuncionarioDAO();

        validarCampos(f);

        Funcionario existente = this.funcionarioDAO.buscarPorLogin(f.getLogin());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um funcionário cadastrado com esse usuário.");
        }

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO gravar no banco...");
        this.funcionarioDAO.cadastrar(f);
    }

    public void editarFuncionario(Funcionario f){
        System.out.println("[SERVICE] Validando...");
        initFuncionarioDAO();

        if (f.getId() <= 0) {
            throw new IllegalArgumentException("ID inválido para edição!");
        }
        validarCampos(f);

        Funcionario outroComMesmoLogin = this.funcionarioDAO.buscarPorLogin(f.getLogin());
        if (outroComMesmoLogin != null && outroComMesmoLogin.getId() != f.getId()) {
            throw new IllegalArgumentException("Já existe outro funcionário cadastrado com esse usuário.");
        }

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO editar no banco...");
        this.funcionarioDAO.editar(f);
    }

    public void excluirFuncionario(Funcionario f){
        System.out.println("[SERVICE] Validando...");

        if (f == null) {throw new IllegalArgumentException("Funcionário inválido.");}
        if (f.getId() <= 0) {throw new IllegalArgumentException("ID inválido para exclusão!");}

        initFuncionarioDAO();
        Funcionario existente = this.funcionarioDAO.buscarPorId(f);
        if (existente == null) {throw new IllegalArgumentException("Funcionário não encontrado para exclusão.");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO deletar do banco...");
        this.funcionarioDAO.excluir(f);
    }

    public List<Funcionario> buscarTodos(){
        System.out.println("[SERVICE] Validando...");
        initFuncionarioDAO();
        return this.funcionarioDAO.buscarTodos();
    }

    public Funcionario autenticar(String login, String senha){
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            return null;
        }

        initFuncionarioDAO();
        Funcionario encontrado = this.funcionarioDAO.buscarPorLogin(login.trim());

        if (encontrado == null) {
            return null;
        }

        if (!encontrado.getSenha().equals(senha)) {
            return null;
        }

        return encontrado;
    }

    private void validarCampos(Funcionario f){
        if (f == null) {throw new IllegalArgumentException("Funcionário inválido.");}
        if (f.getNome() == null || f.getNome().isBlank()) {throw new IllegalArgumentException("Regra de Negócio Violada: O nome não pode estar vazio!");}
        if (f.getLogin() == null || f.getLogin().isBlank()) {throw new IllegalArgumentException("Regra de Negócio Violada: O usuário não pode estar vazio!");}
        if (f.getSenha() == null || f.getSenha().isBlank()) {throw new IllegalArgumentException("Regra de Negócio Violada: A senha não pode estar vazia!");}
        if (f.getTipo() == null || f.getTipo().isBlank()) {throw new IllegalArgumentException("Regra de Negócio Violada: O cargo não pode estar vazio!");}
    }
}
