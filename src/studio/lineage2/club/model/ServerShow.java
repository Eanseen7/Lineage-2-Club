package studio.lineage2.club.model;

import lombok.Data;

/**
 Obi-Wan
 24.07.2016
 */
public @Data class ServerShow
{
	private long id;
	private String domain;
	private String description;
	private String version;
	private int rate;
	private String date;
	private boolean partner;
	private boolean vip;
}