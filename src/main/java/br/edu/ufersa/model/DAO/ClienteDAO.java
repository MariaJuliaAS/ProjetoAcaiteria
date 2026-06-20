package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.Cliente;
import br.edu.ufersa.model.entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public Cliente cadastrar(Cliente cliente) {
        Connection con = ConnectionFactory.getConnection();

        String sql = "INSERT INTO cliente (nome,telefone,endereco)" +
                "VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEndereco());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                cliente.setId(rs.getInt(1));
            }
            System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public void editar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, endereco = ? WHERE id = ?";
        Connection con = ConnectionFactory.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEndereco());
            ps.setInt(4, cliente.getId());
            ps.execute();
            System.out.println("Cliente atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        Connection con = ConnectionFactory.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            ps.execute();
            System.out.println("Cliente excluído com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> buscarPorNome(String nome) {

        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();
        Connection con = ConnectionFactory.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                c.setTelefone(rs.getString("telefone"));
                clientes.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public List<Cliente> buscarTodos(){
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        Connection con = ConnectionFactory.getConnection();

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setEndereco(rs.getString("endereco"));
                clientes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}