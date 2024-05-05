package com.chat.app.domainvalue;

public enum Action {
    JOIN,LEAVE,FETCH,DELETE,HISTORY;

    public static Action fromActionString(String value) {
        for (Action positionType : Action.values()) {
            if (positionType.name().equalsIgnoreCase(value)) {
                return positionType;
            }
        }
        throw new IllegalArgumentException("Invalid PositionType: " + value);
    }
}
