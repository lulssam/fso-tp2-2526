import java.util.concurrent.Semaphore;

public class BufferCircularComando {
    private final int dimensaoBuffer = 16;
    private final Semaphore livres, ocupados, acesso;
    private Comando buffer[];
    private int putBuffer, getBuffer;

    public BufferCircularComando() {
        livres = new Semaphore(dimensaoBuffer);
        ocupados = new Semaphore(0);
        acesso = new Semaphore(1);

        putBuffer = getBuffer = 0;

        buffer = new Comando[dimensaoBuffer];
    }

    public void inserirElemento(Comando c) {
        try {
            livres.acquire(); // bloqueia se buffer cheio
            acesso.acquire(); // obtém exclusão mútua para modificar buffer
            buffer[putBuffer] = c; // insere o comando na posição atual
            putBuffer = ++putBuffer % dimensaoBuffer; // avança índice circularmente
            acesso.release(); // liberta mutex
            ocupados.release(); // sinaliza que há mais um itém disponivel
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Comando removerElemento() {
        try {
            ocupados.acquire(); // bloqueia se buffer vazio
            acesso.acquire(); // exclusão mutua
            Comando c = new Comando(buffer[getBuffer]); // copia
            buffer[getBuffer] = null; // limpar posição
            getBuffer = ++getBuffer % dimensaoBuffer;
            acesso.release();
            livres.release(); // liberta uma posição livre
            return c;
        } catch (InterruptedException e) {
            // Interrupção durante espera -> devolver null para o consumidor lidar
            Thread.currentThread().interrupt();
            return null;
        }
    }

    // Limpar rapidamente o buffer (para PARAR imediato)
    public void limparBuffer() {
        try {
            acesso.acquire();

            for (int i = 0; i < dimensaoBuffer; i++) {
                buffer[i] = null;
            }

            int ocup = ocupados.drainPermits();
            if (ocup > 0) {
                livres.release(ocup);
            }
            putBuffer = 0;
            getBuffer = 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            acesso.release();
        }
    }
}