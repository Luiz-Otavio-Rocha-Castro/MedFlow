package hospital.service.exception;

/**
 * Exceção lançada quando não há leito disponível para internação
 * ou quando se tenta alocar um leito já ocupado.
 */
public class LeitoIndisponivelException extends Exception {
    public LeitoIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
