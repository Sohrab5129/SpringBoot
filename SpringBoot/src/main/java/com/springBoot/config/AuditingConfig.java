package com.springBoot.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.springBoot.security.UserPrincipal;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	private static final Logger log = LoggerFactory.getLogger(AuditingConfig.class);

	@Bean
	public AuditorAware<Long> auditorProvider() {
		log.info("Inside auditorProvider");
		return new SpringSecurityAuditAwareImpl();
	}

	class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {
		
		@Override
		public Optional<Long> getCurrentAuditor() {
			
			log.info("Inside getCurrentAuditor SpringSecurityAuditAwareImpl");
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()
					|| authentication instanceof AnonymousAuthenticationToken) {
				return Optional.empty();
			}

			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

			return Optional.ofNullable(userPrincipal.getId());
		}
	}
}
