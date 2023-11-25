package com.host;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.host.utils.DispatchUtils;
import com.host.utils.ServerEntity;
import com.host.utils.ZipUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String URL = "http://192.168.3.58:8080";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        linearLayout = findViewById(R.id.container);
        getHome();
    }

    private void getHome() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL + "/home").get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "error", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                List<ServerEntity.Bundle> bundles = new Gson().fromJson(response.body().string(), new TypeToken<List<ServerEntity.Bundle>>() {
                }.getType());
                for (ServerEntity.Bundle bundle : bundles) {
                    String bundleName = bundle.getName();
                    runOnUiThread(() -> {
                        Button button = new Button(MainActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 10, 10, 10);
                        button.setLayoutParams(params);
                        button.setText(bundle.getDesc());
                        button.setTextColor(Color.WHITE);
                        button.setBackgroundResource(R.drawable.bg);
                        button.setOnClickListener(v -> {
                            String f = MainActivity.this.getFilesDir().getAbsolutePath() + "/" + bundleName + "/" + bundleName + ".bundle";
                            File file = new File((f));
                            if (file.exists()) {
                                Log.d(TAG, String.format("[%s]存在，无需下载", bundleName));
                                startDispatchActivity(bundleName);
                            } else {
                                Log.d(TAG, String.format("[%s]不存在，需下载", bundleName));
                                download(bundleName);
                            }
                        });
                        linearLayout.addView(button);
                    });
                }
            }
        });
    }

    private void download(final String bundleName) {
        FileDownloader.setup(this);
        FileDownloader.getImpl().create(URL + "/download/bundle/" + bundleName).setPath(this.getFilesDir().getAbsolutePath(), true).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                try {
                    ZipUtils.unzip(MainActivity.this.getFilesDir().getAbsolutePath() + "/" + bundleName + ".zip", MainActivity.this.getFilesDir().getAbsolutePath());
                    startDispatchActivity(bundleName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.e(TAG, "error", e);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
            }
        }).start();
    }

    private void startDispatchActivity(String bundleName) {
        Log.d(TAG, String.format("启动[%s]", bundleName));
        DispatchUtils.dispatchModel = bundleName;
        DispatchActivity.start(MainActivity.this);
    }
}
