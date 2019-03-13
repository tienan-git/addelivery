package jp.acepro.haishinsan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * security不要なリソースを指定する。
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/fonts/**", "/api/**", "/heartBeat");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/loginSuccess");

		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login").permitAll();

		http.authorizeRequests().anyRequest().authenticated();

		// csrf有効
        //http.csrf().disable();
	}

	/**
	 * カスタマイズサービスを指定する。
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);//.passwordEncoder(passwordEncoder());
	}

	// 暗号化
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
