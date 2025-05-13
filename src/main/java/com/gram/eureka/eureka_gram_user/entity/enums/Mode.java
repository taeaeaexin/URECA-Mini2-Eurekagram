package com.gram.eureka.eureka_gram_user.entity.enums;

import lombok.Getter;

@Getter
public enum Mode {
    OFFLINE("대면"),
    ONLINE("비대면");

    private final String label;

    Mode(String label) {
        this.label = label;
    }

    public Mode[] getAllMode() {
        return Mode.values();
    }
}
