package br.edu.ufersa.model.DAO;

import br.edu.ufersa.model.connectionFactory.ConnectionFactory;
import br.edu.ufersa.model.entities.*;
import br.edu.ufersa.model.interfaces.DAOInterface;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements DAOInterface<Pedido> {

    @Override
    public void cadastrar(Pedido p) {
        String sql = "INSERT INTO pedido (data, forma_pagamento, cliente_id) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(p.getData()));
            ps.setString(2, p.getFormaPagamento());
            ps.setInt(3, p.getCliente().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
                salvarItens(con, p);
            }

            System.out.println("Pedido cadastrado com sucesso! ID: " + p.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editar(Pedido p) {
        String sql = "UPDATE pedido SET data = ?, forma_pagamento = ?, cliente_id = ? WHERE id = ?";
        Connection con = ConnectionFactory.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(p.getData()));
            ps.setString(2, p.getFormaPagamento());
            ps.setInt(3, p.getCliente().getId());
            ps.setInt(4,p.getId());
            ps.execute();
            System.out.println("Pedido atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(Pedido p) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        Connection con = ConnectionFactory.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getId());
            ps.execute();
            System.out.println("Pedido excluído com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void salvarItens(Connection con, Pedido p) throws SQLException {

        String sql = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (ItemPedido item : p.getItensPedido()) {

                ps.setInt(1, p.getId());
                ps.setInt(2, item.getProduto().getId());
                ps.setInt(3, item.getQuantidade());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    item.setId(rs.getInt(1));
                    salvarItemAdicional(con, item);
                }
            }
        }
    }

    public void salvarItemAdicional(Connection con, ItemPedido ip) throws SQLException {

        if(ip.getAdicionaisEscolhidos() == null || ip.getAdicionaisEscolhidos().isEmpty()){
            return;
        }

        String sql = "INSERT INTO item_pedido_adicional (item_pedido_id, adicional_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (Adicional a : ip.getAdicionaisEscolhidos()) {

                ps.setInt(1, ip.getId());
                ps.setInt(2, a.getId());

                ps.executeUpdate();
            }
        }
    }

    private Pedido construirPedido(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("cliente_id"));
        c.setNome(rs.getString("nome"));
        c.setTelefone(rs.getString("telefone"));
        c.setEndereco(rs.getString("endereco"));

        int id = rs.getInt("id");

        return new Pedido.Builder()
                .id(id)
                .data(rs.getDate("data").toLocalDate())
                .formaPagamento(rs.getString("forma_pagamento"))
                .cliente(c)
                .itensPedido(buscarItens(id))
                .build();
    }

    public List<Pedido> buscarPorData(LocalDate data) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.nome, c.telefone, c.endereco " +
                "FROM pedido p " +
                "JOIN cliente c ON p.cliente_id = c.id " +
                "WHERE p.data = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(construirPedido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> buscarPorCliente(Cliente c) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.nome, c.telefone, c.endereco " +
                "FROM pedido p " +
                "JOIN cliente c ON p.cliente_id = c.id " +
                "WHERE p.cliente_id = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setInt(1, c.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(construirPedido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> buscarPorProduto(Produto produto) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql =
                "SELECT DISTINCT p.*, c.nome, c.telefone, c.endereco " +
                        "FROM pedido p " +
                        "JOIN cliente c ON p.cliente_id = c.id " +
                        "JOIN item_pedido ip ON p.id = ip.pedido_id " +
                        "WHERE ip.produto_id = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setInt(1, produto.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(construirPedido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql =
                "SELECT p.*, c.nome, c.telefone, c.endereco " +
                        "FROM pedido p " +
                        "JOIN cliente c ON p.cliente_id = c.id " +
                        "WHERE p.data BETWEEN ? AND ? " +
                        "ORDER BY p.data";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(construirPedido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public Pedido buscarPorId(int id) {
        String sql = "SELECT p.*, c.nome, c.telefone, c.endereco " +
                "FROM pedido p " +
                "JOIN cliente c ON p.cliente_id = c.id " +
                "WHERE p.id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return construirPedido(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Pedido> buscarTodos() {
        String sql = "SELECT p.*, c.nome, c.telefone, c.endereco " +
                "FROM pedido p " +
                "JOIN cliente c ON p.cliente_id = c.id";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(construirPedido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    private List<ItemPedido> buscarItens(int pedidoId) {
        List<ItemPedido> itens = new ArrayList<>();

        String sql = "SELECT ip.*, pr.nome, pr.preco " +
                "FROM item_pedido ip " +
                "JOIN produto pr ON ip.produto_id = pr.id " +
                "WHERE ip.pedido_id = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setInt(1, pedidoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Produto pr = new Produto(
                        rs.getInt("produto_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                );

                int itemId = rs.getInt("id");

                ItemPedido item = new ItemPedido.Builder()
                        .id(itemId)
                        .quantidade(rs.getInt("quantidade"))
                        .produto(pr)
                        .adicionaisEscolhidos(buscarAdicionais(itemId))
                        .build();

                itens.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }

    private List<Adicional> buscarAdicionais(int itemId) {
        List<Adicional> lista = new ArrayList<>();

        String sql = "SELECT a.* " +
                "FROM adicional a " +
                "JOIN item_pedido_adicional ipa ON a.id = ipa.adicional_id " +
                "WHERE ipa.item_pedido_id = ?";

        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Adicional a = new Adicional();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setPreco(rs.getDouble("preco"));
                a.setQtdEstoque(rs.getInt("qtd_estoque"));

                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}