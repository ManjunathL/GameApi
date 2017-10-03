package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ProposalBOQ;

/**
 * Created by User on 17-08-2017.
 */
public class SpaceRoomProduct {

    private String space;
    private String room;
    private int productId;

    public SpaceRoomProduct(ProposalBOQ proposal_boq)
    {
        this.space = proposal_boq.getSpaceType();
        this.room = proposal_boq.getROOM();
        this.productId = proposal_boq.getProductId();
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceRoomProduct that = (SpaceRoomProduct) o;

        if (productId != that.productId) return false;
        if (space != null ? !space.equals(that.space) : that.space != null) return false;
        return !(room != null ? !room.equals(that.room) : that.room != null);

    }

    @Override
    public int hashCode() {
        int result = space != null ? space.hashCode() : 0;
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + productId;
        return result;
    }
}


