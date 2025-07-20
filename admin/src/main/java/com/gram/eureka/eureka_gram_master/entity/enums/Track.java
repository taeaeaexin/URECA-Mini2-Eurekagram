package com.gram.eureka.eureka_gram_master.entity.enums;

import lombok.Getter;

@Getter
public enum Track {
    FRONTEND("프론트엔드"),
    BACKEND("백엔드");

    private final String label;

    Track(String label) {
        this.label = label;
    }

    public Track[] getAllTrack() {
        return Track.values();
    }
}
