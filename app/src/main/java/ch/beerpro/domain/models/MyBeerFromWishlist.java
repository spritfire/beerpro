package ch.beerpro.domain.models;

import lombok.Data;

import java.util.Date;

@Data
public class MyBeerFromWishlist implements MyBeer {
    private Wish wish;
    private Beer beer;

    public MyBeerFromWishlist(Wish wish, Beer beer) {
        this.wish = wish;
        this.beer = beer;
    }

    @Override
    public String getBeerId() {
        return wish.getBeerId();
    }

    @Override
    public Date getDate() {
        return wish.getAddedAt();
    }

    @Override
    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
