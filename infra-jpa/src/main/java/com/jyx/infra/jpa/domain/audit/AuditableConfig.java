package com.jyx.infra.jpa.domain.audit;

import com.jyx.infra.exception.AppException;
import com.jyx.infra.jpa.domain.id.Identity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

/**
 * @author jiangyaxin
 * @since 2021/11/5 11:44
 */
@Configuration
@EnableJpaAuditing
public class AuditableConfig {

    @Bean
    @ConditionalOnClass({SecurityContextHolder.class})
    @ConditionalOnMissingBean(AuditorAware.class)
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                throw new AppException("No authorization.");
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof Identity) {
                return Optional.ofNullable(((Identity) principal).getId());
            } else {
                throw new AppException("Principal need extends Identity.");
            }
        };
    }
}
