package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    public void initialize() {
        sidebarController.destacar(sidebarController.getBtnDashboard());

        lblSaudacao.setText("Olá, " + MainApp.getFuncionarioLogado().getNome());

        carregarResumoDoDia();
    }

    private void carregarResumoDoDia() {
        List<Pedido> pedidosHoje = sistema.buscarPedidoPorData(LocalDate.now());

        int qtdPedidos = (pedidosHoje == null) ? 0 : pedidosHoje.size();
        double faturamento = sistema.calcularFaturamento(pedidosHoje);

        int qtdClientes = sistema.buscarClientesTodos().size();
        int qtdProdutos = sistema.buscarProdutoTodos().size();

        lblQtdPedidos.setText(String.valueOf(qtdPedidos));
        lblFaturamento.setText(String.format("R$ %.2f", faturamento));
        lblQtdClientes.setText(String.valueOf(qtdClientes));
        lblQtdProdutos.setText(String.valueOf(qtdProdutos));
    }

    @FXML
    private void irParaNovoPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/modal-novo-pedido.fxml"));
            Parent root = loader.load();

            ModalNovoPedidoController controller = loader.getController();
            controller.setOnSalvar(this::carregarResumoDoDia);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Pedido");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setMaximized(true);
            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaBuscarPedido() {
        MainApp.telaPedidos();
    }

    @FXML
    private void irParaVerEstoque() {
        MainApp.telaAdicionais();
    }

    @FXML
    private void irParaNovoCliente() {
        MainApp.telaClientes();
    }
}
