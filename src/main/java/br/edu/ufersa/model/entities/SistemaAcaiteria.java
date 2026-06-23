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

    public String gerarRelatorioPedido(LocalDate inicio, LocalDate fim){
        return pedidoService.gerarRelatorio(inicio,fim);
    }

    public double calcularFaturamento(List<Pedido> pedidos){
        if (pedidos == null || pedidos.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Pedido p : pedidos) {
            if (p == null || p.getItensPedido() == null) {
                continue;
            }

            for (ItemPedido item : p.getItensPedido()) {
                if (item == null || item.getProduto() == null || item.getQuantidade() <= 0) {
                    continue;
                }

                total += item.getProduto().getPreco() * item.getQuantidade();

                if (item.getAdicionaisEscolhidos() != null) {
                    for (Adicional ad : item.getAdicionaisEscolhidos()) {
                        if (ad != null) {
                            total += ad.getPreco() * item.getQuantidade();
                        }
                    }
                }
            }
        }

        return total;
    }

    public List<RelatorioAdicionalItem> gerarRelatorioAdicionalLista(LocalDate inicio, LocalDate fim){
        List<RelatorioAdicionalItem> resultado = new ArrayList<>();

        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            return resultado;
        }

        List<Pedido> pedidos = new PedidoDAO().buscarPorPeriodo(inicio, fim);
        if (pedidos == null || pedidos.isEmpty()) {
            return resultado;
        }

        Map<Integer, Integer> qtdPorAdicional = new HashMap<>();
        Map<Integer, String> nomePorAdicional = new HashMap<>();
        Map<Integer, Double> valorPorAdicional = new HashMap<>();
        Map<Integer, Integer> qtdEmEstoquePorAdicional = new HashMap<>();

        for (Pedido p : pedidos) {
            if (p == null || p.getData() == null) {
                continue;
            }

            LocalDate dataPedido = p.getData();
            boolean dentroDoPeriodo = !dataPedido.isBefore(inicio) && !dataPedido.isAfter(fim);
            if (!dentroDoPeriodo || p.getItensPedido() == null || p.getItensPedido().isEmpty()) {
                continue;
            }

            for (ItemPedido item : p.getItensPedido()) {
                if (item == null || item.getProduto() == null || item.getQuantidade() <= 0) {
                    continue;
                }

                List<Adicional> adicionaisDoPedido = item.getAdicionaisEscolhidos();
                if (adicionaisDoPedido == null) {
                    continue;
                }

                for (Adicional ad : adicionaisDoPedido) {
                    if (ad == null) {
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

        for (Integer id : qtdPorAdicional.keySet()) {
            resultado.add(new RelatorioAdicionalItem(
                    id,
                    nomePorAdicional.get(id),
                    qtdPorAdicional.get(id),
                    valorPorAdicional.get(id),
                    qtdEmEstoquePorAdicional.get(id)
            ));
        }

        return resultado;
    }
}
