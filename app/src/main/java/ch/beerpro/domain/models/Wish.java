package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@IgnoreExtraProperties
@Data
@NoArgsConstructor
public class Wish implements Entity {

    public static final String COLLECTION = "wishes";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_ADDED_AT = "addedAt";

    /**
     * The id is formed by `$userId_$beerId` to make queries easier.
     */
    @Exclude
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private String beerId;
    @NonNull
    private Date addedAt;

    public Wish(String userId, String itemId, Date addedAt) {
        this.userId = userId;
        id = itemId;
        this.addedAt = addedAt;
    }

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBeerId() {
        return beerId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
}
