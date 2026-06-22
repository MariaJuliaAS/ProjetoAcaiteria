package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.model.entities.SistemaAcaiteria;
import br.edu.ufersa.model.services.ClienteService;
import br.edu.ufersa.model.services.ProdutoService;
import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML private SidebarController sidebarController;

    @FXML private Label lblSaudacao;
    @FXML private Label lblQtdPedidos;
    @FXML private Label lblFaturamento;
    @FXML private Label lblQtdClientes;
    @FXML private Label lblQtdProdutos;

    @FXML private Button btnNovoPedido;
    @FXML private Button btnNovoCliente;
    @FXML private Button btnBuscarPedido;
    @FXML private Button btnVerEstoque;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();
    private final ProdutoService produtoService = new ProdutoService();
    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        sidebarController.destacar(sidebarController.getBtnDashboard());

        lblSaudacao.setText("Olá, Ju Maromba!");

        carregarResumoDoDia();
    }

    private void carregarResumoDoDia() {
        List<Pedido> pedidosHoje = sistema.buscarPedido(LocalDate.now());

        int qtdPedidos = (pedidosHoje == null) ? 0 : pedidosHoje.size();
        double faturamento = sistema.calcularFaturamento(pedidosHoje);

        int qtdClientes = clienteService.buscarTodos().size();
        int qtdProdutos = produtoService.buscarTodos().size();

        lblQtdPedidos.setText(String.valueOf(qtdPedidos));
        lblFaturamento.setText(String.format("R$ %.2f", faturamento));
        lblQtdClientes.setText(String.valueOf(qtdClientes));
        lblQtdProdutos.setText(String.valueOf(qtdProdutos));
    }

    @FXML
    private void irParaNovoPedido() {
        MainApp.telaPedidos();
    }

    @FXML
    private void irParaBuscarPedido() {
        MainApp.telaPedidos();
    }

    @FXML
    private void irParaVerEstoque() {
        MainApp.telaAdicionais();
    }
}
