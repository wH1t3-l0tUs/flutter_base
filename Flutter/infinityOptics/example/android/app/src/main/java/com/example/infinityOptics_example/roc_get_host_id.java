package com.example.infinityOptics_example;// Compare to examples/roc_example_host_id.c

import io.rankone.rocsdk.*;

public class roc_get_host_id {
    static {
        System.loadLibrary("_roc");
    }

    public static String main(String hostId) {

        // Print host id
        SWIGTYPE_p_p_char host_id = roc.new_roc_string();

        roc.roc_ensure(roc.roc_get_host_id(host_id));

       // System.out.println(roc.roc_string_value(host_id));

        String ret = roc.roc_string_value(host_id);

        roc.roc_ensure(roc.roc_free_string(host_id));
        roc.delete_roc_string(host_id);

        return ret;
    }
}
