package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private String token;
    private LocalDate date;
    private String name;

    public String getToken() {
        return token;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Config() {

    }

    public Config(String tok, LocalDate d, String na) {
        this.token = tok;
        this.date = d;
        this.name = na;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "config{" +
                "token='" + token + '\'' +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }
}
