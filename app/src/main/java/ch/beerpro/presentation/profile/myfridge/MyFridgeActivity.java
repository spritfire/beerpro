package ch.beerpro.presentation.profile.myfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import ch.beerpro.R;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.FridgeItem;

import android.os.Bundle;

import java.util.List;

public class MyFridgeActivity extends AppCompatActivity implements CurrentUser {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);
    }


}
