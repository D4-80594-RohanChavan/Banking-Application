package com.app.team2.technotribe.krasvbank.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public class TokenBlacklistRepository {

    private Set<String> blacklist = new HashSet<>();

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean contains(String token) {
        return blacklist.contains(token);
    }
}
