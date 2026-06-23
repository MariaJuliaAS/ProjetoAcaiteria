package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalEditarClienteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEndereco;
    @FXML private Button btnSalvar;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private Cliente cliente;
    private Runnable onSalvar;

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        txtNome.setText(cliente.getNome());
        txtTelefone.setText(cliente.getTelefone());
        txtEndereco.setText(cliente.getEndereco());
    }

    @FXML
    private void salvar() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (nome.isEmpty()) {
            mostrarErro("Nome é obrigatório.");
            return;
        }

        if (telefone.isEmpty()) {
            mostrarErro("Telefone é obrigatório.");
            return;
        }

        if (endereco.isEmpty()) {
            mostrarErro("Endereço é obrigatório.");
            return;
        }

        try {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);

            sistema.editarCliente(cliente);

            if (onSalvar != null) onSalvar.run();
            fechar();

        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void fechar() {
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao salvar");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}