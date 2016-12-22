package studio.lineage2.club.other;

import org.springframework.security.core.AuthenticationException;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

public class RecaptchaAuthenticationException extends AuthenticationException
{
	private final List<ErrorCode> errorCodes;

	public RecaptchaAuthenticationException(List<ErrorCode> errorCodes)
	{
		super("reCAPTCHA authentication error: " + errorCodes);
		this.errorCodes = errorCodes;
	}

	public RecaptchaAuthenticationException(ErrorCode errorCode)
	{
		this(singletonList(errorCode));
	}

	public List<ErrorCode> getErrorCodes()
	{
		return unmodifiableList(errorCodes);
	}
}