package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.Adicional;
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
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,p.getNome());
            ps.setDouble(2,p.getPreco());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }

            salvarAdicionaisDisponiveis(p);

            System.out.println("Produto cadastrado com sucesso! ID: " + p.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editar(Produto p){
        String sql =  "UPDATE produto SET nome=?, preco=? WHERE id=?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,p.getNome());
            ps.setDouble(2,p.getPreco());
            ps.setInt(3,p.getId());
            ps.executeUpdate();

            removerAdicionaisDisponiveis(p);
            salvarAdicionaisDisponiveis(p);

            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Produto p){
        String sql = "DELETE FROM produto WHERE id=?";
        removerAdicionaisDisponiveis(p);

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

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
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getDouble("preco"));
                p.setAdicionaisDisponiveis(buscarAdicionaisDisponiveis(p));
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
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,p.getId());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                p.setAdicionaisDisponiveis(buscarAdicionaisDisponiveis(p));
                return produto;
            }

            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void salvarAdicionaisDisponiveis(Produto p){
        if(p.getAdicionaisDisponiveis() == null || p.getAdicionaisDisponiveis().isEmpty()){
            return;
        }

        String sql = "INSERT INTO produto_adicional(produto_id, adicional_id) VALUES (?, ?)";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)){

            for(Adicional a: p.getAdicionaisDisponiveis()){
                ps.setInt(1, p.getId());
                ps.setInt(2, a.getId());
                ps.executeUpdate();
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removerAdicionaisDisponiveis(Produto p){
        String sql = "DELETE FROM produto_adicional WHERE produto_id=?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, p.getId());
            ps.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Adicional> buscarAdicionaisDisponiveis(Produto p){
        String sql = "SELECT a.* FROM adicional a INNER JOIN produto_adicional pa ON a.id = pa.adicional_id WHERE pa.produto_id=?";
        List<Adicional> adicionaisDisponiveis = new ArrayList<>();
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Adicional a = new Adicional();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setPreco(rs.getDouble("preco"));
                a.setQtdEstoque(rs.getInt("qtd_estoque"));
                adicionaisDisponiveis.add(a);
            }

            return adicionaisDisponiveis;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
