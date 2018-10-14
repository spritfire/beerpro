package ch.beerpro.data.repositories;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

public class FridgeRepository {

    public static LiveData<List<FridgeItem>> getMyFridge(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .orderBy(FridgeItem.FIELD_AMOUNT, Query.Direction.ASCENDING).whereEqualTo(FridgeItem.FIELD_USER_ID, userId),
                FridgeItem.class);
    }


    public Task<Void> toggleUserFridgeItem(String userId, String itemId) {
        DocumentReference fridgeEntryQuery = getFridgeItemEntry(userId, itemId);
        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return fridgeEntryQuery.delete();
            } else if (task.isSuccessful()) {
                return fridgeEntryQuery.set(new FridgeItem(userId, itemId, 1));
            } else {
                throw task.getException();
            }
        });
    }

    public void increaseFridgeItemAmount(String userId, String itemId) {
        DocumentReference fridgeEntryQuery = getFridgeItemEntry(userId, itemId);
        fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                FridgeItem fridgeItem = fridgeEntryQuery.get().getResult().toObject(FridgeItem.class);
                int newAmount = fridgeItem.getAmount() + 1;
                fridgeEntryQuery.update(FridgeItem.FIELD_AMOUNT, newAmount);
                return fridgeEntryQuery.delete();
            } else {
                throw task.getException();
            }
        });
    }

    public void decreaseFridgeItemAmount(String userId, String itemId) {
        DocumentReference fridgeEntryQuery = getFridgeItemEntry(userId, itemId);
        fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                FridgeItem fridgeItem = fridgeEntryQuery.get().getResult().toObject(FridgeItem.class);
                int newAmount = fridgeItem.getAmount() - 1;
                if (newAmount > 0) {
                    fridgeEntryQuery.update(FridgeItem.FIELD_AMOUNT, newAmount);
                } else {
                    fridgeEntryQuery.delete();
                }
                return null;
            } else {
                throw task.getException();
            }
        });
    }

    private DocumentReference getFridgeItemEntry(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String fridgeItemId = FridgeItem.generateId(userId, itemId);
        return db.collection(FridgeItem.COLLECTION).document(fridgeItemId);
    }

    public static LiveData<FridgeItem> getMyBeerFromFridge(String userId, String itemId) {
        LiveData<List<FridgeItem>> list = new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .orderBy(FridgeItem.FIELD_AMOUNT, Query.Direction.ASCENDING).whereEqualTo(FridgeItem.FIELD_USER_ID, userId)
                .whereEqualTo(FridgeItem.FIELD_BEER_ID, itemId), FridgeItem.class);
        return (LiveData<FridgeItem>) list.getValue();
    }
}
