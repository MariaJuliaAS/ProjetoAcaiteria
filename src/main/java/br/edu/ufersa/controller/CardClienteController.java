package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Cliente;
import br.edu.ufersa.model.entities.Pedido;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class CardClienteController implements CardInterface<Cliente> {

    @FXML private Label lblNome;
    @FXML private Label lblTel;
    @FXML private Label lblEnd;

    private Cliente cliente;
    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private Runnable onAlterado;

    public void setDados(Cliente cliente) {
        this.cliente = cliente;
        lblNome.setText(cliente.getNome());
        lblTel.setText(cliente.getTelefone());
        lblEnd.setText(cliente.getEndereco());
    }

    public void setOnAlterado(Runnable onAlterado) {
        this.onAlterado = onAlterado;
    }

    @FXML
    private void excluir() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja excluir o cliente \"" + cliente.getNome() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {

                List<Pedido> pedidos = sistema.buscarPedidoPorCliente(cliente);

                if (!pedidos.isEmpty()) {
                    mostrarErro("Este cliente não pode ser excluído porque já possui pedidos vinculados.");
                    return;
                }

                try {
                    sistema.excluirCliente(cliente);
                    if (onAlterado != null) onAlterado.run();
                } catch (IllegalArgumentException e) {
                    mostrarErro(e.getMessage());
                }
            }
        });
    }

    @FXML
    protected void editar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-editar-cliente.fxml"));
            Parent root = loader.load();

            ModalEditarClienteController controller = loader.getController();
            controller.setCliente(cliente);
            controller.setOnSalvar(onAlterado);

            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Cliente");
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
