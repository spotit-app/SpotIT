package com.spotit.backend.interest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.interest.dto.ReadInterestDto;
import com.spotit.backend.interest.dto.WriteInterestDto;
import com.spotit.backend.interest.mapper.InterestMapper;
import com.spotit.backend.interest.model.Interest;
import com.spotit.backend.interest.service.InterestService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/interest")
public class InterestController
        extends AbstractUserDetailController<Interest, Integer, ReadInterestDto, WriteInterestDto> {

    public InterestController(InterestService interestService, InterestMapper interestMapper) {
        super(interestService, interestMapper);
    }
}
