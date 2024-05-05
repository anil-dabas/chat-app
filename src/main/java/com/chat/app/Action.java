package com.chat.app;

public enum Action {
    JOIN,LEAVE,MESSAGE;

    public static Action fromActionString(String value) {
        for (Action positionType : Action.values()) {
            if (positionType.name().equalsIgnoreCase(value)) {
                return positionType;
            }
        }
        throw new IllegalArgumentException("Invalid PositionType: " + value);
    }
}
