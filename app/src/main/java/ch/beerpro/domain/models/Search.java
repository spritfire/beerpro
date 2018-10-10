package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Search implements Entity {
    public static final String COLLECTION = "searches";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private String term;

    private Date creationDate = new Date();

    public Search(String userId, String term) {
        this.userId = userId;
        this.term = term;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
