package com.kruk.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kruk.authservice.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKafkaDto {

    private Long id;

    private String name;

    public static UserKafkaDto toKafkaDto(User user) {

        return new UserKafkaDto(
                user.getId(),
                user.getName()
        );

    }





}
