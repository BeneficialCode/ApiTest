package com.forensic.apitest;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "API_TEST";

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.println(Log.VERBOSE, TAG, "Hello World!");
        try {
            PackageManager pm = getPackageManager();

            List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
            for (PackageInfo packageInfo : packages) {
                Log.d("InstalledPackages", "Package name: " + packageInfo.packageName);
                Log.d("InstalledPackages", "Version name: " + packageInfo.versionName);
                Log.d("InstalledPackages", "Version code: " + packageInfo.versionCode);
                if(packageInfo.packageName.contains("com.baidu"))
                {
                    String packageName = packageInfo.packageName;
                    Signature[] signatures = packageInfo.signatures;
                    if (signatures != null && signatures.length > 0) {
                        Signature signature = signatures[0]; // 只获取第一个签名信息
                        byte[] signatureBytes = signature.toByteArray();

                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        md5.update(signatureBytes);
                        byte[] digest = md5.digest();

                        // 将字节数组转换为十六进制字符串
                        StringBuilder sb = new StringBuilder();
                        for (byte b : digest) {
                            sb.append(String.format("%02x", b & 0xff));
                        }

                        String md5Signature = sb.toString();
                        Log.d("Signature", "MD5 Signature for " + packageName + ": " + md5Signature);
                    } else {
                        Log.d("Signature", "No signature found for " + packageName);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}