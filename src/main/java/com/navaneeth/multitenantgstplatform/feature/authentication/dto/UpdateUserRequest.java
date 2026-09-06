package com.navaneeth.multitenantgstplatform.feature.authentication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String name;
    private String username;
    private String email;

}
