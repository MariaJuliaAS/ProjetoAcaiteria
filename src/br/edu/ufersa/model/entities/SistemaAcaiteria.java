package br.edu.ufersa.model.entities;

import com.sun.jdi.ClassNotLoadedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaAcaiteria {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<Adicional>  adicionais;
    private List<Produto>  produtos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;


    public SistemaAcaiteria() {
        this.adicionais = Adicional.adicionais;
        this.produtos = Produto.produtos;
        this.clientes = Cliente.getClientes();
        this.pedidos = Pedido.getPedidos();
    }

    public List<Cliente> buscarClientes(String nome){
        List<Cliente> filtrados = new ArrayList<>();

        if(nome == null || nome.isEmpty()){
            System.out.println("Nome não pode ser vazio!");
            return filtrados;
        }

        for (Cliente c : clientes){
            if(c.getNome().toLowerCase().contains(nome.toLowerCase())){
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    public List<Pedido> buscarPedido(Cliente cliente){
        List<Pedido> filtrados = new ArrayList<>();

        if(cliente == null){
            System.out.println("Cliente não pode ser vazio!");
            return filtrados;
        }

        for(Pedido p : pedidos){
            if(p.getCliente().getId() == cliente.getId()){
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public List<Pedido> buscarPedido(Produto produto){
        List<Pedido> filtrados = new ArrayList<>();

        if(produto == null){
            System.out.println("Produto não pode ser vazio!");
            return filtrados;
        }

        for(Pedido p : pedidos){
            for(ItemPedido ip : p.getItensPedido()){
                if(ip.getProduto().getId() == produto.getId()){
                    filtrados.add(p);
                    break;
                }
            }
        }
        return filtrados;
    }

    public List<Pedido> buscarPedido(LocalDate data){
        List<Pedido> filtrados = new ArrayList<>();

        if(data == null){
            System.out.println("Data não pode ser vazio!");
            return filtrados;
        }

        for(Pedido p : pedidos){
            if(p.getData().equals(data)){
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public List<Adicional> buscarAdicionais(String nome){
        List<Adicional> resultado = new ArrayList<>();

        if(nome == null || nome.isEmpty()){
            System.out.println("Nome não pode ser vazio!");
            return resultado;
        }

        for (Adicional ad: adicionais){
            if(ad.getNome().toLowerCase().contains(nome.toLowerCase())){
                resultado.add(ad);
            }
        }
        return resultado;
    }

    public String gerarRelatorioAdicional(LocalDate inicio, LocalDate fim){
        if(inicio == null || fim == null){
            return "Datas inválidas!";
        }

        if(inicio.isAfter(fim)){
            return "Período inválido!";
        }

        List<Pedido> pedidos = new Pedido().getPedidos();
        if(pedidos == null || pedidos.isEmpty()){
            return "Nenhum pedido cadastrado!";
        }

        Map<Integer, Integer> qtdPorAdicional = new HashMap<>();
        Map<Integer, String> nomePorAdicional = new HashMap<>();
        Map<Integer, Double> valorPorAdicional = new HashMap<>();
        Map<Integer, Integer> qtdEmEstoquePorAdicional = new HashMap<>();

        for(Pedido p: pedidos){
            if(p == null || p.getData() == null){
                continue;
            }

            LocalDate dataPedido = p.getData();
            boolean dentroDoPeriodo = !dataPedido.isBefore(inicio) && !dataPedido.isAfter(fim);
            if(!dentroDoPeriodo || p.getItensPedido() == null || p.getItensPedido().isEmpty()){
                continue;
            }

            for (ItemPedido item: p.getItensPedido()){
                if (item == null || item.getProduto() == null || item.getQuantidade() <= 0) {
                    continue;
                }

                List<Adicional> adicionaisDoPedido = item.getAdicionaisEscolhidos();
                if(adicionaisDoPedido == null){
                    continue;
                }

                for(Adicional ad: adicionaisDoPedido){
                    if(ad == null){
                        continue;
                    }

                    int idAdicional = ad.getId();
                    int qtdComprada = item.getQuantidade();
                    int qtdEmEstoque = ad.getQtdEstoque();

                    qtdEmEstoquePorAdicional.put(idAdicional, qtdEmEstoque);
                    nomePorAdicional.put(idAdicional, ad.getNome());
                    qtdPorAdicional.put(idAdicional, (qtdPorAdicional.getOrDefault(idAdicional, 0) + qtdComprada));
                    valorPorAdicional.put(idAdicional, (valorPorAdicional.getOrDefault(idAdicional, 0.0) + ad.getPreco() * qtdComprada));
                }
            }
        }

        if(qtdPorAdicional.isEmpty()){
            return "Nenhum adicional comprado nesse período!";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATORIO DE ADICIONAIS\n");
        relatorio.append("Periodo: ").append(inicio.format(FORMATO_DATA)).append(" ate ").append(fim.format(FORMATO_DATA)).append("\n");
        relatorio.append("--------------------------------\n");

        double totalGeral = 0.0;
        for (Integer id : qtdPorAdicional.keySet()) {
            String nome = nomePorAdicional.get(id);
            int quantidade = qtdPorAdicional.get(id);
            double valor = valorPorAdicional.get(id);
            int estoque = qtdEmEstoquePorAdicional.get(id);
            totalGeral += valor;

            relatorio.append("ID: ").append(id)
                    .append(" | Nome: ").append(nome)
                    .append(" | Quantidade: ").append(quantidade)
                    .append(" | Quantidade em estoque: ").append(estoque)
                    .append(" | Total: R$ ").append(String.format("%.2f", valor))
                    .append("\n");
        }

        relatorio.append("--------------------------------\n");
        relatorio.append("Total geral: R$ ").append(String.format("%.2f", totalGeral));

        return relatorio.toString();

    }

    public String gerarRelatorioPedido(LocalDate inicio, LocalDate fim){
        if(inicio == null || fim == null || inicio.isAfter(fim)){
            return "Intervalo de datas inválido";
        }

        List<Pedido> filtrados = new ArrayList<>();
        double totalFaturado = 0;

        for(Pedido p : pedidos){
            LocalDate data = p.getData();

            if(data != null && !data.isBefore(inicio) && !data.isAfter(fim)){
                filtrados.add(p);

                for(ItemPedido ip : p.getItensPedido()){
                    totalFaturado += ip.calcularValorItem();
                }
            }
        }

        String relatorio = "===== RELATÓRIO =====\n";
        relatorio += "Período: " + inicio.format(FORMATO_DATA) + " até " + fim.format(FORMATO_DATA) + "\n";
        relatorio += "Quantidade de pedidos: " + filtrados.size() + "\n";
        relatorio += "Total faturado: R$ " + String.format("%.2f", totalFaturado) + "\n";
        relatorio += "---------------------\n";

        for(Pedido p : filtrados){
            relatorio += Pedido.gerarNota(p) + "\n";
        }

        return relatorio;
    }
}
