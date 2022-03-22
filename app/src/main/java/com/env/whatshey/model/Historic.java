package com.env.whatshey.model;

public class Historic {
    private String id;
    private String number;
    private long time;

    private Historic(Builder builder){
        this.id = builder.id;
        this.number = builder.number;
        this.time = builder.time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static class Builder{
        private String id;
        private String number;
        private long time;

        public Historic build(){
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
    }
}
