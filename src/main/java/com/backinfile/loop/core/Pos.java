package com.backinfile.loop.core;

import java.util.Objects;

public class Pos {
    public int x = 0;
    public int y = 0;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos getTransPos(int dx, int dy) {
        return new Pos(this.x + dx, this.y + dy);
    }

    public Pos getTransPos(Pos dPos) {
        return new Pos(this.x + dPos.x, this.y + dPos.y);
    }

    public void trans(Pos dPos) {
        this.x += dPos.x;
        this.y += dPos.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Pos copy() {
        return new Pos(x, y);
    }

    public Pos getOppsite() {
        return new Pos(-x, -y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Pos) {
            Pos obj = (Pos) other;
            return this.x == obj.x && this.y == obj.y;
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
