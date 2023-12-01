package com.spotit.backend.userAccount.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.userAccount.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findUserAccountByAuth0Id(String auth0Id);
}
