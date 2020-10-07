package com.travisit.travisitbusiness.model;

import java.io.Serializable;
import java.util.Comparator;

public class OfferComment implements Serializable {
    Integer id = null;
    String comment = null;
    Integer OfferId = null;
    Integer TravelerId = null;
    Integer businessHelperID = null;

    public OfferComment() {
    }

    public OfferComment(Integer id, String comment, Integer offerId, Integer travelerId, Integer businessHelperID) {
        this.id = id;
        this.comment = comment;
        OfferId = offerId;
        TravelerId = travelerId;
        this.businessHelperID = businessHelperID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getOfferId() {
        return OfferId;
    }

    public void setOfferId(Integer offerId) {
        OfferId = offerId;
    }

    public Integer getTravelerId() {
        return TravelerId;
    }

    public void setTravelerId(Integer travelerId) {
        TravelerId = travelerId;
    }

    public Integer getBusinessHelperID() {
        return businessHelperID;
    }

    public void setBusinessHelperID(Integer businessHelperID) {
        this.businessHelperID = businessHelperID;
    }
}
