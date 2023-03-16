package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PackageOps implements Parcelable {
    private String mPackageName;
    private int mUid;
    private List<OpEntry> mEntries = new ArrayList<>();

    public PackageOps(AppOpsManager$PackageOps pkgOps) {
        mPackageName = pkgOps.getPackageName();
        mUid = pkgOps.getUid();
        for (AppOpsManager$OpEntry opEntry : pkgOps.getOps()) {
            mEntries.add(new OpEntry(opEntry));
        }
    }

    protected PackageOps(Parcel in) {
        mPackageName = in.readString();
        mUid = in.readInt();
        mEntries = in.createTypedArrayList(OpEntry.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPackageName);
        dest.writeInt(mUid);
        dest.writeTypedList(mEntries);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageOps> CREATOR = new Creator<PackageOps>() {
        @Override
        public PackageOps createFromParcel(Parcel in) {
            return new PackageOps(in);
        }

        @Override
        public PackageOps[] newArray(int size) {
            return new PackageOps[size];
        }
    };

    public @NonNull String getPackageName() {
        return mPackageName;
    }

    public int getUid() {
        return mUid;
    }

    public @NonNull List<OpEntry> getOps() {
        return mEntries;
    }
}
