package com.gram.eureka.eureka_gram_master.entity.enums;

import lombok.Getter;

@Getter
public enum Batch {
    FIRST("1기"),
    SECOND("2기"),
    THIRD("3기"),
    FOURTH("4기"),
    FIFTH("5기"),
    SIXTH("6기"),
    SEVENTH("7기"),
    EIGHTH("8기"),
    NINTH("9기"),
    TENTH("10기");

    private final String label;

    Batch(String label) {
        this.label = label;
    }

    public Batch[] getAllBatch() {
        return Batch.values();
    }
}
