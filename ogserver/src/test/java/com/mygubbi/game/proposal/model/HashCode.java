package com.mygubbi.game.proposal.model;

/**
 * Created by User on 04-12-2017.
 */
public class HashCode {

    public String id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashCode hashCode = (HashCode) o;

        if (id != null ? !id.equals(hashCode.id) : hashCode.id != null) return false;
        return !(name != null ? !name.equals(hashCode.name) : hashCode.name != null);

    }

    @Override

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
