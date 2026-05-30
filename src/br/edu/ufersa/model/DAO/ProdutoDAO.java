package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public void cadastrar(Produto p){
        String sql = "INSERT INTO produto (nome, preco) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,p.getNome());
            ps.setDouble(2,p.getPreco());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }

            System.out.println("Produto cadastrado com sucesso! ID: " + p.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editar(Produto p){
        String sql =  "UPDATE produto SET nome=?, preco=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,p.getNome());
            ps.setDouble(2,p.getPreco());
            ps.setInt(3,p.getId());
            ps.executeUpdate();

            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Produto p){
        String sql = "DELETE FROM produto WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,p.getId());
            ps.executeUpdate();

            System.out.println("Produto excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto> buscarTodos(){
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getDouble("preco"));
                produtos.add(p);
            }

            System.out.println("Produto atualizado com sucesso!");
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Produto buscarPorId(Produto p){
        String sql = "SELECT * FROM produto WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,p.getId());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                return produto;
            }

            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
