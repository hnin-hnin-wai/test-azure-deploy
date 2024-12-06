package edu.miu.cse.heartlink.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank String name
) {
}
