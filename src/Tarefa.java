import java.util.concurrent.Semaphore;
public abstract class Tarefa extends Thread {

    public Semaphore semaforo_dormir;
    public Estados estado;

    public Tarefa() {
        estado = Estados.DORMIR;
        semaforo_dormir = new Semaphore(0);
    }

    /**
     * Este trabalhar é geral. Todas as tarefas que estendem desta vão obrigatoriamente
     * ter este método. Mas como as tarefas vão ser diferentes a implementação do método vai mudar
     */
    abstract void trabalharAtividade() throws InterruptedException;

    public void dormir() throws InterruptedException {
        semaforo_dormir.acquire();
    }

    public void ativar() {
        estado = Estados.TRABALHAR;
        semaforo_dormir.release();
    }

    public void desativar() {
        estado = Estados.DORMIR;
    }


    public void run() {
        while (estado != Estados.TERMINAR) {
            switch (estado) {
                case DORMIR:
                    try {
                        dormir();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case TRABALHAR:
                    try {
                        trabalharAtividade();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    break;
                case TERMINAR:
                    desativar();

                    break;
            }
        }
    }

    public enum Estados {
        TERMINAR,
        DORMIR,
        TRABALHAR
    }
}
