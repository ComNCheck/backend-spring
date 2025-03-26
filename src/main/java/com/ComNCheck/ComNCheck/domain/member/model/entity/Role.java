package com.ComNCheck.ComNCheck.domain.member.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MAJOR_PRESIDENT("ROLE_MAJOR_PRESIDENT"),
    ROLE_STUDENT_COUNCIL("ROLE_STUDENT_COUNCIL"),
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_GRADUATE_STUDENT("ROLE_GRADUATE_STUDENT");

    private final String value;

    Role(String value) {
        this.value = value;
    }
    @JsonCreator
    public Role deserializeRole(String value){
        for(Role role : Role.values()){
            if(role.getValue().equals(value)){
                return role;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 권한 타입입니다.");
    }
    @JsonValue
    public String serializeRole(){
        return this.value;
    }

}
