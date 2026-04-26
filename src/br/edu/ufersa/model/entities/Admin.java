package br.edu.ufersa.model.entities;

public class Admin extends Funcionario {
	
	public Admin() {
		super();
	}

	public Admin(int id, String nome, String login, String senha, String tipo){
		super(id, nome, login, senha, tipo);
	}

	public void cadastrarProduto(Produto produto) {
		System.out.println("Produto: " + produto.getNome() + " cadastrado com sucesso.");
	}

	public void cadastrarProduto(Produto produto) {
		System.out.println("Funcionario " + funcionario.getNome() + " cadastrado com sucesso.");
	}
}
