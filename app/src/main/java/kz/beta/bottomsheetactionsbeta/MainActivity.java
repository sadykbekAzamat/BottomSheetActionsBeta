package kz.beta.bottomsheetactionsbeta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import kz.beta.bottom_sheet_beta.BottomSheetBeta;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<BottomSheetBeta.ActionItem> actions = new ArrayList<>();

        actions.add(new BottomSheetBeta.ActionItem(
                "WhatsApp",
                () -> Toast.makeText(MainActivity.this, "WhatsApp", Toast.LENGTH_SHORT).show(),
                true
        ));

        actions.add(new BottomSheetBeta.ActionItem(
                "Позвонить",
                () -> Toast.makeText(MainActivity.this, "Позвонить", Toast.LENGTH_SHORT).show(),
                true
        ));

        actions.add(new BottomSheetBeta.ActionItem(
                "Написать в чат",
                () -> Toast.makeText(MainActivity.this, "Написать в чат", Toast.LENGTH_SHORT).show(),
                false
        ));

        BottomSheetBeta sheet =
                BottomSheetBeta.newInstance(
                        actions,
                        60,
                        "Выберите",
                        () -> Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show(),
                        "Жабу"
                );

        sheet.show(getSupportFragmentManager(), "contact_sheet");


    }
}