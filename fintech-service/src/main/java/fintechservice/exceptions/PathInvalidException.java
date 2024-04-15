package fintechservice.exceptions;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;

public class PathInvalidException extends InvalidFileNameException {

	private static final long serialVersionUID = 8831648389876897996L;

	public PathInvalidException(String pName) {
		super(pName, "Invalid file path: ");
	}

}
