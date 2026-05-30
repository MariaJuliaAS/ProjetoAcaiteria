import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.services.AdicionalService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== TESTANDO ADICIONAL SERVICE / DAO =====\n");

        AdicionalService service = new AdicionalService();

        // ===== TESTE 5: EDITAR ADICIONAL =====
        System.out.println("[TESTE 5] Editando Adicional (ID 5)...");
        try {
            Adicional paraEditar = new Adicional();
            paraEditar.setId(5);  // ID obtido do banco
            paraEditar.setNome("Granola Premium");
            paraEditar.setPreco(7.50);
            paraEditar.setQtdEstoque(75);

            service.editarAdicional(paraEditar);
            System.out.println("✓ Edição bem-sucedida!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro ao editar: " + e.getMessage() + "\n");
        }

        // ===== TESTE 6: VALIDAR EDIÇÃO =====
        System.out.println("[TESTE 6] Verificando edição (buscando Granola Premium)...");
        try {
            Adicional busca = new Adicional();
            busca.setNome("Granola Premium");

            Adicional encontrado = service.buscarPorNome(busca);
            if (encontrado != null) {
                System.out.println("✓ Edição confirmada: " + encontrado);
            }
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro na busca: " + e.getMessage() + "\n");
        }

        // ===== TESTE 7: EXCLUIR ADICIONAL =====
        System.out.println("[TESTE 7] Excluindo Adicional (ID 2)...");
        try {
            Adicional paraExcluir = new Adicional();
            paraExcluir.setId(6);  // ID obtido do banco

            service.excluirAdicional(paraExcluir);
            System.out.println("✓ Exclusão bem-sucedida!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro ao excluir: " + e.getMessage() + "\n");
        }

        // ===== TESTE 8: LISTAGEM FINAL =====
        System.out.println("[TESTE 8] Listagem final de Adicionais...");
        try {
            List<Adicional> todos = service.buscarTodos();
            System.out.println("✓ Total final: " + todos.size());
            for (Adicional ad : todos) {
                System.out.println("  - " + ad);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro na busca: " + e.getMessage());
        }

        System.out.println("\n===== TESTES FINALIZADOS =====");
    }
}