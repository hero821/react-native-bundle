package com.host;

import androidx.annotation.NonNull;

import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultReactNativeHost;

import java.util.List;

import javax.annotation.Nullable;

public class DispatchDelegate extends ReactActivityDelegate {

    private final ReactActivity activity;
    private final String bundleName;

    public DispatchDelegate(ReactActivity activity, @Nullable String mainComponentName) {
        super(activity, mainComponentName);
        this.activity = activity;
        this.bundleName = mainComponentName;
    }

    @Override
    protected ReactNativeHost getReactNativeHost() {
        return new DefaultReactNativeHost(activity.getApplication()) {
            @Override
            public boolean getUseDeveloperSupport() {
                return BuildConfig.DEBUG;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return new PackageList(this).getPackages();
            }

            @Override
            protected String getJSBundleFile() {
                return activity.getFilesDir().getAbsolutePath() + "/" + bundleName + "/" + bundleName + ".bundle";
            }

            @NonNull
            @Override
            protected String getBundleAssetName() {
                return bundleName + ".bundle";
            }

            @Override
            protected String getJSMainModuleName() {
                return "index";
            }
        };
    }
}
