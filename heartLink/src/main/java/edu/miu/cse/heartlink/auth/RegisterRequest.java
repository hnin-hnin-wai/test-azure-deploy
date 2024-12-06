package edu.miu.cse.heartlink.auth;

import edu.miu.cse.heartlink.constant.Role;

public record RegisterRequest(
        String firstName,
        String lastName,
        String userName,
        String password,
        Role role
) {
}
