package com.hanghae.navis.common.security;

import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(user);
    }
}
