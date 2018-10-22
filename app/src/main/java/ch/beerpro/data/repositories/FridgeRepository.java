package ch.beerpro.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {

    private static LiveData<List<FridgeItem>> getFridgeByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .orderBy(FridgeItem.FIELD_AMOUNT, Query.Direction.ASCENDING).whereEqualTo(FridgeItem.FIELD_USER_ID, userId),
                FridgeItem.class);
    }

    private static LiveData<FridgeItem> getUserFridgeFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .document(FridgeItem.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, FridgeItem.class);
    }

    private DocumentReference getFridgeItemEntry(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String fridgeItemId = FridgeItem.generateId(userId, itemId);
        return db.collection(FridgeItem.COLLECTION).document(fridgeItemId);
    }

    public Task<Void> addItemToFridge(String userId, String itemId) {
        DocumentReference fridgeEntryQuery = getFridgeItemEntry(userId, itemId);
        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return null;
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
                return null;
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

    public LiveData<List<Pair<FridgeItem, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                       LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getFridgeByUser(currentUserId.getValue()), map(allBeers, Entity::entitiesById)), input -> {
            List<FridgeItem> fridgeItems = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<FridgeItem, Beer>> result = new ArrayList<>();
            for (FridgeItem fridgeItem : fridgeItems) {
                Beer beer = beersById.get(fridgeItem.getBeerId());
                result.add(Pair.create(fridgeItem, beer));
            }
            return result;
        });
    }

    public LiveData<List<FridgeItem>> getMyFridge(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getFridgeByUser);
    }

    public LiveData<FridgeItem> getMyFridgeItemForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserFridgeFor);
    }
}
