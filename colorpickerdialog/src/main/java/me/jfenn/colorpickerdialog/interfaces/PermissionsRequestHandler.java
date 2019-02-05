package me.jfenn.colorpickerdialog.interfaces;

public interface PermissionsRequestHandler {

    void handlePermissionsRequest(PermissionsResultHandler resultHandler, String... permissions);

}
