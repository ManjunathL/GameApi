package com.mygubbi.game.proposal.sow;

import com.mygubbi.game.proposal.model.ProposalSOW;

/**
 * Created by User on 14-07-2017.
 */
public class SpaceRoom {

    private String space;
    private String room;

    public SpaceRoom(ProposalSOW sow) {
        this.space = sow.getSpaceType();
        this.room = sow.getROOM();
    }

    public SpaceRoom(String spaceType, String room)
    {
        this.space = spaceType;
        this.room = room;
    }

    public String getSpace() {
        return space;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceRoom spaceRoom = (SpaceRoom) o;

        if (space != null ? !space.equals(spaceRoom.space) : spaceRoom.space != null) return false;
        return !(room != null ? !room.equals(spaceRoom.room) : spaceRoom.room != null);

    }

    @Override
    public int hashCode() {
        int result = space != null ? space.hashCode() : 0;
        result = 31 * result + (room != null ? room.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpaceRoom{" +
                "space='" + space + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}
