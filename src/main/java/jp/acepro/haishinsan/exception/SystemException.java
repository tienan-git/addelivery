package jp.acepro.haishinsan.exception;

/**
 * システム例外
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SystemException() {
		super();
	}

	public SystemException(String message) {
		super(message);
	}
}
