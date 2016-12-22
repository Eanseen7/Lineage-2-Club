package studio.lineage2.club.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 Obi-Wan
 23.07.2016
 */
@Entity @Table(name = "server") public class Server
{
	@Id @GeneratedValue(generator = "increment") @GenericGenerator(name = "increment", strategy = "increment") @Column(name = "id") private @Getter @Setter long id;

	@NotEmpty(message = "Заполните поле") @Size(min = 3, max = 20, message = "Допустимая длинна 3-20 символов") @Column(name = "domain") private @Getter @Setter String domain;

	@Column(name = "description", columnDefinition = "longtext") private @Getter @Setter String description;

	@Column(name = "version") private @Getter @Setter int version;

	@Column(name = "rate") private @Getter @Setter int rate;

	@Temporal(TemporalType.DATE) @Column(name = "date") private @Getter @Setter Date date;

	@Column(name = "active") private @Getter @Setter boolean active;

	@Column(name = "partner") private @Getter @Setter boolean partner;
	@Column(name = "vip") private @Getter @Setter boolean vip;
}