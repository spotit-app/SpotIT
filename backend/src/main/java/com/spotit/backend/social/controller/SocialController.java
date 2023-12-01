package com.spotit.backend.social.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.social.dto.ReadSocialDto;
import com.spotit.backend.social.dto.WriteSocialDto;
import com.spotit.backend.social.mapper.SocialMapper;
import com.spotit.backend.social.model.Social;
import com.spotit.backend.social.service.SocialService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/social")
public class SocialController
        extends AbstractUserDetailController<Social, Integer, ReadSocialDto, WriteSocialDto> {

    public SocialController(SocialService socialService, SocialMapper socialMapper) {
        super(socialService, socialMapper);
    }
}
