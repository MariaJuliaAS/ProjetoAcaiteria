package br.edu.ufersa.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Adicional {
    private int id;
    private String nome;
    private double preco;
    private int qtdEstoque;

    public static List<Adicional> adicionais = new ArrayList<>();

    public Adicional(){}
    public Adicional(int id, String nome, double preco, int qtdEstoque) {
        this.id = id;
        setNome(nome);
        setPreco(preco);
        setQtdEstoque(qtdEstoque);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(!nome.isEmpty()){
            this.nome = nome;
        }else{
            System.out.println("Nome não pode ser vazio");
        }
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if(preco > 0){
            this.preco = preco;
        }else{
            System.out.println("Preço não pode ser negativo");
        }
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(int qtdEstoque) {
        if(qtdEstoque >= 0){
            this.qtdEstoque = qtdEstoque;
        }else{
            System.out.println("Quantidade de estoque não pode ser negativo");
        }
    }

    @Override
    public String toString() {
        return "Adicional{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", qtdEstoque=" + qtdEstoque +
                '}';
    }
}
