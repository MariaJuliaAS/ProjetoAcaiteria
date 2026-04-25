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

    public Pedido(int id,Cliente cliente, String formaPagamento){
        this.id = id;
        this.data = LocalDate.now();
        setCliente(cliente);
        this.itensPedido = new ArrayList<>();
        setFormaPagamento(formaPagamento);
    }

    public int getId() {return id;}

    public Cliente getCliente() {return cliente;}

    public LocalDate getData() {return data;}

    public List<ItemPedido> getItensPedido() {return itensPedido;}

    public String getFormaPagamento() {return formaPagamento;}

    public List<Pedido> getPedidos() {return pedidos;}

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

    public static void cadastrarPedido(Pedido p){
        if(p.getCliente() == null) {
            System.out.println("Cliente inválido");
            return;
        }
        if(p.getFormaPagamento() == null || p.getFormaPagamento().isEmpty()) {
            System.out.println("Forma de pagamento inválida");
            return;
        }

        if(p.getItensPedido() == null){
            System.out.println("Nenhum item selecionado");
            return;
        }

        for(Pedido ped : pedidos){
            if (ped.getId() == p.getId()){
                System.out.println("ID já cadastrado");
                return;
            }
        }
        pedidos.add(p);
        System.out.println("Pedido cadastrado");
    }

    public static void editarPedido(Pedido p){
        for(Pedido ped : pedidos){
            if(ped.getId() == p.getId()){
                ped.setCliente(p.cliente);
                ped.setItensPedido(p.itensPedido);
                ped.setFormaPagamento(p.formaPagamento);
                System.out.println("Pedido atualizado");
                return;
            }
        }
        System.out.println("Pedido não encontrado");
    }

    public static void excluirPedido(int id){
        for(Pedido ped : pedidos){
            if(ped.getId() == id){
                pedidos.remove(ped);
                System.out.println("Pedido excluído");
                return;
            }
        }
        System.out.println("Pedido não encontrado");
    }

    public void addItem(ItemPedido ip){
        itensPedido.add(ip);
    }

    public static String gerarNota(Pedido p) {
        if (p == null) return "Pedido inválido";

        if (p.getCliente() == null) return "Cliente inválido";

        if (p.getItensPedido() == null || p.getItensPedido().isEmpty()) {
            return "Pedido sem itens";
        }

        double total = 0;

        String nota = "===== NOTA =====\n";
        nota += "Cliente: " + p.getCliente().getNome() + "\n";
        nota += "Data: " + p.getData() + "\n";
        nota += "Pagamento: " + p.getFormaPagamento() + "\n";
        nota += "-----------------\n";

        for (ItemPedido ip : p.getItensPedido()) {
            nota += ip + "\n";
            total += ip.calcularValorItem();
        }

        nota += "-----------------\n";
        nota += "TOTAL: " + total + "\n";

        return nota;
    }
}
