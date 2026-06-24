package br.edu.ufersa.model.services;

import br.edu.ufersa.model.DAO.ProdutoDAO;
import br.edu.ufersa.model.entities.Produto;
import br.edu.ufersa.model.exceptions.DadosInvalidosException;
import br.edu.ufersa.model.exceptions.EntidadeNaoEncontradaException;

import java.util.List;

public class ProdutoService {
    private ProdutoDAO produtoDAO;

    private void initProdutoDAO() {this.produtoDAO = new ProdutoDAO();}

    public void cadastrarProduto(Produto p){
        System.out.println("[SERVICE] Validando...");
        initProdutoDAO();

        if (p == null) {throw new DadosInvalidosException("Adicional inválido.");}
        if(p.getNome() == null){throw new DadosInvalidosException("Regra de Negócio Violada: O nome do produto não pode estar vazio!");}
        if(p.getPreco() < 0){throw new DadosInvalidosException("Regra de Negócio Violada: O preço está inválido!");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO gravar no banco...");
        this.produtoDAO.cadastrar(p);
    }

    public void editarProduto(Produto p){
        System.out.println("[SERVICE] Validando...");
        initProdutoDAO();

        if (p == null) {throw new DadosInvalidosException("Produto inválido.");}
        if (p.getId() <= 0) {throw new DadosInvalidosException("ID inválido para edição!");}
        if(p.getNome() == null){throw new DadosInvalidosException("Regra de Negócio Violada: O nome do produto não pode estar vazio!");}
        if(p.getPreco() < 0){throw new DadosInvalidosException("Regra de Negócio Violada: O preço está inválido!");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO editar no banco...");
        this.produtoDAO.editar(p);
    }

    public void excluirProduto(Produto p){
        System.out.println("[SERVICE] Validando...");

        if (p == null) {throw new DadosInvalidosException("Produto inválido.");}
        if (p.getId() <= 0) {throw new DadosInvalidosException("ID inválido para exclusão!");}
        initProdutoDAO();
        Produto produtoExistente = this.produtoDAO.buscarPorId(p);
        if(produtoExistente == null){throw new EntidadeNaoEncontradaException("Produto não encontrado para exclusão.");}

        System.out.println("[SERVICE] Tudo certo! Encaminhando para o DAO deletar do banco...");
        this.produtoDAO.excluir(p);
    }

    public Produto buscarPorId(Produto p){
        System.out.println("[SERVICE] Validando...");

        if(p.getId() < 0){throw new DadosInvalidosException("ID inválido para produto.");}

        initProdutoDAO();
        Produto produtoExistente = this.produtoDAO.buscarPorId(p);
        if(produtoExistente == null){throw new EntidadeNaoEncontradaException("Produto não encontrado.");}

        return produtoExistente;
    }

    public List<Produto> buscarTodos(){
        System.out.println("[SERVICE] Validando...");
        initProdutoDAO();
        return this.produtoDAO.buscarTodos();
    }

    private void salvarAdicionais(Produto p){

    }
}