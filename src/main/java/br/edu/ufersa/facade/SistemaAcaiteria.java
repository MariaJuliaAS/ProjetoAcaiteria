package br.edu.ufersa.facade;

import br.edu.ufersa.model.DAO.PedidoDAO;
import br.edu.ufersa.model.entities.*;
import br.edu.ufersa.model.services.*;

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
    private FuncionarioService funcionarioService;

    public SistemaAcaiteria() {
        this.adicionalService = new AdicionalService();
        this.produtoService = new ProdutoService();
        this.clienteService = new ClienteService();
        this.pedidoService = new PedidoService();
        this.funcionarioService = new FuncionarioService();
    }

    public void cadastrarCliente(Cliente cliente){
        clienteService.cadastrar(cliente);
    }

    public void cadastrarProduto(Produto produto){
        produtoService.cadastrarProduto(produto);
    }

    public void cadastrarAdicional(Adicional adicional){
        adicionalService.cadastrarAdicional(adicional);
    }

    public void cadastrarPedido(Pedido pedido){
        pedidoService.cadastrar(pedido);
    }

    public void editarCliente(Cliente cliente) {
        clienteService.editar(cliente);
    }

    public void excluirCliente(Cliente c) {
        clienteService.excluir(c);
    }

    public void editarProduto(Produto produto) {
        produtoService.editarProduto(produto);
    }

    public void excluirProduto(Produto p) {
        produtoService.excluirProduto(p);
    }

    public void editarAdicional(Adicional adicional) {
        adicionalService.editarAdicional(adicional);
    }

    public void excluirAdicional(Adicional adicional) {
        adicionalService.excluirAdicional(adicional);
    }

    public void excluirPedido(Pedido pedido) {
        pedidoService.excluir(pedido);
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        funcionarioService.cadastrarFuncionario(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario) {
        funcionarioService.editarFuncionario(funcionario);
    }

    public void excluirFuncionario(Funcionario funcionario) {
        funcionarioService.excluirFuncionario(funcionario);
    }

    public Funcionario autenticar(String login, String senha){
        return funcionarioService.autenticar(login, senha);
    }

    public  List<Funcionario> buscarFuncionariosTodos(){
        return funcionarioService.buscarTodos();
    }

    public List<Cliente> buscarClientesTodos(){
        return clienteService.buscarTodos();
    }

    public List<Cliente> buscarClientesPorNome(String nome){
        return clienteService.buscarPorNome(nome);
    }

    public List<Pedido> buscarPedidosTodos(){
        return pedidoService.buscarTodos();
    }

    public List<Pedido> buscarPedidoPorCliente(Cliente cliente){
        return pedidoService.buscarPorCliente(cliente);
    }

    public List<Pedido> buscarPedidoPorProduto(Produto produto){
        return pedidoService.buscarPorProduto(produto);
    }

    public List<Pedido> buscarPedidoPorData(LocalDate data){
        return pedidoService.buscarPorData(data);
    }

    public List<Pedido> buscarPedidoPorPeriodo(LocalDate inicio, LocalDate fim){
        return pedidoService.buscarPorPeriodo(inicio, fim);
    }

    public List<Adicional> buscarAdicionaisPorNome(String nome) {
        return adicionalService.buscarPorNome(nome);
    }

    public String gerarRelatorioPedido(LocalDate inicio, LocalDate fim){
        return pedidoService.gerarRelatorio(inicio,fim);
    }

    public List<Produto> buscarProdutoTodos(){
        return produtoService.buscarTodos();
    }

    public List<Adicional> buscarAdicionaisTodos(){
        return adicionalService.buscarTodos();
    }

    public double calcularTotalPedidos(Pedido pedido){
        return pedidoService.calcularTotal(pedido);
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

    public String gerarNotaPedido(Pedido pedido){
        return pedidoService.gerarNota(pedido);
    }
}
