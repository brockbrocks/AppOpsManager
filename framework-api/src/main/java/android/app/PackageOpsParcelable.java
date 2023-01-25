package android.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PackageOpsParcelable implements Parcelable {
    private List<AppOpsManager$PackageOps> packageOps;

    public PackageOpsParcelable(List<AppOpsManager$PackageOps> packageOps) {
        this.packageOps = packageOps;
    }

    protected PackageOpsParcelable(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageOpsParcelable> CREATOR = new Creator<PackageOpsParcelable>() {
        @Override
        public PackageOpsParcelable createFromParcel(Parcel in) {
            return new PackageOpsParcelable(in);
        }

        @Override
        public PackageOpsParcelable[] newArray(int size) {
            return new PackageOpsParcelable[size];
        }
    };
}
