package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class ParceledListSlice implements Parcelable {
    protected ParceledListSlice(Parcel in) {
    }

    public static final Creator<ParceledListSlice> CREATOR = new Creator<ParceledListSlice>() {
        @Override
        public ParceledListSlice createFromParcel(Parcel in) {
            return new ParceledListSlice(in);
        }

        @Override
        public ParceledListSlice[] newArray(int size) {
            return new ParceledListSlice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }

    public List<?> getList() {
        return null;
    }
}
