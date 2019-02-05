package me.jfenn.colorpickerdialog.interfaces;

import android.content.Intent;

public interface ActivityRequestHandler {

    void handlePermissionsRequest(ActivityResultHandler resultHandler, String... permissions);

    void handleActivityRequest(ActivityResultHandler resultHandler, Intent intent);

}
