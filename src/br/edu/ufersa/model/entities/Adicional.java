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
        if(qtdEstoque > 0){
            this.qtdEstoque = qtdEstoque;
        }else{
            System.out.println("Quantidade de estoque não pode ser negativo");
        }
    }

    public static void cadastrarAdicional(Adicional a){
        if (a == null) {
            System.out.println("Adicional inválido");
            return;
        }

        for (Adicional ad: adicionais){
            if(ad.getId() == a.getId()){
                System.out.println("ID já cadastrado");
                return;
            }
        }

        adicionais.add(a);
        System.out.println("Adicional cadastrada com sucesso");
    }

    public static void editarAdicional(Adicional a) {
        for (Adicional ad: adicionais){
            if (ad.getId() == a.getId()) {

                if (a.getNome() == null || a.getNome().isEmpty()) {
                    System.out.println("Nome inválido");
                    return;
                }

                if (a.getPreco() <= 0) {
                    System.out.println("Preço inválido");
                    return;
                }

                if (a.getQtdEstoque() < 0) {
                    System.out.println("Estoque inválido");
                    return;
                }

                ad.setNome(a.getNome());
                ad.setPreco(a.getPreco());
                ad.setQtdEstoque(a.getQtdEstoque());

                System.out.println("Adicional atualizado");
                return;
            }
        }

        System.out.println("Adicional não encontrado");
    }

    public static void excluirAdicional(int id){
        for (Adicional ad: adicionais){
            if(ad.getId() == id){
                adicionais.remove(ad);
                System.out.println("Adicional excluida com sucesso");
                return;
            }
        }
        System.out.println("Adicional não encontrado");
    }

    public static void atualizarEstoque(int id, int qtd){
        for (Adicional ad: adicionais){
            if(ad.getId() == id){
                if (qtd < 0){
                    System.out.println("Quantidade de estoque insuficiente");
                }
                ad.setQtdEstoque(qtd);
                System.out.println("Estoque atualizado");
                return;
            }
        }
        System.out.println("Adicional não encontrado");
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
