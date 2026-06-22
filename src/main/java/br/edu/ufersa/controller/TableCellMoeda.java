package br.edu.ufersa.controller;

import javafx.scene.control.TableCell;

public class TableCellMoeda<T> extends TableCell<T, Double> {

    @Override
    protected void updateItem(Double valor, boolean vazio) {
        super.updateItem(valor, vazio);

        if (vazio || valor == null) {
            setText(null);
        } else {
            setText(String.format("R$ %.2f", valor));
        }
    }
}
