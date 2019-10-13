//package com.wan.android.util;
//
//import android.content.Context;
//
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.wan.android.util.constant.AppConstants;
//
//import timber.log.Timber;
//
///**
// * Bugly 初始化工具类
// *
// * @author wzc
// * @date 2018/8/30
// */
//public class BuglyUtil {
//    private BuglyUtil() {
//        //no instance
//    }
//
//    public static void initBugly(Context context) {
//        if (context == null) {
//            Timber.w("initBugly: context cannot be null!!!");
//            return;
//        }
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = CommonUtils.getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        /*
//         * 参数1: 上下文对象
//         * 参数2: 注册时申请的 APPID
//         * 参数3: 是否开启 debug 模式，true 表示打开 debug 模式，false 表示关闭调试模式
//         */
//        Bugly.init(context.getApplicationContext(), AppConstants.BUGLY_APPID, true, strategy);
//    }
//}
