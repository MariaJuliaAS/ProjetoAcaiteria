package br.edu.ufersa.model.entities;

import br.edu.ufersa.model.DAO.PedidoDAO;
import br.edu.ufersa.model.services.AdicionalService;
import br.edu.ufersa.model.services.ClienteService;
import br.edu.ufersa.model.services.PedidoService;
import br.edu.ufersa.model.services.ProdutoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaAcaiteria {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AdicionalService adicionalService;
    private ProdutoService produtoService;
    private ClienteService clienteService;
    private PedidoService pedidoService;


    public SistemaAcaiteria() {
        this.adicionalService = new AdicionalService();
        this.produtoService = new ProdutoService();
        this.clienteService = new ClienteService();
        this.pedidoService = new PedidoService();
    }

    public List<Cliente> buscarClientesNome(String nome){
        return clienteService.buscarPorNome(nome);
    }

    public List<Pedido> buscarPedido(Cliente cliente){
        return pedidoService.buscarPorCliente(cliente);
    }

    public List<Pedido> buscarPedido(Produto produto){
        return pedidoService.buscarPorProduto(produto);
    }

    public List<Pedido> buscarPedido(LocalDate data){
        return pedidoService.buscarPorData(data);
    }

    public List<Adicional> buscarAdicionais(String nome){
        List<Adicional> resultado = new ArrayList<>();

        if(nome == null || nome.isEmpty()){
            throw  new IllegalArgumentException("Nome não pode ser vazio!");
        }

        for (Adicional ad: adicionalService.buscarPorNome(nome)){
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

        List<Pedido> pedidos = new PedidoDAO().buscarPorPeriodo(inicio, fim);
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
        return pedidoService.gerarRelatorio(inicio,fim);
    }
}
