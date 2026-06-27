package hospital.service.exception;

/**
 * Exceção lançada quando uma operação é realizada em uma fila vazia.
 * Exemplo: tentar chamar o próximo paciente sem ninguém na fila.
 */
public class FilaVaziaException extends Exception {
    public FilaVaziaException(String mensagem) {
        super(mensagem);
    }
}
