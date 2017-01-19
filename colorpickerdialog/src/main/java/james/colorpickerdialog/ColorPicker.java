package james.colorpickerdialog;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends Application {

    private List<OnActivityResultListener> onActivityResultListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        onActivityResultListeners = new ArrayList<>();
    }

    public void addListener(OnActivityResultListener listener) {
        onActivityResultListeners.add(listener);
    }

    public void removeListener(OnActivityResultListener listener) {
        onActivityResultListeners.remove(listener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (OnActivityResultListener listener : onActivityResultListeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
