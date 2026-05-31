package br.edu.ufersa.model.entities;

import java.util.ArrayList;
import java.util.List;

public class ItemPedido {
    private int id;
    private Produto produto;
    private int quantidade;
    private List<Adicional> adicionaisEscolhidos;

    public ItemPedido(){adicionaisEscolhidos = new ArrayList<>();}
    public ItemPedido(int id, Produto p, int qtd){
        this.id = id;
        setProduto(p);
        setQuantidade(qtd);
        adicionaisEscolhidos = new ArrayList<>();
    }

    public int getId() {return id;}

    public Produto getProduto() {return produto;}

    public int getQuantidade() {return quantidade;}

    public List<Adicional> getAdicionaisEscolhidos() {return adicionaisEscolhidos;}

    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(Produto p) {
        if(p == null){
            System.out.println("Produto inválido");
            return;
        }

        this.produto = p;
    }

    public void setQuantidade(int qtd) {
        if (qtd <= 0){
            System.out.println("Quantidade deve ser maior que 0");
            return;
        }

        this.quantidade = qtd;
    }

    public void setAdicionaisEscolhidos(List<Adicional> adicionaisEscolhidos) {
        this.adicionaisEscolhidos = adicionaisEscolhidos;
    }

    @Override
    public String toString(){
        return quantidade + "x " + produto.getNome();
    }
}

