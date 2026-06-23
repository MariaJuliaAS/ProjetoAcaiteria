package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Adicional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalNovoAdicionalController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private TextField txtEstoque;
    @FXML private Button btnAdicionar;
    @FXML private Button btnFechar;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private Runnable onSalvar;

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    @FXML
    private void adicionar() {
        String nome = txtNome.getText();

        double preco;
        int estoque;
        try {
            preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
            estoque = Integer.parseInt(txtEstoque.getText());
        } catch (NumberFormatException e) {
            mostrarErro("Preço e estoque precisam ser números válidos.");
            return;
        }

        try {
            Adicional novo = new Adicional(0, nome, preco, estoque);
            sistema.cadastrarAdicional(novo);

            if (onSalvar != null) {
                onSalvar.run();
            }
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
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle("Erro ao cadastrar");
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }
}
