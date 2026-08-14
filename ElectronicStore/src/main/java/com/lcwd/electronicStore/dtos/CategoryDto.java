package com.lcwd.electronicStore.dtos;


import com.lcwd.electronicStore.validate.ImageNameValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private String categoryId;
  @NotBlank(message = "tttle is required")
   @Size(min=4, message = "category title required more than 4 char!!")
    private String title;
   @NotBlank(message = "description required!!")
    private String description;
   @ImageNameValid
    private String coverImage;
}
