package com.pinyougou.user.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 描述
 *
 * @author 三国的包子
 * @version 1.0
 * @package com.pinyougou.user.security *
 * @since 1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //1.只做授权 不做认证,认证交给CAS服务端
        return new User(s,"", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
