package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.services.AdicionalService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalEditarAdicionalController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private TextField txtEstoque;
    @FXML private Button btnSalvar;
    @FXML private Button btnFechar;

    private final AdicionalService service = new AdicionalService();
    private Adicional adicional;

    private Runnable onSalvar;

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    public void setAdicional(Adicional adicional) {
        this.adicional = adicional;
        txtNome.setText(adicional.getNome());
        txtPreco.setText(String.valueOf(adicional.getPreco()));
        txtEstoque.setText(String.valueOf(adicional.getQtdEstoque()));
    }

    @FXML
    private void salvar() {
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
            adicional.setNome(nome);
            adicional.setPreco(preco);
            adicional.setQtdEstoque(estoque);

            service.editarAdicional(adicional);

            if (onSalvar != null) {
                onSalvar.run();
            }

        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }finally{
            fechar();
        }
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle("Erro ao salvar");
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }
}
