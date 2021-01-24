package com.example.infinityOptics;// Compare to examples/roc_example_flatten.c

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import io.rankone.rocsdk.*;

public class roc_get_template {
    static {
        System.loadLibrary("_roc");
    }

    static double roc_spoofAF;

    public static double getRoc_spoofAF() {
        return roc_spoofAF;
    }

    public static String readAssetFile(AssetManager assetManager, String fileName) {
        try {
            InputStream is = assetManager.open("ROC.lic");
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            return new String(buffer);

        } catch (IOException ioe) {
            return new String();
        }
    }

    public static void init(AssetManager assetManager) {
        String license = readAssetFile(assetManager, "ROC.lic");
        // Initialize SDK
        roc.roc_ensure(roc.roc_initialize(license, null));
    }

    public static String main(String fileName) {
        // Open the image
        roc_image image = new roc_image();
        roc.roc_ensure(roc.roc_read_image(fileName, roc_color_space.ROC_GRAY8, image));

        // Find and represent one face in the image
        roc_template template_src = new roc_template();

        roc.roc_ensure(
                roc.roc_represent(image,
                        (long) (roc_algorithm_options.ROC_FRONTAL_DETECTION |
                                roc_algorithm_options.ROC_STANDARD_REPRESENTATION |
                                roc_algorithm_options.ROC_ANALYTICS |
                                roc_algorithm_options.ROC_SPOOF_AF),
                        roc.ROC_SUGGESTED_ABSOLUTE_MIN_SIZE, 1, roc.ROC_SUGGESTED_FALSE_DETECTION_RATE, roc.ROC_SUGGESTED_MIN_QUALITY, template_src));

        if (template_src.getAlgorithm_id() == 0)
            roc.roc_ensure("Failed to detect face in image: " + fileName);

/*
        // For debug purpose

        SWIGTYPE_p_double age = roc.new_double();

        roc.roc_ensure(roc.roc_get_metadata_double(template_src, "Age", age));

        int roc_age = (int) roc.double_value(age);

        SWIGTYPE_p_p_char gender = roc.new_roc_string();

        roc.roc_ensure(roc.roc_get_metadata(template_src, "Gender", gender));

        String roc_gender = roc.roc_string_value(gender);
*/

        SWIGTYPE_p_p_char fv = roc.new_roc_string();

        roc.roc_ensure(roc.roc_get_metadata(template_src, "FV", fv));

        String roc_fv = roc.roc_string_value(fv);


        SWIGTYPE_p_double spoofAF = roc.new_double();

        roc.roc_ensure(roc.roc_get_metadata_double(template_src, "SpoofAF", spoofAF));

        roc_spoofAF = roc.double_value(spoofAF);

        // Cleanup
        roc.roc_ensure(roc.roc_free_image(image));
        roc.roc_ensure(roc.roc_free_template(template_src));

        roc.roc_ensure(roc.roc_free_string(fv));
        roc.delete_roc_string(fv);

        roc.delete_double(spoofAF);
        return roc_fv;
    }

    public static void deinit() {
        roc.roc_ensure(roc.roc_finalize());
    }
}
