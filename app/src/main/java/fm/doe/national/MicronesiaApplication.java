package fm.doe.national;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import fm.doe.national.di.AppComponent;
import fm.doe.national.di.DaggerAppComponent;
import fm.doe.national.di.modules.ContextModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AppComponent appComponent;
    private WeakReference<Activity> currentActivityRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        registerActivityLifecycleCallbacks(this);

        showDebugDBAddressLogToast(this);
    }

    public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {
            }
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    // region ActivityLifecycleCallbacks
    @Nullable
    public Activity getCurrentActivity() {
        return currentActivityRef.get();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        // nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // nothing
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        // nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivityRef = null;
    }
    // endregion
}
