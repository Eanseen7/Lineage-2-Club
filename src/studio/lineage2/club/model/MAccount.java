package studio.lineage2.club.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 Obi-Wan
 27.05.2016
 */
@Entity @Table(name = "master_account") public class MAccount implements UserDetails
{
	@Id @GeneratedValue(generator = "increment") @GenericGenerator(name = "increment", strategy = "increment") @Column(name = "id") private @Getter @Setter long id;

	@NotEmpty(message = "Заполните поле") @Email(message = "Неверный формат Email") @Column(name = "username") private @Setter String username;

	@Size(min = 6, message = "Пароль должен содержать от 6 символов") @Column(name = "password") private @Setter String password;

	@Transient private @Getter @Setter String repeatPassword;

	@Column(name = "admin") private @Getter @Setter boolean admin = false;

	@Override public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if(isAdmin())
		{
			return AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
		}
		return AuthorityUtils.createAuthorityList("ROLE_USER");
	}

	@Override public String getUsername()
	{
		return username;
	}

	@Override public String getPassword()
	{
		return password;
	}

	@Override public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override public boolean isEnabled()
	{
		return true;
	}
}