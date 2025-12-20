package kz.beta.bottomsheetactionsbeta;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import kz.beta.bottom_sheet_beta.BottomSheetBeta;


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
                "Сохранить",
                () -> {
                    // действие при нажатии
                },
                true // positive (синий)
        ));

        actions.add(new BottomSheetBeta.ActionItem(
                "Удалить",
                () -> {
                    // опасное действие
                },
                false // negative (красный)
        ));

        BottomSheetBeta sheet = BottomSheetBeta.newInstance(
                actions,
                1,                 // white_level (прозрачность белого слоя)
                "Выберите действие",// infoText (можно "" или null)
                () -> {
                    // onCancel
                },
                "Отмена"            // текст кнопки Cancel
        );

// Показ
        sheet.show(getSupportFragmentManager(), "BottomSheetBeta");


    }
}