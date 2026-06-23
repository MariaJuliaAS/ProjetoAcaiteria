package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.entities.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModalProdutoController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private GridPane gridAdicionais;
    @FXML private Button btnSalvar;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    private final List<CheckBox> checkBoxesAdicionais = new ArrayList<>();

    private Produto produto;
    private Runnable onSalvar;

    @FXML
    public void initialize() {
        montarChecklistAdicionais();
    }

    public void setOnSalvar(Runnable onSalvar) {
        this.onSalvar = onSalvar;
    }

    public void modoEdicao(Produto produto) {
        this.produto = produto;
        lblTitulo.setText("Editar Produto");
        btnSalvar.setText("Salvar");

        txtNome.setText(produto.getNome());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        marcarAdicionaisDoProduto(produto);
    }

    private void montarChecklistAdicionais() {
        List<Adicional> todos = sistema.buscarAdicionaisTodos();

        int coluna = 0;
        int linha = 0;

        for (Adicional adicional : todos) {
            CheckBox checkBox = new CheckBox(adicional.getNome());
            checkBox.setUserData(adicional);
            checkBoxesAdicionais.add(checkBox);

            gridAdicionais.add(checkBox, coluna, linha);

            coluna++;
            if (coluna > 1) {
                coluna = 0;
                linha++;
            }
        }
    }

    private void marcarAdicionaisDoProduto(Produto produto) {
        if (produto.getAdicionaisDisponiveis() == null) {
            return;
        }

        for (Adicional adicionalDoProduto : produto.getAdicionaisDisponiveis()) {
            for (CheckBox checkBox : checkBoxesAdicionais) {
                Adicional adicionalDoCheckBox = (Adicional) checkBox.getUserData();
                if (adicionalDoCheckBox.getId() == adicionalDoProduto.getId()) {
                    checkBox.setSelected(true);
                }
            }
        }
    }

    private List<Adicional> obterAdicionaisSelecionados() {
        List<Adicional> selecionados = new ArrayList<>();
        for (CheckBox checkBox : checkBoxesAdicionais) {
            if (checkBox.isSelected()) {
                selecionados.add((Adicional) checkBox.getUserData());
            }
        }
        return selecionados;
    }

    @FXML
    private void salvar() {
        String nome = txtNome.getText();

        double preco;
        try {
            preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarErro("Preço precisa ser um número válido.");
            return;
        }

        List<Adicional> adicionaisSelecionados = obterAdicionaisSelecionados();

        try {
            if (produto == null) {
                Produto novo = new Produto(0, nome, preco);
                novo.setAdicionaisDisponiveis(adicionaisSelecionados);
                sistema.cadastrarProduto(novo);
            } else {
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setAdicionaisDisponiveis(adicionaisSelecionados);
                sistema.editarProduto(produto);
            }

            if (onSalvar != null) {
                onSalvar.run();
            }
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
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle("Erro ao salvar");
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }
}
