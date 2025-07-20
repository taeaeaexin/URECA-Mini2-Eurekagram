package com.gram.eureka.eureka_gram_master.entity.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("처리중"),
    ACTIVE("사용가능"),
    INACTIVE("사용불가");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public Status[] getAllStatus() {
        return Status.values();
    }
}
