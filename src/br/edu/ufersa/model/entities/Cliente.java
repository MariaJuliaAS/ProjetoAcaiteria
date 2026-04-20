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

    public static void cadastrarCliente(Cliente c){
        if (c.getNome() == null || c.getNome().isEmpty()){
            System.out.println("Nome inválido");
            return;
        }

        if (c.getTelefone() == null || c.getTelefone().isEmpty()) {
            System.out.println("Telefone inválido");
            return;
        }

        if (c.getEndereco() == null || c.getEndereco().isEmpty()) {
            System.out.println("Endereço inválido");
            return;
        }

        for(Cliente cl : clientes){
            if (cl.getId() == c.getId()){
                System.out.println("ID já cadastrado");
                return;
            }
        }

        clientes.add(c);
        System.out.println("Cliente cadastrado");
    }

    public static void editarCliente(Cliente c){
        for (Cliente cl : clientes){
            if (cl.getId() == c.getId()){
                cl.setNome(c.getNome());
                cl.setTelefone(c.getTelefone());
                cl.setEndereco(c.getEndereco());

                System.out.println("Cliente atualizado");
                return;
            }
        }

        System.out.println("Cliente não encontrado");
    }

    public static void excluirCliente(int id){
        for (Cliente cl : clientes){
            if (id == cl.getId()){
                clientes.remove(cl);
                System.out.println("Cliente excluído");
                return;
            }
        }

        System.out.println("Cliente não encontrado");
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
