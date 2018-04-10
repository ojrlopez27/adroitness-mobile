package edu.cmu.adroitness.client.services.red5streaming.view;

/**
 * Created by sakoju on 7/13/16.
 */
public enum AppState {
    PUBLISH(0);

    final int value;

    private AppState(int num) {
        this.value = num;
    }

    public int getValue() {
        return this.value;
    }

    public static AppState fromValue(int value) {
        for (AppState state : values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return null;
    }
}
