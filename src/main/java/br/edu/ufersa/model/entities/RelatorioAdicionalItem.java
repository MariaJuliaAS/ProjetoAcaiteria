package br.edu.ufersa.model.entities;

public class RelatorioAdicionalItem {

    private final int id;
    private final String nome;
    private final int quantidadeVendida;
    private final double valorTotal;
    private final int estoqueAtual;

    public RelatorioAdicionalItem(int id, String nome, int quantidadeVendida, double valorTotal, int estoqueAtual) {
        this.id = id;
        this.nome = nome;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotal = valorTotal;
        this.estoqueAtual = estoqueAtual;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }
}
