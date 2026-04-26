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

    public double calcularValorItem() {
        double total = 0;

        for (Adicional ad : adicionaisEscolhidos) {
            total += ad.getPreco();
        }

        total += produto.getPreco() * quantidade;

        return total;
    }

    public void escolherAdicional(Adicional a) {
        if(a == null){
            System.out.println("Adicional inválido");
            return;
        }

        if (!produto.getAdicionaisDisponiveis().contains(a)) {
            System.out.println("Adicional não pertence a este produto");
            return;
        }

        for (Adicional ad: adicionaisEscolhidos){
            if (ad.getId() == a.getId()) {
                System.out.println("Adicional já escolhido");
                return;
            }
        }

        if (a.getQtdEstoque() <= 0) {
            System.out.println("Adicional sem estoque");
            return;
        }

        adicionaisEscolhidos.add(a);
        System.out.println("Adicional adicionado ao item");
    }

    public void removerAdicional(int id){
        for (Adicional ad: adicionaisEscolhidos){
            if(ad.getId() == id){
                adicionaisEscolhidos.remove(ad);
                System.out.println("Adicional removido com sucesso");
                return;
            }
        }
        System.out.println("Adicional não encontrado no item");
    }

    @Override
    public String toString(){
        return quantidade + "x " + produto.getNome() +
                " - R$ " + calcularValorItem();
    }
}

