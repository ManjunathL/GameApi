package com.mygubbi.game.proposal.model;

/**
 * Created by User on 17-08-2017.
 */
public class DistinctModule {

    private String space;
    private String room;
    private String product;
    private int productId;
    private int moduleSeq;
    private String module;


    public DistinctModule(ProposalBOQ proposal_boq)
    {
        this.space = proposal_boq.getSpaceType();
        this.room = proposal_boq.getROOM();
        this.product = proposal_boq.getProductService();
        this.productId = proposal_boq.getProductId();
        this.moduleSeq = proposal_boq.getModuleSeq();
        this.module = proposal_boq.getMgCode();

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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getModuleSeq() {
        return moduleSeq;
    }

    public void setModuleSeq(int moduleSeq) {
        this.moduleSeq = moduleSeq;
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

        DistinctModule that = (DistinctModule) o;

        if (productId != that.productId) return false;
        if (moduleSeq != that.moduleSeq) return false;
        if (space != null ? !space.equals(that.space) : that.space != null) return false;
        if (room != null ? !room.equals(that.room) : that.room != null) return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        return !(module != null ? !module.equals(that.module) : that.module != null);

    }

    @Override
    public int hashCode() {
        int result = space != null ? space.hashCode() : 0;
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + productId;
        result = 31 * result + moduleSeq;
        result = 31 * result + (module != null ? module.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DistinctModule{" +
                "space='" + space + '\'' +
                ", room='" + room + '\'' +
                ", product='" + product + '\'' +
                ", productId=" + productId +
                ", moduleSeq=" + moduleSeq +
                ", module='" + module + '\'' +
                '}';
    }
}


