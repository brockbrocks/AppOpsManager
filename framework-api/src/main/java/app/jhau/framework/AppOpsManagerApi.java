package app.jhau.framework;

import android.app.AppOpsManager;
import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.internal.app.IAppOpsService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppOpsManagerApi {

    private static IAppOpsService mService = IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));

    public static List<AppOpsManagerApi.PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        List<AppOpsManagerApi.PackageOps> packageOpsList = new ArrayList<>();
        for (AppOpsManager$PackageOps packageOps : mService.getOpsForPackage(uid, packageName, ops)) {
            packageOpsList.add(new AppOpsManagerApi.PackageOps(packageOps));
        }
        Log.i("tttt", "AppOpsManagerApi.getOpsForPackage: size="+packageOpsList.size());
        return packageOpsList;
    }

    public static final int MODE_ALLOWED = 0;

    public static final int MODE_IGNORED = 1;

    public static final int MODE_ERRORED = 2;

    public static final int MODE_DEFAULT = 3;

    public static final String[] MODE_NAMES = new String[] {
            "allow",        // MODE_ALLOWED
            "ignore",       // MODE_IGNORED
            "deny",         // MODE_ERRORED
            "default",      // MODE_DEFAULT
            "foreground",   // MODE_FOREGROUND
    };

    public static int _NUM_OP;
    public static String[] sOpNames;
    public static String[] sOpPerms;
    public static String[] sOpRestrictions;
    public static HashMap<String, Integer> sOpStrToOp;
    public static String[] sOpToString;
    public static int[] sOpToSwitch;
    public static HashMap<String, Integer> sPermToOp;

    private static Method opToName;
    private static Method opToSwitch;

    public static String opToName(int op) {
        try {
            opToName.setAccessible(true);
            return (String) opToName.invoke(null, op);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final class PackageOps implements Parcelable {
        private final String mPackageName;
        private final int mUid;
        private final List<OpEntry> mEntries;

        public PackageOps(AppOpsManager$PackageOps packageOps) {
            mPackageName = packageOps.getPackageName();
            mUid = packageOps.getUid();
            List<OpEntry> opEntries = new ArrayList<>();
            for (AppOpsManager$OpEntry opEntry : packageOps.getOps()) {
                opEntries.add(new OpEntry(opEntry));
            }
            mEntries = opEntries;
        }

        public PackageOps(String packageName, int uid, List<OpEntry> entries) {
            mPackageName = packageName;
            mUid = uid;
            mEntries = entries;
        }

        public @NonNull String getPackageName() {
            return mPackageName;
        }

        public int getUid() {
            return mUid;
        }

        public @NonNull List<OpEntry> getOps() {
            return mEntries;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(mPackageName);
            dest.writeInt(mUid);
            dest.writeInt(mEntries.size());
            for (int i = 0; i < mEntries.size(); i++) {
                mEntries.get(i).writeToParcel(dest, flags);
            }
        }

        PackageOps(Parcel source) {
            mPackageName = source.readString();
            mUid = source.readInt();
            mEntries = new ArrayList<>();
            final int N = source.readInt();
            for (int i = 0; i < N; i++) {
                mEntries.add(OpEntry.CREATOR.createFromParcel(source));
            }
        }

        public static final @NonNull Parcelable.Creator<PackageOps> CREATOR = new Creator<PackageOps>() {
            @Override
            public PackageOps createFromParcel(Parcel source) {
                return new PackageOps(source);
            }

            @Override
            public PackageOps[] newArray(int size) {
                return new PackageOps[size];
            }
        };
    }

    public static final class OpEntry implements Parcelable {
        private final int mOp;
        private final int mMode;
        private final long mTime;
        private final long mRejectTime;
        private final int mDuration;
        private final int mProxyUid;
        private final String mProxyPackageName;

        protected OpEntry(Parcel in) {
            mOp = in.readInt();
            mMode = in.readInt();
            mTime = in.readLong();
            mRejectTime = in.readLong();
            mDuration = in.readInt();
            mProxyUid = in.readInt();
            mProxyPackageName = in.readString();
        }

        public OpEntry(AppOpsManager$OpEntry opEntry) {
            mOp = opEntry.getOp();
            mMode = opEntry.getMode();
            mTime = opEntry.getTime();
            mRejectTime = opEntry.getRejectTime();
            mDuration = opEntry.getDuration();
            mProxyUid = opEntry.getProxyUid();
            mProxyPackageName = opEntry.getProxyPackageName();
        }


        public static final Creator<OpEntry> CREATOR = new Creator<OpEntry>() {
            @Override
            public OpEntry createFromParcel(Parcel in) {
                return new OpEntry(in);
            }

            @Override
            public OpEntry[] newArray(int size) {
                return new OpEntry[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(mOp);
            dest.writeInt(mMode);
            dest.writeLong(mTime);
            dest.writeLong(mRejectTime);
            dest.writeInt(mDuration);
            dest.writeInt(mProxyUid);
            dest.writeString(mProxyPackageName);
        }
    }

    static {
        try {
            for (Field field : AppOpsManager.class.getDeclaredFields()) {
                String fieldName = field.getName();
                field.setAccessible(true);
                if (fieldName.equals("_NUM_OP")) {
                    _NUM_OP = (int) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpNames")) {
                    sOpNames = (String[]) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpPerms")) {
                    sOpPerms = (String[]) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpRestrictions")) {
                    sOpRestrictions = (String[]) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpStrToOp")) {
                    sOpStrToOp = (HashMap<String, Integer>) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpToString")) {
                    sOpToString = (String[]) field.get(null);
                    continue;
                }
                if (fieldName.equals("sOpToSwitch")) {
                    sOpToSwitch = (int[]) field.get(null);
                }
            }
            for (Method method : AppOpsManager.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if (methodName.equals("opToName")) {
                    opToName = method;
                    continue;
                }
                if (methodName.equals("opToSwitch")) {
                    opToSwitch = method;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
