package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.model.entities.RelatorioAdicionalItem;
import br.edu.ufersa.facade.SistemaAcaiteria;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class RelatoriosController {

    @FXML private SidebarController sidebarController;

    @FXML private Button btnDia;
    @FXML private Button btnSemana;
    @FXML private Button btnMes;
    @FXML private TextField txtDataReferencia;
    @FXML private TableView<RelatorioAdicionalItem> tabelaAdicionais;
    @FXML private TableColumn<RelatorioAdicionalItem, String> colNome;
    @FXML private TableColumn<RelatorioAdicionalItem, Integer> colQtd;
    @FXML private TableColumn<RelatorioAdicionalItem, Double> colValor;
    @FXML private TableColumn<RelatorioAdicionalItem, Integer> colEstoque;
    @FXML private Label lblTotal;
    @FXML private TableView<Pedido> tabelaPedidos;
    @FXML private TableColumn<Pedido, String> colData;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colPagamento;
    @FXML private TableColumn<Pedido, String> colTotal;
    @FXML private Label lblTotalPedidos;
    @FXML private Label lblQtdPedidos;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private String periodoAtual = "DIA";
    private LocalDate dataReferencia = LocalDate.now();

    private static final String ESTILO_BOTAO_ATIVO =
            "-fx-background-color: #6b2d7a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;";
    private static final String ESTILO_BOTAO_INATIVO =
            "-fx-background-color: white; -fx-text-fill: #444444; -fx-border-color: #d8d0db; -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        sidebarController.destacar(sidebarController.getBtnRelatorios());
        configurarColunasPedidos();
        configurarColunasAdicionais();
        txtDataReferencia.setText(dataReferencia.format(FORMATO_DATA));
        atualizarRelatorio();
    }

    private void configurarColunasPedidos() {
        colData.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getData().format(FORMATO_DATA)));

        colCliente.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCliente() != null
                        ? cell.getValue().getCliente().getNome() : "—"));

        colPagamento.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFormaPagamento()));

        colTotal.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("R$ %.2f", sistema.calcularTotalPedidos(cell.getValue()))));
    }

    private void configurarColunasAdicionais() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colValor.setCellFactory(coluna -> new TableCellMoeda<>());
    }

    @FXML
    private void selecionarDia() {
        periodoAtual = "DIA";
        atualizarEstiloBotoes();
        atualizarRelatorio();
    }

    @FXML
    private void selecionarSemana() {
        periodoAtual = "SEMANA";
        atualizarEstiloBotoes();
        atualizarRelatorio();
    }

    @FXML
    private void selecionarMes() {
        periodoAtual = "MES";
        atualizarEstiloBotoes();
        atualizarRelatorio();
    }

    @FXML
    private void alterarDataReferencia() {
        try {
            dataReferencia = LocalDate.parse(txtDataReferencia.getText(), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            txtDataReferencia.setText(dataReferencia.format(FORMATO_DATA));
            return;
        }
        atualizarRelatorio();
    }

    private void atualizarEstiloBotoes() {
        btnDia.setStyle(periodoAtual.equals("DIA") ? ESTILO_BOTAO_ATIVO : ESTILO_BOTAO_INATIVO);
        btnSemana.setStyle(periodoAtual.equals("SEMANA") ? ESTILO_BOTAO_ATIVO : ESTILO_BOTAO_INATIVO);
        btnMes.setStyle(periodoAtual.equals("MES") ? ESTILO_BOTAO_ATIVO : ESTILO_BOTAO_INATIVO);
    }

    private void atualizarRelatorio() {
        LocalDate inicio;
        LocalDate fim;

        switch (periodoAtual) {
            case "SEMANA":
                inicio = dataReferencia.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                fim = dataReferencia.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                break;
            case "MES":
                inicio = dataReferencia.with(TemporalAdjusters.firstDayOfMonth());
                fim = dataReferencia.with(TemporalAdjusters.lastDayOfMonth());
                break;
            default: // DIA
                inicio = dataReferencia;
                fim = dataReferencia;
                break;
        }

        carregarTabelaPedidos(inicio, fim);
        carregarTabelaAdicionais(inicio, fim);
    }

    private void carregarTabelaPedidos(LocalDate inicio, LocalDate fim) {
        List<Pedido> pedidos = sistema.buscarPedidoPorPeriodo(inicio, fim);

        tabelaPedidos.setItems(FXCollections.observableArrayList(pedidos));

        double total = pedidos.stream()
                .mapToDouble(sistema::calcularTotalPedidos)
                .sum();

        lblTotalPedidos.setText(String.format("R$ %.2f", total));
        lblQtdPedidos.setText(pedidos.size() + " pedido(s)");
    }

    private void carregarTabelaAdicionais(LocalDate inicio, LocalDate fim) {
        List<RelatorioAdicionalItem> itens = sistema.gerarRelatorioAdicionalLista(inicio, fim);

        tabelaAdicionais.setItems(FXCollections.observableArrayList(itens));

        double total = itens.stream().mapToDouble(RelatorioAdicionalItem::getValorTotal).sum();
        lblTotal.setText(String.format("R$ %.2f", total));
    }
}