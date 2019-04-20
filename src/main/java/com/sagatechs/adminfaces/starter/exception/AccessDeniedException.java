	package com.sagatechs.adminfaces.starter.exception;

    import javax.ejb.ApplicationException;
    import java.io.Serializable;

    /**
 * Created by rmpestano on 18/02/17.
 * A marker exception to redirect user to 403.xhtml. See web-fragment.xml
 */
@ApplicationException(rollback = true)
public class AccessDeniedException  extends RuntimeException implements Serializable {

	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
    	}

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param msg exception message
     */
    public AccessDeniedException(String msg) {
        super(msg);
    }
    
}
