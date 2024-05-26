package com.spotit.backend.domain.employee.portfolio;

import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccountReadDataDto;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.userAccount.UserAccountReadDataDto;

public interface PortfolioUtils {

    static Portfolio generatePortfolio(Integer id) {
        return Portfolio.builder()
                .portfolioUrl("portfolio-url" + id)
                .build();
    }

    static PortfolioListReadDto createPortfolioListReadDto(Integer id) {
        UserAccountReadDataDto userAccountReadDataDto = createUserAccountReadDataDto(id);

        return PortfolioListReadDto.builder()
                .portfolioUrl("portfolio-url" + id)
                .userData(userAccountReadDataDto)
                .build();
    }

    static String getInvalidUserMessage() {
        return "User account lacks necessary data!";
    }

    static List<Portfolio> generatePortfolioList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(PortfolioUtils::generatePortfolio)
                .toList();
    }
}
