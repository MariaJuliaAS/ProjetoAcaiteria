package br.edu.ufersa.model.entities;

public class Funcionario{
	private int id;
	private String nome;
	private String login;
	private String senha;
	private String tipo;

	public Funcionario(int id, String nome, String login, String senha, String tipo){
		this.id = id;
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.tipo = tipo;
	}			
	
	public int getId() {
		return id;
	}

	public String getNome() { 
		return nome; 
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setSenha(String senha){
		this.senha = senha;
	}

	public void setTipo(String tipo){
		this.tipo = tipo;
	}

	public void registrarPedido(Pedido pedido) {
		System.out.println("Pedido registrado pelo funcionario: " + this.nome);
	}	
}
