package com.detectify.codingchallenge.webdriverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "image")
public class ScreenShotDto {
    private String url;
    private byte[] image;
}
