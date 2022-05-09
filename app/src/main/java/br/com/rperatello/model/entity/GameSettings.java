package br.com.rperatello.model.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class GameSettings implements Serializable {
    private int round;
    private int level;

    public GameSettings(int level, int round) {
        this.level = level;
        this.round = round;
    }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dificuldade: " + (this.level == 1 ? "Fácil": this.level == 2 ? "Médio": "Difícil") + " | Jogadas: " + this.round;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSettings gameSettings = (GameSettings) o;
        return level == gameSettings.level &&
                round == gameSettings.round;
    }
}

