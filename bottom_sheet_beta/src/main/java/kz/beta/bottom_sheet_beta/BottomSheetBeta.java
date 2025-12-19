package kz.beta.bottom_sheet_beta;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetBeta extends BottomSheetDialogFragment {

    private static List<ActionItem> actions = new ArrayList<>();
    private static Runnable cancelAction;
    private static String cancelText = "Cancel";
    private static String infoText = "";
    private static int white_level_percent = 33;

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            int white_level,
            String info,
            @Nullable Runnable onCancel,
            @Nullable String cancelBtnText
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        white_level_percent = white_level;
        infoText = info;
        cancelAction = onCancel;
        if (cancelBtnText != null) cancelText = cancelBtnText;
        return f;
    }

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            int white_level,
            String info,
            @Nullable Runnable onCancel
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        white_level_percent = white_level;
        infoText = info;
        cancelAction = onCancel;
        return f;
    }

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            int white_level,
            @Nullable Runnable onCancel,
            @Nullable String cancelBtnText
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        white_level_percent = white_level;
        cancelAction = onCancel;
        if (cancelBtnText != null) cancelText = cancelBtnText;
        return f;
    }

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            @Nullable Runnable onCancel,
            @Nullable String cancelBtnText
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        cancelAction = onCancel;
        if (cancelBtnText != null) cancelText = cancelBtnText;
        return f;
    }

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            int white_level,
            @Nullable Runnable onCancel
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        white_level_percent = white_level;
        cancelAction = onCancel;
        return f;
    }

    public static BottomSheetBeta newInstance(
            List<ActionItem> actionItems,
            @Nullable Runnable onCancel
    ) {
        BottomSheetBeta f = new BottomSheetBeta();
        actions = actionItems;
        cancelAction = onCancel;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View v = createContentView();

        LinearLayout containerActions = v.findViewById(R.id.actionsContainer);
        TextView cancel = v.findViewById(R.id.btnCancel);
        View white_level = v.findViewById(R.id.white_level);

        int alpha = Math.min(255, Math.max(0,
                255 * white_level_percent / 100
        ));
        int color = Color.argb(
                alpha, 0xF2,  0xF2,  0xF2
        );
        white_level.setBackgroundColor(color);

        if(infoText != null && !infoText.isEmpty()){
            TextView info_text = new TextView(requireContext());
            info_text.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            info_text.setPadding(0, 30, 0, 30);
            info_text.setGravity(Gravity.CENTER);
            info_text.setTextSize(14);
            info_text.setTextColor(0xE6000000);
            info_text.setText(infoText);
            containerActions.addView(info_text);
        }


        for (ActionItem item : actions) {
            TextView btn = new TextView(requireContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            btn.setPadding(0, 40, 0, 40);
            btn.setGravity(Gravity.CENTER);
            btn.setTextSize(18);
            btn.setTextColor(
                    item.isPositive
                            ? Color.parseColor("#007AFF")
                            : Color.parseColor("#FF0000")
            );
            btn.setText(item.text);

            btn.setOnClickListener(v1 -> {
                dismiss();
                if (item.action != null) item.action.run();
            });

            containerActions.addView(btn);
        }

        cancel.setText(cancelText);
        cancel.setOnClickListener(v1 -> {
            dismiss();
            if (cancelAction != null) cancelAction.run();
        });

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(d -> {

            FrameLayout glass = dialog.findViewById(R.id.cardView);
            FrameLayout blurLayer = dialog.findViewById(R.id.blurLayer);

            View root = requireActivity()
                    .getWindow()
                    .getDecorView()
                    .getRootView();

            Bitmap screen = takeScreenshot(root);

            int[] loc = new int[2];
            assert glass != null;
            glass.getLocationOnScreen(loc);

            Bitmap cropped = Bitmap.createBitmap(
                    screen,
                    loc[0],
                    loc[1],
                    glass.getWidth(),
                    glass.getHeight()
            );

            assert blurLayer != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                applyBlur(blurLayer, cropped);
            } else {
                blurLayer.setBackgroundColor(Color.parseColor("#E6FFFFFF"));
            }


            View sheet = dialog.findViewById(
                    com.google.android.material.R.id.design_bottom_sheet
            );
            if (sheet != null) {
                sheet.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        return dialog;
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private void applyBlur(FrameLayout blurLayer, Bitmap cropped) {

        ImageView blurView = new ImageView(requireContext());
        blurView.setScaleType(ImageView.ScaleType.FIT_XY);
        blurView.setImageBitmap(cropped);

        blurView.setRenderEffect(
                RenderEffect.createBlurEffect(
                        30f, 30f, Shader.TileMode.CLAMP
                )
        );

        blurLayer.removeAllViews();
        blurLayer.addView(blurView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        View overlay = new View(requireContext());
        overlay.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        blurLayer.addView(overlay, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    private Bitmap takeScreenshot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public int getTheme() {
        return R.style.TransparentBottomSheetDialog;
    }
    private int dp(int value) {
        return (int) (value * requireContext().getResources().getDisplayMetrics().density);
    }
    private android.graphics.drawable.Drawable whiteRoundedBackground() {
        android.graphics.drawable.GradientDrawable d =
                new android.graphics.drawable.GradientDrawable();
        d.setColor(Color.WHITE);
        d.setCornerRadius(dp(12));
        return d;
    }
    private View createContentView() {

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        int padding = dp(20);
        root.setPadding(padding, padding, padding, padding);

        androidx.constraintlayout.widget.ConstraintLayout constraintLayout =
                new androidx.constraintlayout.widget.ConstraintLayout(requireContext());
        constraintLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        root.addView(constraintLayout);

        androidx.cardview.widget.CardView cardView =
                new androidx.cardview.widget.CardView(requireContext());
        cardView.setId(R.id.cardView);
        cardView.setRadius(dp(12));
        cardView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        constraintLayout.addView(cardView);

        FrameLayout blurLayer = new FrameLayout(requireContext());
        blurLayer.setId(R.id.blurLayer);
        blurLayer.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        cardView.addView(blurLayer);

        View whiteLevel = new View(requireContext());
        whiteLevel.setId(R.id.white_level);
        whiteLevel.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        cardView.addView(whiteLevel);

        LinearLayout actionsContainer = new LinearLayout(requireContext());
        actionsContainer.setId(R.id.actionsContainer);
        actionsContainer.setOrientation(LinearLayout.VERTICAL);
        actionsContainer.setTranslationZ(dp(5));
        actionsContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        actionsContainer.setDividerDrawable(
                new android.graphics.drawable.ColorDrawable(
                        Color.parseColor("#C7C7C7")
                )
        );
        actionsContainer.setDividerPadding(dp(1));
        actionsContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        constraintLayout.addView(actionsContainer);

        androidx.constraintlayout.widget.ConstraintSet set =
                new androidx.constraintlayout.widget.ConstraintSet();
        set.clone(constraintLayout);

        set.connect(cardView.getId(),
                androidx.constraintlayout.widget.ConstraintSet.TOP,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.TOP);

        set.connect(cardView.getId(),
                androidx.constraintlayout.widget.ConstraintSet.BOTTOM,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.BOTTOM);

        set.connect(cardView.getId(),
                androidx.constraintlayout.widget.ConstraintSet.START,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.START);

        set.connect(cardView.getId(),
                androidx.constraintlayout.widget.ConstraintSet.END,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.END);

        set.connect(actionsContainer.getId(),
                androidx.constraintlayout.widget.ConstraintSet.TOP,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.TOP);

        set.connect(actionsContainer.getId(),
                androidx.constraintlayout.widget.ConstraintSet.BOTTOM,
                androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                androidx.constraintlayout.widget.ConstraintSet.BOTTOM);

        set.applyTo(constraintLayout);

        TextView cancelBtn = new TextView(requireContext());
        cancelBtn.setId(R.id.btnCancel);
        cancelBtn.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) cancelBtn.getLayoutParams()).topMargin = dp(8);
        cancelBtn.setPadding(0, dp(16), 0, dp(16));
        cancelBtn.setGravity(Gravity.CENTER);
        cancelBtn.setTextSize(18);
        cancelBtn.setTextColor(Color.parseColor("#007AFF"));
        cancelBtn.setBackground(whiteRoundedBackground());


        root.addView(cancelBtn);

        return root;
    }

    public static class ActionItem {
        public String text;
        public Runnable action;
        public boolean isPositive;

        public ActionItem(String text, Runnable action, boolean isPositive) {
            this.text = text;
            this.action = action;
            this.isPositive = isPositive;
        }
    }

}
