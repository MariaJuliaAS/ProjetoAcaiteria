package br.edu.ufersa.model.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Admin extends Funcionario {

    public Admin() {
        super(0, "Admin", "admin", "admin", "Admin");
    }

    public Admin(int id, String nome, String login, String senha){
        super(id, nome, login, senha, "Admin");
    }

    public void cadastrarProduto(Produto produto) {
        if (produto == null) {
            System.out.println("Erro: produto nulo");
            return;
        }
        //Produto.cadastrarProduto(produto);
        System.out.println("Produto: " + produto.getNome() + " cadastrado com sucesso.");
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            System.out.println("Erro: funcionário nulo");
            return;
        }
        // Aqui apenas uma mensagem; persistência de funcionário pode ser implementada posteriormente
        System.out.println("Funcionario " + funcionario.getNome() + " cadastrado com sucesso.");
    }

    /**
     * Imprime configuração do banco para verificar as properties/variáveis.
     */
    public void imprimirConfiguracaoDB() {
        String envUrl = System.getenv("DB_URL");
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASS");

        if (envUrl != null || envUser != null || envPass != null) {
            System.out.println("Config (via env):");
            System.out.println("DB_URL=" + envUrl);
            System.out.println("DB_USER=" + envUser);
            System.out.println("DB_PASS=" + (envPass == null ? "(null)" : "********"));
            return;
        }

        Path p = Paths.get("db.properties");
        if (Files.exists(p)) {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(p)) {
                props.load(in);
                System.out.println("Config (via db.properties):");
                System.out.println("db.url=" + props.getProperty("db.url"));
                System.out.println("db.user=" + props.getProperty("db.user"));
                System.out.println("db.password=" + (props.getProperty("db.password") == null ? "(null)" : "********"));
                return;
            } catch (IOException e) {
                System.out.println("Erro ao ler db.properties: " + e.getMessage());
                return;
            }
        }

        // try classpath resource
        try (InputStream in = Admin.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                System.out.println("Config (via classpath db.properties):");
                System.out.println("db.url=" + props.getProperty("db.url"));
                System.out.println("db.user=" + props.getProperty("db.user"));
                System.out.println("db.password=" + (props.getProperty("db.password") == null ? "(null)" : "********"));
                return;
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler db.properties do classpath: " + e.getMessage());
            return;
        }

        System.out.println("Nenhuma configuração de banco encontrada (nem variáveis de ambiente, nem db.properties).");
    }
}