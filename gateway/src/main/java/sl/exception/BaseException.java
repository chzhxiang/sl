package sl.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 2925961769468414327L;
	private static Logger logger = LoggerFactory.getLogger(BaseException.class);
	private Exception originException;

	public BaseException(Map<String, Object> arguments, Exception e) {
		super(e);
		originException = e;
		StringBuilder stringBuilder = new StringBuilder(generateArgumentErrorMessage(arguments));
		stringBuilder.append(generalExceptionMessage());
		logger.error(stringBuilder.toString());
	}

	public BaseException(Map<String, Object> arguments, String message, Exception e) {
		super(message, e);
		originException = e;
		StringBuilder stringBuilder = new StringBuilder(generateArgumentErrorMessage(arguments));
		stringBuilder.append(generalExceptionMessage());
		logger.error(stringBuilder.toString());
	}

	public BaseException(String message) {
		super(message);
		logger.error(generalExceptionMessage());
	}

	public BaseException(Exception e) {
		super(e);
		originException = e;
		logger.error(generalExceptionMessage());
	}

	public BaseException(String message, Exception e) {
		super(message, e);
		logger.error(generalExceptionMessage());
	}

	protected String generalExceptionMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Exception toString: \n").append(this.toString()).append("\n");
		stringBuilder.append("Exception stack trace: \n");

		if (originException != null) {
			for (StackTraceElement element : originException.getStackTrace()) {
				stringBuilder.append(element.toString()).append("\n");
			}
			if (originException.getCause() != null) {
				stringBuilder.append("Exception cause: \n").append(originException.getCause().toString()).append("\n");
			}
		}

		return stringBuilder.toString();
	}

	private String generateArgumentErrorMessage(Map<String, Object> arguments) {
		StringBuilder stringBuilder = new StringBuilder("arguments:").append("\n");
		for (Map.Entry<String, Object> entry : arguments.entrySet()) {
			stringBuilder.append("key: ").append(entry.getKey());
			stringBuilder.append(" value: ").append(entry.getValue().toString()).append("\n");
		}
		stringBuilder.append("exception message:").append("\n");
		return stringBuilder.toString();
	}
}
