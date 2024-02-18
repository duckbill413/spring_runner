package com.example.learner.global.security.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Getter
public class UserSecurityDTO extends User implements OAuth2User {
    private Map<String, Object> props;

    public UserSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }
    public void setAttributes(Map<String, Object> props) {
        this.props = props;
    }

    @Override
    public String getName() {
        return super.getUsername();
    }
}
