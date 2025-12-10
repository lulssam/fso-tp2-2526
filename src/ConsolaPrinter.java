/**
 * Interface para objetos que podem imprimir mensagens na consola.
 * Permite que as tarefas e o Gravador não dependam diretamente da GUI.
 */
public interface ConsolaPrinter {
    void print(String mensagem);
}
