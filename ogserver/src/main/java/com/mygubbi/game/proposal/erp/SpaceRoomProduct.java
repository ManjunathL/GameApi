package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ProposalBOQ;

/**
 * Created by User on 17-08-2017.
 */
public class SpaceRoomProduct {

    private String space;
    private String room;
    private String product;

    public SpaceRoomProduct(ProposalBOQ proposal_boq)
    {
        this.space = proposal_boq.getSpaceType();
        this.room = proposal_boq.getROOM();
        this.product = proposal_boq.getProductService();
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceRoomProduct that = (SpaceRoomProduct) o;

        if (!space.equals(that.space)) return false;
        if (!room.equals(that.room)) return false;
        return product.equals(that.product);

    }

    @Override
    public int hashCode() {
        int result = space.hashCode();
        result = 31 * result + room.hashCode();
        result = 31 * result + product.hashCode();
        return result;
    }
}


