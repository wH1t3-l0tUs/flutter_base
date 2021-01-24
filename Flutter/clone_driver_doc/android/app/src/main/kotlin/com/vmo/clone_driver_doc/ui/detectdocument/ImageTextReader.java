package io.driverdoc.testapp.ui.detectdocument;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

import io.driverdoc.testapp.common.MVVMApplication;
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils;


public class ImageTextReader {
    public static Bitmap getUprightImage(String imgUrl) {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgUrl);
        } catch (IOException ignored) {
        }

        int orientation = 0;
        if (exif != null) {
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        }
        int rotation = 0;
        switch (orientation) {
            case 3:
                rotation = 180;
                break;
            case 6:
                rotation = 90;
                break;
            case 8:
                rotation = 270;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);

        Bitmap bitmap = BitmapFactory.decodeFile(imgUrl);
        //rotate image
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    //resize image to device width
    public static Bitmap resizeImage(Bitmap bitmap, Context ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        return Bitmap.createScaledBitmap(bitmap, width, width, true);
    }

    //read text from image using Firebase ML kit api
    //on-device api
    public static String[] readTextFromImage(Bitmap bitmap, Context context) {
        final String[] text = new String[1];
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                textRecognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                                MVVMApplication.Companion.getListText().add(firebaseVisionText.getText());
                                MVVMApplication.Companion.isDoneDetext().postValue(true);
                                if (MVVMApplication.Companion.isFocusStart()) {
                                    if (firebaseVisionText.getText().toUpperCase().contains("BILL OF LADING")) {
                                        SharedPfPermissionUtils.saveTitleDocument(context, "Bill of Lading");
                                    } else if (firebaseVisionText.getText().toLowerCase().contains("delivery")) {
                                        SharedPfPermissionUtils.saveTitleDocument(context, "Proof of Delivery");
                                    } else {
                                        SharedPfPermissionUtils.saveTitleDocument(context, "Trip Document");
                                    }
                                    MVVMApplication.Companion.setFocusStart(false);
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        SharedPfPermissionUtils.saveTitleDocument(context, "Trip Document");
                                    }
                                });
        return text;

    }
}
