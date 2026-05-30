package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.Adicional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdicionalDAO {

    public void cadastrar(Adicional a){
        String sql = "INSERT INTO adicional (nome, preco, qtd_estoque) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNome());
            ps.setDouble(2, a.getPreco());
            ps.setInt(3, a.getQtdEstoque());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }

            System.out.println("Adicional cadastrado com sucesso! ID: " + a.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editar(Adicional a){
        String sql = "UPDATE adicional SET nome = ?, preco = ?, qtd_estoque = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNome());
            ps.setDouble(2, a.getPreco());
            ps.setInt(3, a.getQtdEstoque());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
            System.out.println("Adicional atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Adicional a){
        String sql = "DELETE FROM adicional WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getId());
            ps.executeUpdate();
            System.out.println("Adicional excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Adicional buscarPorNome(Adicional a){
        String sql = "SELECT * FROM adicional WHERE nome = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNome());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Adicional ad = new Adicional();
                    ad.setId(rs.getInt("id"));
                    ad.setNome(rs.getString("nome"));
                    ad.setPreco(rs.getDouble("preco"));
                    ad.setQtdEstoque(rs.getInt("qtd_estoque"));
                    return ad;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Adicional> buscarTodos(){
        String sql = "SELECT * FROM adicional";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Adicional> adicionais = new ArrayList<>();

            while (rs.next()) {
                Adicional ad = new Adicional();
                ad.setId(rs.getInt("id"));
                ad.setNome(rs.getString("nome"));
                ad.setPreco(rs.getDouble("preco"));
                ad.setQtdEstoque(rs.getInt("qtd_estoque"));
                adicionais.add(ad);
            }
            return adicionais;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Adicional buscarPorId(Adicional a){
        String sql = "SELECT * FROM adicional WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Adicional ad = new Adicional();
                    ad.setId(rs.getInt("id"));
                    ad.setNome(rs.getString("nome"));
                    ad.setPreco(rs.getDouble("preco"));
                    ad.setQtdEstoque(rs.getInt("qtd_estoque"));
                    return ad;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}


