package studio.lineage2.club.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import studio.lineage2.club.service.MAccountService;

/**
 Obi-Wan
 27.05.2016
 */
@Configuration @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired private MAccountService mAccountService;

	@Override protected void configure(HttpSecurity http) throws Exception
	{
		http.
				csrf().disable().
				authorizeRequests().
				antMatchers("/static/semantic/dist/**", "/static/js/**", "/static/img/**").permitAll().
				antMatchers("/", "/page/**", "/server/add", "/server/show/**", "/api/getServers/**", "/mail/send", "/pay/**", "/top").permitAll().
				antMatchers("/robots.txt", "/sitemap.xml").permitAll().
				antMatchers("/enter/auth", "/enter/reg", "/enter/recover").anonymous().
				antMatchers("/admin/**").hasRole("ADMIN").
				anyRequest().hasRole("USER").and().
				logout().logoutUrl("/enter/logout").logoutSuccessUrl("/");
	}

	@Autowired public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(mAccountService::findByUsername).passwordEncoder(new BCryptPasswordEncoder());
	}
}