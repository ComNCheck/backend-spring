package com.ComNCheck.ComNCheck.domain.majorEvent.model.entity.enums;

import lombok.Getter;

@Getter
public enum HostType {
    COMPUTER_SCIENCE("우리 과"),    // 우리 과 (컴퓨터공학부)
    ETC("타 과");  // 타 과

    private final String displayName;

    HostType(String displayName){
        this.displayName = displayName;
    }
}
