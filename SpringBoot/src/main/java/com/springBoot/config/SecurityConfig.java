package com.springBoot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springBoot.security.CustomUserDetailsService;
import com.springBoot.security.JwtAuthenticationEntryPoint;
import com.springBoot.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
      
    	log.info("Inside configure authenticationManagerBuilder");
    	
    	authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	
    	log.info("Inside authenticationManagerBean");
    	return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    	log.info("Inside passwordEncoder");
    	return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      
    	log.info("Inside configure HttpSecurity");
    	http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                   .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   .and() .authorizeRequests()
                   .antMatchers("/",
                		   		"/signin",
		                        "/favicon.ico",
		                        "/**/*.png",
		                        "/**/*.gif",
		                        "/**/*.svg",
		                        "/**/*.jpg",
		                        "/**/*.html",
		                        "/**/*.css",
		                        "/**/*.js").permitAll()
                   .antMatchers("/api/auth/**").permitAll()
                   .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability").permitAll()
                   .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**").permitAll()
                   //.antMatchers("/**").permitAll()
                   .anyRequest().authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
