package br.edu.ufersa.model.entities;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private List<Adicional> adicionaisDisponiveis;

    public static List<Produto> produtos = new ArrayList<>();

    public Produto() {
        this.adicionaisDisponiveis = new ArrayList<>();
    }
    public Produto(int id, String nome, double preco) {
        this.id = id;
        setNome(nome);
        setPreco(preco);
        this.adicionaisDisponiveis = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

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

    public List<Adicional> getAdicionaisDisponiveis() {
        return adicionaisDisponiveis;
    }

    public void setAdicionaisDisponiveis(List<Adicional> adicionaisDisponiveis) {
        this.adicionaisDisponiveis = adicionaisDisponiveis;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", adicionais=" + adicionaisDisponiveis.size() +
                '}';
    }
}
