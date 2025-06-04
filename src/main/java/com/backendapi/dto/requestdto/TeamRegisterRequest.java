package com.backendapi.dto.requestdto;

import lombok.Data;

import java.util.List;

@Data
public class TeamRegisterRequest {
    private String teamName;
    private List<Long> teamMembers;
}
