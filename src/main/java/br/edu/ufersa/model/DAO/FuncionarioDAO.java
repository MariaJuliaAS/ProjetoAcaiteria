package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void cadastrar(Funcionario f){
        String sql = "INSERT INTO funcionario (nome, login, senha, tipo) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, f.getNome());
            ps.setString(2, f.getLogin());
            ps.setString(3, f.getSenha());
            ps.setString(4, f.getTipo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setId(rs.getInt(1));
                }
            }

            System.out.println("Funcionario cadastrado com sucesso! ID: " + f.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editar(Funcionario f){
        String sql = "UPDATE funcionario SET nome = ?, login = ?, senha = ?, tipo = ? WHERE id = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getNome());
            ps.setString(2, f.getLogin());
            ps.setString(3, f.getSenha());
            ps.setString(4, f.getTipo());
            ps.setInt(5, f.getId());
            ps.executeUpdate();
            System.out.println("Funcionario atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Funcionario f){
        String sql = "DELETE FROM funcionario WHERE id = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getId());
            ps.executeUpdate();
            System.out.println("Funcionario excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Funcionario> buscarTodos(){
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> funcionarios = new ArrayList<>();
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                funcionarios.add(mapearLinha(rs));
            }
            return funcionarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Funcionario buscarPorId(Funcionario f){
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLinha(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Funcionario buscarPorLogin(String login){
        String sql = "SELECT * FROM funcionario WHERE login = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLinha(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Funcionario mapearLinha(ResultSet rs) throws SQLException {
        return new Funcionario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("login"),
                rs.getString("senha"),
                rs.getString("tipo")
        );
    }
}
