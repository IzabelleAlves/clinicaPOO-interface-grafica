package crud.clinica.exception;

public class CPFJaExisteException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CPFJaExisteException(String mensagem) {
        super(mensagem);
    }
}