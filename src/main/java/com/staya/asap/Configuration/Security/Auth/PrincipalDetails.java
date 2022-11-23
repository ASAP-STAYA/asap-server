package com.staya.asap.Configuration.Security.Auth;

// Security가 /api/auth/sigin 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 security session을 만들어준다. (Security ContextHolder)
// 오브젝트 -> Authentication 타입의 객체
// Authentication 안에 User 정보가 있어야 됨
// User 오브젝트의 타입은 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

import com.staya.asap.Model.DB.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private UserDTO user; // composition

    public PrincipalDetails(UserDTO user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    public Integer getId(){ return user.getId();}
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트!! 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 함
        return true;
    }
}
