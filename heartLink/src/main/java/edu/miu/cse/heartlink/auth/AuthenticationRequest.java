package edu.miu.cse.heartlink.auth;

public record AuthenticationRequest(String username,
                                    String password) {
}
