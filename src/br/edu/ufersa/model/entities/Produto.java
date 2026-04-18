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

    public static void cadastrarProduto(Produto p){
        if(p == null){
            System.out.println("Produto inválido");
            return;
        }

        for(Produto pd: produtos){
            if(pd.getId() == p.getId()){
                System.out.println("Produto existente");
                return;
            }
        }

        produtos.add(p);
        System.out.println("Produto cadastrado com sucesso");
    }

    public static void editarProduto(Produto p){
        for(Produto pd: produtos){
            if(pd.getId() == p.getId()){

                if (p.getNome() == null || p.getNome().isEmpty()) {
                    System.out.println("Nome inválido");
                    return;
                }

                if(p.getPreco() <= 0){
                    System.out.println("Preço inválido");
                    return;
                }

                pd.setNome(p.getNome());
                pd.setPreco(p.getPreco());

                System.out.println("Produto atualizado");

            }
        }
    }

    public static void excluirProduto(int id){
        for (Produto pd: produtos){
            if(pd.getId() == id){
                produtos.remove(pd);
                System.out.println("Produto removido com sucesso");
                return;
            }
        }
    }

    public void adicionarAdicional(Adicional a) {
        if(a == null){
            System.out.println("Adicional inválido");
            return;
        }

        for (Adicional ad: adicionaisDisponiveis){
            if (ad.getId() == a.getId()) {
                System.out.println("Adicional já adicionado");
                return;
            }
        }

        if (a.getQtdEstoque() <= 0) {
            System.out.println("Adicional sem estoque");
            return;
        }

        adicionaisDisponiveis.add(a);
        System.out.println("Adicional adicionado ao produto");
    }

    public void removerAdicional(int id){
        for (Adicional ad: adicionaisDisponiveis){
            if(ad.getId() == id){
                adicionaisDisponiveis.remove(ad);
                System.out.println("Adicional removido com sucesso");
                return;
            }
        }
        System.out.println("Adicional não encontrado no produto");
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
