package hospital.service.exception;

/**
 * Exceção lançada quando um paciente buscado pelo ID não é encontrado
 * na lista de pacientes do hospital.
 */
public class PacienteNaoEncontradoException extends Exception {
    public PacienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
