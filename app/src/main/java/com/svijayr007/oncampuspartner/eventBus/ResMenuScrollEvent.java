package com.svijayr007.oncampuspartner.eventBus;

public class ResMenuScrollEvent {
    private int pos;

    public ResMenuScrollEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
