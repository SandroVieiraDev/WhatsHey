package com.env.whatshey.model;

import java.util.Objects;

public class Historic {

    public static final int TYPE_CHAT_LEFT = 0;
    public static final int TYPE_CHAT_RIGHT = 1;
    public static final int TYPE_DATE_INDICATOR = 2;

    private String id;
    private int type;
    private String number;
    private long time;

    private Historic(Builder builder) {
        this.id = builder.id;
        this.number = builder.number;
        this.time = builder.time;
        this.type = builder.type;
    }

    public Historic() {
    }

    public Historic(long time, int type) {
        this.time = time;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class Builder {
        private String id;
        private int type;
        private String number;
        private long time;

        public Historic build() {
            return new Historic(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historic historic = (Historic) o;
        return type == historic.type && time == historic.time && id.equals(historic.id) && number.equals(historic.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, number, time);
    }
}
