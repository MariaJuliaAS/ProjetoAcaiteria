package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.*;
import br.edu.ufersa.model.services.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModalNovoPedidoController {

    @FXML private FlowPane containerProdutos;
    @FXML private GridPane gridAdicionais;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<String> cmbPagamento;
    @FXML private ListView<ItemPedido> listaItens;
    @FXML private Label lblNenhumItem;
    @FXML private Label lblTotal;
    @FXML private Button btnFinalizar;

    private final ProdutoService produtoService = new ProdutoService();
    private final AdicionalService adicionalService = new AdicionalService();
    private final ClienteService clienteService = new ClienteService();
    private final PedidoService pedidoService = new PedidoService();

    private final List<ItemPedido> itensPedido = new ArrayList<>();
    private final List<CheckBox> checkBoxesAdicionais = new ArrayList<>();
    private Runnable onSalvar;

    @FXML
    public void initialize() {
        carregarClientes();
        carregarPagamentos();
        carregarProdutos();
        carregarAdicionais();
        configurarListView();
        atualizarVisibilidadeLista();
    }

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    private void carregarClientes() {
        List<Cliente> clientes = clienteService.buscarTodos();
        cmbCliente.getItems().addAll(clientes);

        cmbCliente.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNome());
            }
        });
        cmbCliente.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNome());
            }
        });
    }

    private void carregarPagamentos() {
        cmbPagamento.getItems().addAll("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix");
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoService.buscarTodos();
        containerProdutos.getChildren().clear();

        for (Produto produto : produtos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card-produto-pedido.fxml"));
                Parent card = loader.load();

                CardProdutoPedidoController controller = loader.getController();
                controller.setDados(produto);
                controller.setOnAdicionar(() -> adicionarProduto(produto));

                containerProdutos.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void carregarAdicionais() {
        List<Adicional> adicionais = adicionalService.buscarTodos();
        gridAdicionais.getChildren().clear();
        checkBoxesAdicionais.clear();

        int coluna = 0;
        int linha = 0;

        for (Adicional adicional : adicionais) {

            if (adicional.getQtdEstoque() <= 0) continue;

            CheckBox cb = new CheckBox(adicional.getNome() +
                    " (R$ " + String.format("%.2f", adicional.getPreco()) + ")");
            cb.setUserData(adicional);
            checkBoxesAdicionais.add(cb);

            gridAdicionais.add(cb, coluna, linha);
            coluna++;
            if (coluna > 1) {
                coluna = 0;
                linha++;
            }
        }
    }

    private void adicionarProduto(Produto produto) {
        List<Adicional> adicionaisMarcados = obterAdicionaisSelecionados();

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(1);
        item.setAdicionaisEscolhidos(new ArrayList<>(adicionaisMarcados));

        itensPedido.add(item);
        listaItens.getItems().add(item);
        listaItens.getSelectionModel().select(item);

        atualizarTotal();
        atualizarVisibilidadeLista();
    }

    private List<Adicional> obterAdicionaisSelecionados() {
        List<Adicional> selecionados = new ArrayList<>();
        for (CheckBox cb : checkBoxesAdicionais) {
            if (cb.isSelected()) {
                selecionados.add((Adicional) cb.getUserData());
            }
        }
        return selecionados;
    }

    private void configurarListView() {
        listaItens.setCellFactory(lv -> new ListCell<>() {
            private final Button btnExcluir = new Button("🗑");
            private final Label lblTexto = new Label();
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(10, lblTexto, spacer, btnExcluir);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER_LEFT);

                btnExcluir.setStyle("-fx-background-color: transparent; -fx-text-fill: #cc0000; " +
                        "-fx-font-size: 14px; -fx-cursor: hand;");

                btnExcluir.setOnAction(e -> {
                    ItemPedido item = getItem();
                    if (item != null) {
                        itensPedido.remove(item);
                        listaItens.getItems().remove(item);
                        atualizarTotal();
                        atualizarVisibilidadeLista();
                    }
                });
            }

            @Override
            protected void updateItem(ItemPedido item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    int qtdAdicionais = item.getAdicionaisEscolhidos().size();
                    lblTexto.setText(item.getQuantidade() + "x " + item.getProduto().getNome() +
                            " — " + qtdAdicionais + " adicional(is)");
                    setGraphic(hbox);
                }
            }
        });
    }

    private void atualizarTotal() {
        double total = itensPedido.stream()
                .mapToDouble(item -> {
                    double precoAdicionais = item.getAdicionaisEscolhidos()
                            .stream()
                            .mapToDouble(Adicional::getPreco)
                            .sum();
                    return item.getProduto().getPreco() + precoAdicionais;
                })
                .sum();

        lblTotal.setText(String.format("R$ %.2f", total));
    }

    private void atualizarVisibilidadeLista() {
        boolean vazio = itensPedido.isEmpty();
        lblNenhumItem.setVisible(vazio);
        listaItens.setVisible(!vazio);
    }

    @FXML
    private void finalizarPedido() {
        if (cmbCliente.getValue() == null) {
            mostrarErro("Selecione um cliente.");
            return;
        }
        if (cmbPagamento.getValue() == null) {
            mostrarErro("Selecione a forma de pagamento.");
            return;
        }
        if (itensPedido.isEmpty()) {
            mostrarErro("Adicione ao menos um produto.");
            return;
        }

        List<Adicional> semEstoque = verificarEstoque();
        if (!semEstoque.isEmpty()) {
            String nomes = semEstoque.stream()
                    .map(Adicional::getNome)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            mostrarErro("Estoque insuficiente para os seguintes adicionais: " + nomes +
                    "\nRemova-os do pedido antes de finalizar.");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cmbCliente.getValue());
        pedido.setFormaPagamento(cmbPagamento.getValue());
        pedido.setData(LocalDate.now());
        pedido.setItensPedido(itensPedido);

        pedidoService.cadastrar(pedido);

        if (onSalvar != null) onSalvar.run();

        mostrarSucesso("Pedido cadastrado com sucesso!");
        fechar();
    }

    private List<Adicional> verificarEstoque() {
        List<Adicional> atualizados = adicionalService.buscarTodos();
        List<Adicional> semEstoque = new ArrayList<>();

        Map<Integer, Integer> contagem = new HashMap<>();
        for (ItemPedido item : itensPedido) {
            for (Adicional a : item.getAdicionaisEscolhidos()) {
                contagem.merge(a.getId(), 1, Integer::sum);
            }
        }

        for (Adicional atualizado : atualizados) {
            int qtdPedida = contagem.getOrDefault(atualizado.getId(), 0);
            if (qtdPedida > 0 && atualizado.getQtdEstoque() < qtdPedida) {
                semEstoque.add(atualizado);
            }
        }

        return semEstoque;
    }

    private void fechar() {
        Stage stage = (Stage) btnFinalizar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}