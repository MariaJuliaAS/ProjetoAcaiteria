package br.edu.ufersa.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String gerarRelatorioAdicional(LocalDate inicio, LocalDate fim){
        if(inicio == null || fim == null){
            return "Datas inválidas!";
        }

        if(inicio.isAfter(fim)){
            return "Período inválido!";
        }

        List<Pedido> pedidos = new Pedido().getPedidos();
        if(pedidos == null || pedidos.isEmpty()){
            return "Nenhum pedido cadastrado!";
        }

        Map<Integer, Integer> qtdPorAdicional = new HashMap<>();
        Map<Integer, String> nomePorAdicional = new HashMap<>();
        Map<Integer, Double> valorPorAdicional = new HashMap<>();

        for(Pedido p: pedidos){
            if(p == null || p.getData() == null){
                continue;
            }

            LocalDate dataPedido = p.getData();
            boolean dentroDoPeriodo = !dataPedido.isBefore(inicio) && !dataPedido.isAfter(fim);
            if(!dentroDoPeriodo || p.getPedidos() == null || p.getPedidos().isEmpty()){
                continue;
            }

            for (ItemPedido item: p.getItensPedido()){
                if (item == null || item.getProduto() == null || item.getQuantidade() <= 0) {
                    continue;
                }

                List<Adicional> adicionaisDoPedido = item.getAdicionaisEscolhidos();
                if(adicionaisDoPedido == null){
                    continue;
                }

                for(Adicional ad: adicionaisDoPedido){
                    if(ad == null){
                        continue;
                    }

                    int idAdicional = ad.getId();
                    int qtdComprada = item.getQuantidade();

                    nomePorAdicional.put(idAdicional, ad.getNome());
                    qtdPorAdicional.put(idAdicional, (qtdPorAdicional.getOrDefault(idAdicional, 0) + qtdComprada));
                    valorPorAdicional.put(idAdicional, (valorPorAdicional.getOrDefault(idAdicional, 0.0) + ad.getPreco() * qtdComprada));
                }
            }
        }

        if(qtdPorAdicional.isEmpty()){
            return "Nenhum adicional comprado nesse período!";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATORIO DE ADICIONAIS\n");
        relatorio.append("Periodo: ").append(inicio).append(" ate ").append(fim).append("\n");
        relatorio.append("--------------------------------\n");

        double totalGeral = 0.0;
        for (Integer id : qtdPorAdicional.keySet()) {
            String nome = nomePorAdicional.get(id);
            int quantidade = qtdPorAdicional.get(id);
            double valor = valorPorAdicional.get(id);
            totalGeral += valor;

            relatorio.append("ID: ").append(id)
                    .append(" | Nome: ").append(nome)
                    .append(" | Qtd: ").append(quantidade)
                    .append(" | Total: R$ ").append(String.format("%.2f", valor))
                    .append("\n");
        }

        relatorio.append("--------------------------------\n");
        relatorio.append("Total geral: R$ ").append(String.format("%.2f", totalGeral));

        return relatorio.toString();

    }
}
