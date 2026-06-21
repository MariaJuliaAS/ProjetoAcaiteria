package br.edu.ufersa.model.services;

import br.edu.ufersa.model.DAO.PedidoDAO;
import br.edu.ufersa.model.entities.*;
import java.time.LocalDate;
import java.util.List;

public class PedidoService {

    private PedidoDAO pedidoDAO = new PedidoDAO();
    private AdicionalService adicionalService = new AdicionalService();

    public void cadastrar(Pedido p) {

        validarPedido(p);

        validarItens(p);

        atualizarEstoqueAdicionais(p);

        pedidoDAO.cadastrar(p);
    }

    public void editar(Pedido p) {
        if (p.getId() <= 0) {
            throw new RuntimeException("Pedido sem ID para edição");
        }

        validarPedido(p);

        pedidoDAO.editar(p);
    }

    public void excluir(Pedido p) {

        if (p == null) {
            throw new RuntimeException("Pedido inválido");
        }

        if (p.getId() <= 0) {
            throw new RuntimeException("Pedido sem ID para exclusão");
        }

        pedidoDAO.excluir(p);
    }

    private void validarPedido(Pedido p) {

        if (p == null) {
            throw new RuntimeException("Pedido não pode ser nulo");
        }

        if (p.getCliente() == null) {
            throw new RuntimeException("Pedido precisa de cliente");
        }

        if (p.getFormaPagamento() == null || p.getFormaPagamento().isBlank()) {
            throw new RuntimeException("Forma de pagamento obrigatória");
        }
    }

    private void validarItens(Pedido p) {

        if (p.getItensPedido() == null || p.getItensPedido().isEmpty()) {
            throw new RuntimeException("Pedido deve ter pelo menos 1 item");
        }

        for (ItemPedido item : p.getItensPedido()) {

            if (item.getProduto() == null) {
                throw new RuntimeException("Item sem produto");
            }

            if (item.getQuantidade() <= 0) {
                throw new RuntimeException("Quantidade inválida");
            }
        }
    }

    private void atualizarEstoqueAdicionais(Pedido p) {

        for (ItemPedido item : p.getItensPedido()) {

            for (Adicional a : item.getAdicionaisEscolhidos()) {

                if (a.getQtdEstoque() <= 0) {
                    throw new RuntimeException("Sem estoque do adicional: " + a.getNome());
                }

                a.setQtdEstoque(a.getQtdEstoque() - 1);

                adicionalService.editarAdicional(a);
            }
        }
    }

    public double calcularTotal(Pedido p) {

        double total = 0;

        for (ItemPedido item : p.getItensPedido()) {

            total += item.getProduto().getPreco() * item.getQuantidade();

            for (Adicional a : item.getAdicionaisEscolhidos()) {
                total += a.getPreco();
            }
        }

        return total;
    }

    public List<Pedido> buscarPorData(LocalDate data) {

        if (data == null) {
            throw new RuntimeException("Data inválida");
        }

        return pedidoDAO.buscarPorData(data);
    }

    public List<Pedido> buscarPorCliente(Cliente c) {

        if (c.getId() <= 0) {
            throw new RuntimeException("Cliente inválido");
        }

        return pedidoDAO.buscarPorCliente(c);
    }

    public List<Pedido> buscarPorProduto(Produto p) {

        if (p.getId() <= 0) {
            throw new RuntimeException("Produto inválido");
        }

        return pedidoDAO.buscarPorProduto(p);
    }

    public Pedido buscarPorId(int id) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }

        return pedidoDAO.buscarPorId(id);
    }

    public List<Pedido> buscarTodos() {
        return pedidoDAO.buscarTodos();
    }

    public String gerarNota(Pedido p) {

        String nota = "";

        nota += "===== NOTA DO PEDIDO =====\n";
        nota += "Pedido ID: " + p.getId() + "\n";
        nota += "Cliente: " + p.getCliente().getNome() + "\n";
        nota += "Data: " + p.getData() + "\n";
        nota += "Forma de Pagamento: " + p.getFormaPagamento() + "\n\n";

        double total = 0;

        for (ItemPedido item : p.getItensPedido()) {

            double subtotal = item.getProduto().getPreco() * item.getQuantidade();
            total += subtotal;

            nota += "Produto: " + item.getProduto().getNome()
                    + " | Qtd: " + item.getQuantidade()
                    + " | Subtotal: R$ " + subtotal + "\n";

            for (Adicional a : item.getAdicionaisEscolhidos()) {

                nota += "   + Adicional: " + a.getNome()
                        + " | R$ " + a.getPreco() + "\n";

                total += a.getPreco();
            }
        }

        nota += "\nTOTAL: R$ " + total;
        nota += "\n==========================";

        return nota;
    }

    public String gerarRelatorio(LocalDate inicio, LocalDate fim) {

        List<Pedido> pedidos = pedidoDAO.buscarPorPeriodo(inicio, fim);

        if (pedidos.isEmpty()) {
            return "Nenhum pedido encontrado no período.";
        }

        String relatorio = "===== RELATÓRIO DE PEDIDOS =====\n";
        relatorio += "Período: " + inicio + " até " + fim + "\n\n";

        for (Pedido p : pedidos) {

            relatorio += "Pedido ID: " + p.getId() + "\n";
            relatorio += "Data: " + p.getData() + "\n";
            relatorio += "Forma de pagamento: " + p.getFormaPagamento() + "\n";

            if (p.getCliente() != null) {
                relatorio += "Cliente ID: " + p.getCliente().getId() + "\n";
            }

            relatorio += "-------------------------\n";
        }

        relatorio += "Total de pedidos: " + pedidos.size();

        return relatorio;
    }
}