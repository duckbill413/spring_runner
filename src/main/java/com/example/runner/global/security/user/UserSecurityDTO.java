package com.example.runner.global.security.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Getter
public class UserSecurityDTO extends User implements OAuth2User {
    private final UUID id;
    private final Map<String, Object> props;

    @Builder(builderMethodName = "fromSocial", buildMethodName = "create")
    public UserSecurityDTO(String username,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           Map<String, Object> props) {
        super(username, password, authorities);
        this.props = props;
        this.id = UUID.fromString(username);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return super.getUsername();
    }
}
