package br.edu.ufersa.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private LocalDate data;
    private Cliente cliente;
    private List<ItemPedido> itensPedido;
    private String formaPagamento;

    private static List<Pedido> pedidos = new ArrayList<>();

    private Pedido() {
        this.itensPedido = new ArrayList<>();
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public LocalDate getData() { return data; }
    public List<ItemPedido> getItensPedido() { return itensPedido; }
    public String getFormaPagamento() { return formaPagamento; }
    public static List<Pedido> getPedidos() { return pedidos; }

    public void setId(int id) { this.id = id; }
    public void setData(LocalDate data) { this.data = data; }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("Cliente inválido");
            return;
        }
        this.cliente = cliente;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        if (itensPedido == null) {
            System.out.println("Pedidos precisam de no mínimo 1 item");
            return;
        }
        this.itensPedido = itensPedido;
    }

    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.isEmpty()) {
            System.out.println("Forma de pagamento inválido");
            return;
        }
        this.formaPagamento = formaPagamento;
    }

    public static class Builder {
        private int id;
        private LocalDate data;
        private Cliente cliente;
        private List<ItemPedido> itensPedido = new ArrayList<>();
        private String formaPagamento;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder data(LocalDate data) {
            this.data = data;
            return this;
        }

        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder itensPedido(List<ItemPedido> itensPedido) {
            this.itensPedido = itensPedido;
            return this;
        }

        public Builder formaPagamento(String formaPagamento) {
            this.formaPagamento = formaPagamento;
            return this;
        }

        public Pedido build() {
            Pedido pedido = new Pedido();
            pedido.setId(id);
            pedido.setData(data);
            pedido.setCliente(cliente);
            pedido.setItensPedido(itensPedido);
            pedido.setFormaPagamento(formaPagamento);
            return pedido;
        }
    }
}