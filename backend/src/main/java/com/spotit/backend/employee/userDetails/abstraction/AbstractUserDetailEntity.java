package com.spotit.backend.employee.userDetails.abstraction;

import static jakarta.persistence.FetchType.LAZY;

import com.spotit.backend.employee.abstraction.AbstractEntity;
import com.spotit.backend.employee.userAccount.UserAccount;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractUserDetailEntity extends AbstractEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
