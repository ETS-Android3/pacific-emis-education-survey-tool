package fm.doe.national.core.utils;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Progress;

public class ViewUtils {

    private static final SparseIntArray STANDARD_ICONS = new SparseIntArray();
    static {
        STANDARD_ICONS.put(1, R.drawable.ic_standard_leadership);
        STANDARD_ICONS.put(2, R.drawable.ic_standard_teacher);
        STANDARD_ICONS.put(3, R.drawable.ic_standard_data);
        STANDARD_ICONS.put(4, R.drawable.ic_standard_cirriculum);
        STANDARD_ICONS.put(5, R.drawable.ic_standard_facility);
        STANDARD_ICONS.put(6, R.drawable.ic_standard_improvement);
        STANDARD_ICONS.put(7, R.drawable.ic_standard_observation);
    }

    public static void rebindProgress(Progress progressData,
                                      @Nullable TextView textView,
                                      @Nullable ProgressBar progressBar) {
        int done = progressData.getCompleted();
        int total = progressData.getTotal();
        int progress = (int) ((float) done / total * 100);

        if (textView != null) {
            textView.setActivated(progress == 100);
            textView.setText(textView.getContext().getString(R.string.criteria_progress, done, total));
        }

        if (progressBar != null) {
            progressBar.setActivated(progress == 100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }
        }
    }

    public static void setScaledDownImageTo(ImageView imageView, String imagePath) {
        int targetW = Constants.SIZE_THUMB_PICTURE;
        int targetH = Constants.SIZE_THUMB_PICTURE;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, bmOptions));
    }

    public static void setImageTo(ImageView imageView, String imagePath) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
    }

    @Nullable
    @DrawableRes
    public static Integer getStandardIconRes(@Nullable String idString) {
        if (idString == null) return null;
        idString = idString.replaceAll("\\D+","");
        try {
            return STANDARD_ICONS.get(Integer.parseInt(idString));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static void setTintedBackgroundDrawable(View targetView, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        Context context = targetView.getContext();
        Drawable backgroundDrawable = ContextCompat.getDrawable(context, drawableRes);

        if (backgroundDrawable != null) {
            backgroundDrawable = DrawableCompat.wrap(backgroundDrawable);
            DrawableCompat.setTint(
                    backgroundDrawable.mutate(),
                    ContextCompat.getColor(context, colorRes)
            );
            targetView.setBackground(backgroundDrawable);
        }
    }

}
