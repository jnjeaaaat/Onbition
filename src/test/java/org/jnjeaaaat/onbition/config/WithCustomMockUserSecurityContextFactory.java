package org.jnjeaaaat.onbition.config;

import java.util.Set;
import org.jnjeaaaat.onbition.domain.dto.auth.UserDetailsImpl;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * security config 와의 의존성을 깨고 test 할때만 사용하게되는 SecurityContext 재설정 class
 */
public class WithCustomMockUserSecurityContextFactory implements
    WithSecurityContextFactory<WithCustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
    String uid = annotation.uid();
    String role = annotation.role();

    UserDetails userDetails = new UserDetailsImpl(User.builder()
        .uid(uid)
        .build());

    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "password",
        Set.of(new SimpleGrantedAuthority(role)));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);

    return context;
  }
}
