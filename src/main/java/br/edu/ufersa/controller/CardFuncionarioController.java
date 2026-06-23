package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Funcionario;
import br.edu.ufersa.view.MainApp;
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

public class CardFuncionarioController {

    @FXML private VBox cardRoot;
    @FXML private Label lblNome;
    @FXML private Label lblCargo;
    @FXML private Label lblLogin;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Funcionario funcionario;
    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private Runnable onAlterado;

    public void setDados(Funcionario funcionario) {
        this.funcionario = funcionario;
        lblNome.setText(funcionario.getNome());
        lblCargo.setText(funcionario.getTipo());
        lblLogin.setText(funcionario.getLogin());
    }

    public void setOnAlterado(Runnable onAlterado) {
        this.onAlterado = onAlterado;
    }

    @FXML
    private void excluir() {
        boolean ehOProprioUsuarioLogado = MainApp.getFuncionarioLogado() != null
                && MainApp.getFuncionarioLogado().getId() == funcionario.getId();

        if (ehOProprioUsuarioLogado) {
            mostrarErro("Você não pode excluir o próprio usuário enquanto estiver logado com ele.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja excluir o funcionário \"" + funcionario.getNome() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    sistema.excluirFuncionario(funcionario);
                    if (onAlterado != null) {
                        onAlterado.run();
                    }
                } catch (IllegalArgumentException e) {
                    mostrarErro(e.getMessage());
                } catch (RuntimeException e) {
                    mostrarErro("Não foi possível excluir o funcionário. Tente novamente.");
                }
            }
        });
    }

    @FXML
    private void editar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-funcionario.fxml"));
            Parent root = loader.load();

            ModalFuncionarioController controller = loader.getController();
            controller.modoEdicao(funcionario);
            controller.setOnSalvar(onAlterado);

            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Funcionário");
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
        erro.setTitle("Erro");
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }
}
