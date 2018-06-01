//package com.wan.android.util;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.text.TextUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author wzc
// * @date 2018/5/29
// */
//public class MarketUtils {
//
//    /**
//     * 获取已安装应用商店的包名列表
//     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET" />的app
//     * @param context
//     * @return
//     */
//    public ArrayList<String> getInstallAppMarkets(Context context) {
//        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
//        ArrayList<String>  pkgList = new ArrayList<>();
//        pkgList.add("com.xiaomi.market");
//        pkgList.add("com.qihoo.appstore");
//        pkgList.add("com.wandoujia.phoenix2");
//        pkgList.add("com.tencent.android.qqdownloader");
//        pkgList.add("com.taptap");
//        ArrayList<String> pkgs = new ArrayList<String>();
//        if (context == null)
//            return pkgs;
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.APP_MARKET");
//        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
//        if (infos == null || infos.size() == 0)
//            return pkgs;
//        int size = infos.size();
//        for (int i = 0; i < size; i++) {
//            String pkgName = "";
//            try {
//                ActivityInfo activityInfo = infos.get(i).activityInfo;
//                pkgName = activityInfo.packageName;
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (!TextUtils.isEmpty(pkgName))
//                pkgs.add(pkgName);
//
//        }
//        //取两个list并集,去除重复
//        pkgList.removeAll(pkgs);
//        pkgs.addAll(pkgList);
//        return pkgs;
//    }
//
//    /**
//     * 过滤出已经安装的包名集合
//     * @param context
//     * @param pkgs 待过滤包名集合
//     * @return 已安装的包名集合
//     */
//    public ArrayList<String> getFilterInstallMarkets(Context context,ArrayList<String> pkgs) {
//        ArrayList<String> appList = new ArrayList<String>();
//        if (context == null || pkgs == null || pkgs.size() == 0)
//            return appList;
//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
//        int li = installedPkgs.size();
//        int lj = pkgs.size();
//        for (int j = 0; j < lj; j++) {
//            for (int i = 0; i < li; i++) {
//                String installPkg = "";
//                String checkPkg = pkgs.get(j);
//                PackageInfo packageInfo = installedPkgs.get(i);
//                try {
//                    installPkg = packageInfo.packageName;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (TextUtils.isEmpty(installPkg))
//                    continue;
//                if (installPkg.equals(checkPkg)) {
//                    // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
//                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                        //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
//                        AppInfo appInfo = new AppInfo();
//                        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
//                        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
//                        appInfo.setPackageName(packageInfo.packageName);
//                        appInfo.setVersionCode(packageInfo.versionCode);
//                        appInfo.setVersionName(packageInfo.versionName);
//                        appInfos.add(appInfo);
//                        appList.add(installPkg);
//                    }
//                    break;
//                }
//
//            }
//        }
//        return appList;
//    }
//
//    /**
//     * 跳转到应用市场app详情界面
//     * @param appPkg App的包名
//     * @param marketPkg 应用市场包名
//     */
//    public void launchAppDetail(String appPkg, String marketPkg) {
//        try {
//            if (TextUtils.isEmpty(appPkg))
//                return;
//            Uri uri = Uri.parse("market://details?id=" + appPkg);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            if (!TextUtils.isEmpty(marketPkg))
//                intent.setPackage(marketPkg);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
