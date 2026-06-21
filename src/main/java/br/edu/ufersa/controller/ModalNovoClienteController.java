package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Cliente;
import br.edu.ufersa.model.services.ClienteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalNovoClienteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEndereco;
    @FXML private Button btnAdicionar;
    @FXML private Button btnFechar;

    private final ClienteService service = new ClienteService();
    private Runnable onSalvar;

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    @FXML
    private void adicionar() {
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
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);

            service.cadastrar(cliente);

            if (onSalvar != null) onSalvar.run();
            fechar();

        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
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