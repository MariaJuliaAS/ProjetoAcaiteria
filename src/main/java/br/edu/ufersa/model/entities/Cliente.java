package br.edu.ufersa.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nome;
    private String telefone;
    private String endereco;

    private static List<Cliente> clientes = new ArrayList<>();

    public Cliente (){}
    public Cliente (int id, String nome, String telefone, String endereco){
        this.id = id;
        setNome(nome);
        setTelefone(telefone);
        setEndereco(endereco);
    }

    public int getId() {return id;}

    public String getNome() {return nome;}

    public String getTelefone() {return telefone;}

    public String getEndereco() {return endereco;}

    public static List<Cliente> getClientes() {return clientes;}

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()){
            System.out.println("Nome inválido");
            return;
        }

        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            System.out.println("Telefone inválido");
            return;
        }

        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isEmpty()) {
            System.out.println("Endereço inválido");
            return;
        }

        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Cliente:\n" +
                "Id = " + id +
                "\nNome = " + nome +
                "\nTelefone = " + telefone +
                "\nEndereço = " + endereco;
    }
}
