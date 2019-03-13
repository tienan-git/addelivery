package jp.acepro.haishinsan.exception;

/**
 * 業務例外
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String[] params;

	public BusinessException(String message) {

		super(message);
	}

	public BusinessException(String message, String... params) {

		super(message);
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

}
