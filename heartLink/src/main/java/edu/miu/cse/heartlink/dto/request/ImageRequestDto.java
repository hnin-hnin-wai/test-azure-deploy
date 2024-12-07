package edu.miu.cse.heartlink.dto.request;

import edu.miu.cse.heartlink.model.Image;

import java.util.List;

public record ImageRequestDto(
        String fileType,
        String url,
        Integer itemId,
        byte[] imageBytes,
        String fileName
) {
}
