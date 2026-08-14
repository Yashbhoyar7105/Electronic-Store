package com.lcwd.electronicStore.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private String ImageName;
    private String message;
    private boolean sucess;
    private HttpStatus status;
}
