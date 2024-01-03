package com.spotit.backend.employee.userDetails.interest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/interest")
public class InterestController
        extends AbstractUserDetailController<Interest, Integer, InterestReadDto, InterestWriteDto> {

    public InterestController(InterestService interestService, InterestMapper interestMapper) {
        super(interestService, interestMapper);
    }
}
