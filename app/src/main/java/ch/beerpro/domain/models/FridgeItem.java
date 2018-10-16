package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FridgeItem implements Entity {
    public static final String COLLECTION = "fridgeitems";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_AMOUNT = "amount";

    /**
     * The id is formed by `$userId_$beerId` to make queries easier.
     */
    @Exclude
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private String beerId;

    private int amount;

    public FridgeItem(String userId, String beerId, int amount) {
        this.userId = userId;
        this.beerId = beerId;
        this.amount = amount;
    }

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getBeerId() { return beerId; }

    public void setBeerId(String beerId) { this.beerId = beerId; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }
}
