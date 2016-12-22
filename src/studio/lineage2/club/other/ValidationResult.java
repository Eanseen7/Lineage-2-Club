package studio.lineage2.club.other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@JsonIgnoreProperties(ignoreUnknown = true) public class ValidationResult
{
	public boolean success;
	private List<ErrorCode> errorCodes = new ArrayList<>();

	@JsonCreator public ValidationResult(@JsonProperty("success") boolean success, @JsonProperty("error-codes") List<ErrorCode> errorCodes)
	{
		this.success = success;
		this.errorCodes = errorCodes == null ? new ArrayList<>() : errorCodes;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public List<ErrorCode> getErrorCodes()
	{
		return unmodifiableList(errorCodes);
	}
}