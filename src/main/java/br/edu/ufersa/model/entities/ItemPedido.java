package br.edu.ufersa.model.entities;

import java.util.ArrayList;
import java.util.List;

public class ItemPedido {
    private int id;
    private Produto produto;
    private int quantidade;
    private List<Adicional> adicionaisEscolhidos;

    private ItemPedido() {
        adicionaisEscolhidos = new ArrayList<>();
    }

    public int getId() { return id; }
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public List<Adicional> getAdicionaisEscolhidos() { return adicionaisEscolhidos; }

    public void setId(int id) { this.id = id; }

    public void setProduto(Produto p) {
        if (p == null) {
            System.out.println("Produto inválido");
            return;
        }
        this.produto = p;
    }

    public void setQuantidade(int qtd) {
        if (qtd <= 0) {
            System.out.println("Quantidade deve ser maior que 0");
            return;
        }
        this.quantidade = qtd;
    }

    public void setAdicionaisEscolhidos(List<Adicional> adicionaisEscolhidos) {
        this.adicionaisEscolhidos = adicionaisEscolhidos;
    }

    @Override
    public String toString() {
        return quantidade + "x " + produto.getNome();
    }

    public static class Builder {
        private int id;
        private Produto produto;
        private int quantidade;
        private List<Adicional> adicionaisEscolhidos = new ArrayList<>();

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public Builder quantidade(int quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public Builder adicionaisEscolhidos(List<Adicional> adicionaisEscolhidos) {
            this.adicionaisEscolhidos = adicionaisEscolhidos;
            return this;
        }

        public ItemPedido build() {
            ItemPedido item = new ItemPedido();
            item.setId(id);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setAdicionaisEscolhidos(adicionaisEscolhidos);
            return item;
        }
    }
}