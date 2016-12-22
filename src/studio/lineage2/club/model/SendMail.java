package studio.lineage2.club.model;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 Obi-Wan
 25.06.2016
 */
public @Data class SendMail
{
	@NotEmpty(message = "Заполните поле") @Email(message = "Неверный формат Email") private String email;
	@NotEmpty(message = "Заполните поле") private String content;
}