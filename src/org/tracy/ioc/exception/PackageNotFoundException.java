package org.tracy.ioc.exception;


/**
 * 
* @ClassName: PackageNotFoundException 
* @Description: 鍖呮病鏈夋壘鍒�
* @author minjun
* @date 2015骞�鏈�鏃�涓婂崍9:59:04 
*
 */
public class PackageNotFoundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7782819407085925337L;

	public PackageNotFoundException() {
		super();
	}

	public PackageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackageNotFoundException(String message) {
		super(message);
	}

	public PackageNotFoundException(Throwable cause) {
		super(cause);
	}

}
