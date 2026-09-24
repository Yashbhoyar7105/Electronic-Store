package com.lcwd.electronicStore.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Roles")
public class Role {

    @Id
    private String roleId;
    private String roleName;
}
