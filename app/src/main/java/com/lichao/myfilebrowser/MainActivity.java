package com.lichao.myfilebrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int FILE_RESULT_CODE = 1;
    private Button btn_open;
    private TextView changePath;
    private String rootPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        btn_open = (Button) findViewById(R.id.btn_open);
        changePath = (TextView) findViewById(R.id.changePath);
    }

    private void initListener() {
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });

        findViewById(R.id.btn_open1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser1();
            }
        });
    }

    private void openBrowser() {
        rootPath = System.getenv("SECONDARY_STORAGE");
        if (rootPath == null) {
            rootPath = Environment.getExternalStorageDirectory().toString();
        }
        if ((rootPath.equals(Environment.getExternalStorageDirectory().toString()))) {
            String filePath = rootPath + "/Android";
            Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
            //根目录
            intent.putExtra("rootPath", rootPath);
            //进去指定文件夹
            intent.putExtra("path", filePath);
            startActivityForResult(intent, FILE_RESULT_CODE);
        }
    }

    private void openBrowser1() {
        rootPath = getSdcardPath();
        if (rootPath == null || rootPath.isEmpty()) {
            rootPath = Environment.getExternalStorageDirectory().toString();
        }
        Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
        intent.putExtra("rootPath", rootPath);
        intent.putExtra("path", rootPath);
        startActivityForResult(intent, FILE_RESULT_CODE);
    }

    public String getSdcardPath() {
        String sdcardPath = "";
        String[] pathArr = null;
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        try {
            Method getVolumePaths = storageManager.getClass().getMethod("getVolumePaths");
            pathArr = (String[]) getVolumePaths.invoke(storageManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (pathArr != null && pathArr.length >= 3) {
            sdcardPath = pathArr[1];
        }
        return sdcardPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FILE_RESULT_CODE == requestCode) {
            Bundle bundle = null;
            if (data != null && (bundle = data.getExtras()) != null) {
                String path = bundle.getString("file","");
                if(!path.isEmpty()){
                    changePath.setText("选择路径为 : " + path);
                }
            }
        }
    }
}
