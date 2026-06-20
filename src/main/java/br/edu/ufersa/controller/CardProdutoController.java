package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Produto;
import br.edu.ufersa.model.services.ProdutoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CardProdutoController {

    @FXML private VBox cardRoot;
    @FXML private Label lblNome;
    @FXML private Label lblPreco;
    @FXML private Label lblAdicionais;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Produto produto;
    private final ProdutoService service = new ProdutoService();

    private Runnable onAlterado;

    public void setDados(Produto produto) {
        this.produto = produto;
        lblNome.setText(produto.getNome());
        lblPreco.setText(String.format("R$ %.2f", produto.getPreco()));

        int qtd = produto.getAdicionaisDisponiveis() == null ? 0 : produto.getAdicionaisDisponiveis().size();
        lblAdicionais.setText(qtd + " adicionais disponíveis");
    }

    public void setOnAlterado(Runnable onAlterado) {
        this.onAlterado = onAlterado;
    }

    @FXML
    private void excluir() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja excluir o produto \"" + produto.getNome() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    service.excluirProduto(produto);
                    if (onAlterado != null) {
                        onAlterado.run();
                    }
                } catch (IllegalArgumentException e) {
                    mostrarErro(e.getMessage());
                } catch (RuntimeException e) {
                    if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        mostrarErro("Este produto não pode ser excluído porque já foi usado em pedidos.");
                    } else {
                        mostrarErro("Não foi possível excluir o produto. Tente novamente.");
                    }
                }
            }
        });
    }

    @FXML
    private void editar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-produto.fxml"));
            Parent root = loader.load();

            ModalProdutoController controller = loader.getController();
            controller.modoEdicao(produto);
            controller.setOnSalvar(onAlterado);

            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Produto");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle("Erro ao excluir");
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }
}
