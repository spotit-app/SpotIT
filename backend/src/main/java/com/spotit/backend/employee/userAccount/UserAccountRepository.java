package com.spotit.backend.employee.userAccount;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findUserAccountByAuth0Id(String auth0Id);
}
