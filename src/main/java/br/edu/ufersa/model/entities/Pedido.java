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

    public Pedido(){this.itensPedido = new ArrayList<>();}

    public Pedido(int id,LocalDate data,Cliente cliente, String formaPagamento){
        setId(id);
        setData(data);
        setCliente(cliente);
        this.itensPedido = new ArrayList<>();
        setFormaPagamento(formaPagamento);
    }

    public int getId() {return id;}

    public Cliente getCliente() {return cliente;}

    public LocalDate getData() {return data;}

    public List<ItemPedido> getItensPedido() {return itensPedido;}

    public String getFormaPagamento() {return formaPagamento;}

    public static List<Pedido> getPedidos() {return pedidos;}

    public void setId(int id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null){
            System.out.println("Cliente inválido");
            return;
        }
        this.cliente = cliente;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        if(itensPedido == null){
            System.out.println("Pedidos precisam de no mínimo 1 item");
            return;
        }
        this.itensPedido = itensPedido;
    }

    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.isEmpty()){
            System.out.println("Forma de pagamento inválido");
            return;
        }
        this.formaPagamento = formaPagamento;
    }
}
