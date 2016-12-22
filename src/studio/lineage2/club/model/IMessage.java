package studio.lineage2.club.model;

import lombok.Getter;

/**
 Obi-Wan
 26.10.2015
 */
public class IMessage
{
	private @Getter Type type;
	private @Getter String message;

	public IMessage(Type type)
	{
		this.type = type;
		this.message = "";
	}

	public IMessage(Type type, String message)
	{
		this.type = type;
		this.message = message;
	}

	public enum Type
	{
		SUCCESS,
		FAIL
	}
}