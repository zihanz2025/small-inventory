package com.zihan.small_inventory.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class ShopDetails implements UserDetails {

    private final String shopId;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public ShopDetails(String shopId, String password, List<GrantedAuthority> authorities) {
        this.shopId = shopId;
        this.password = password;
        this.authorities = authorities;
    }

    public String getShopId() {
        return shopId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return shopId; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

