package br.edu.ufersa.model.entities;

import java.util.ArrayList;
import java.util.List;

public class SistemaAcaiteria {
    private List<Adicional>  adicionais;
    private List<Produto>  produtos;

    public SistemaAcaiteria() {
        this.adicionais = Adicional.adicionais;
        this.produtos = Produto.produtos;
    }

    public List<Adicional> buscarAdicionais(String nome){
        List<Adicional> resultado = new ArrayList<>();

        if(nome == null || nome.isEmpty()){
            System.out.println("Nome não pode ser vazio!");
            return resultado;
        }

        for (Adicional ad: adicionais){
            if(ad.getNome().toLowerCase().contains(nome.toLowerCase())){
                resultado.add(ad);
            }
        }
        return resultado;
    }
}
