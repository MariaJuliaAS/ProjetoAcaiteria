package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Funcionario;
import br.edu.ufersa.model.services.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalFuncionarioController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<String> cmbCargo;
    @FXML private Button btnSalvar;

    private final FuncionarioService service = new FuncionarioService();

    private Funcionario funcionario;
    private Runnable onSalvar;

    @FXML
    public void initialize() {
        cmbCargo.setItems(FXCollections.observableArrayList("Funcionário", "Admin"));
        cmbCargo.getSelectionModel().selectFirst();
    }

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    public void modoEdicao(Funcionario funcionario) {
        this.funcionario = funcionario;
        lblTitulo.setText("Editar Funcionário");
        btnSalvar.setText("Salvar");

        txtNome.setText(funcionario.getNome());
        txtLogin.setText(funcionario.getLogin());
        txtSenha.setPromptText("Deixe em branco para manter a senha atual");
        cmbCargo.getSelectionModel().select(funcionario.getTipo());
    }

    @FXML
    private void salvar() {
        String nome = txtNome.getText();
        String login = txtLogin.getText();
        String senhaDigitada = txtSenha.getText();
        String cargo = cmbCargo.getValue();

        try {
            if (funcionario == null) {
                if (senhaDigitada == null || senhaDigitada.isBlank()) {
                    mostrarErro("Informe uma senha para o novo funcionário.");
                    return;
                }

                Funcionario novo = new Funcionario(0, nome, login, senhaDigitada, cargo);
                service.cadastrarFuncionario(novo);

            } else {
                String senhaFinal = (senhaDigitada == null || senhaDigitada.isBlank())
                        ? funcionario.getSenha()
                        : senhaDigitada;

                funcionario.setNome(nome);
                funcionario.setLogin(login);
                funcionario.setSenha(senhaFinal);
                funcionario.setTipo(cargo);

                service.editarFuncionario(funcionario);
            }

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
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
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
