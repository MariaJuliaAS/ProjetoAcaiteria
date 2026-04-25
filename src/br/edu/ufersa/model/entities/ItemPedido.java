package br.edu.ufersa.model.entities;

public class ItemPedido {
    private Produto produto;
    private int quantidade;

    public ItemPedido(){}
    public ItemPedido(Produto p, int qtd){
        setProduto(p);
        setQuantidade(qtd);
    }

    public Produto getProduto() {return produto;}

    public int getQuantidade() {return quantidade;}

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

        for (Adicional ad : produto.getAdicionaisDisponiveis()) {
            total += ad.getPreco();
        }

        total += produto.getPreco() * quantidade;

        return total;
    }

    @Override
    public String toString(){
        return quantidade + "x " + produto.getNome() +
                " - R$ " + calcularValorItem();
    }
}

