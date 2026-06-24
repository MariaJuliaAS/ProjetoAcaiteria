package br.edu.ufersa.model.services;

import br.edu.ufersa.model.DAO.AdicionalDAO;
import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.exceptions.ConflitoDeDadosException;
import br.edu.ufersa.model.exceptions.DadosInvalidosException;
import br.edu.ufersa.model.exceptions.EntidadeNaoEncontradaException;

import java.util.List;

public class AdicionalService {
    private AdicionalDAO adicionalDAO;

    private void initAdicionalDAO() {this.adicionalDAO = new AdicionalDAO();}

    public void cadastrarAdicional(Adicional a){
        System.out.println("[SERVICE] Validando...");
        initAdicionalDAO();

        if (a == null) {throw new DadosInvalidosException("Adicional inválido.");}
        if(a.getNome() == null){throw new DadosInvalidosException("O nome do adicional não pode estar vazio!");}
        if(a.getPreco() < 0){throw new DadosInvalidosException("O preço está inválido!");}
        if(a.getQtdEstoque() < 0){throw new DadosInvalidosException("A quantidade de estoque está inválido!");}

        List<Adicional> adicionaisExistentes = this.adicionalDAO.buscarPorNome(a.getNome());
        if(adicionaisExistentes.size() > 0){throw new ConflitoDeDadosException("Já existe um adicional cadastrado com esse nome.");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO gravar no banco...");
        this.adicionalDAO.cadastrar(a);
    }

    public void editarAdicional(Adicional a){
        System.out.println("[SERVICE] Validando...");
        initAdicionalDAO();

        if (a == null) {throw new DadosInvalidosException("Adicional inválido.");}
        if (a.getId() <= 0) {throw new DadosInvalidosException("ID inválido para edição!");}
        if(a.getNome() == null){throw new DadosInvalidosException("O nome do adicional não pode estar vazio!");}
        if(a.getPreco() < 0){throw new DadosInvalidosException("O preço está inválido!");}
        if(a.getQtdEstoque() < 0){throw new DadosInvalidosException("A quantidade de estoque está inválido!");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO editar no banco...");
        this.adicionalDAO.editar(a);
    }

    public void excluirAdicional(Adicional a){
        System.out.println("[SERVICE] Validando...");

        if (a == null) {throw new DadosInvalidosException("Adicional inválido.");}
        if (a.getId() <= 0) {throw new DadosInvalidosException("ID inválido para exclusão!");}
        initAdicionalDAO();
        Adicional adicionalExistente = this.adicionalDAO.buscarPorId(a);
        if(adicionalExistente == null){throw new EntidadeNaoEncontradaException("Adicional não encontrado para exclusão.");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO deletar do banco...");
        this.adicionalDAO.excluir(a);
    }

    public List<Adicional> buscarPorNome(String nome){
        System.out.println("[SERVICE] Validando...");

        if(nome == null){throw new DadosInvalidosException("Adicional inválido.");}

        initAdicionalDAO();
        List<Adicional> adicionais = this.adicionalDAO.buscarPorNome(nome);
        if(adicionais == null){throw new EntidadeNaoEncontradaException("Adicional não encontrado.");}

        return adicionais;
    }

    public List<Adicional> buscarTodos(){
        System.out.println("[SERVICE] Validando...");
        initAdicionalDAO();
        return this.adicionalDAO.buscarTodos();
    }
}