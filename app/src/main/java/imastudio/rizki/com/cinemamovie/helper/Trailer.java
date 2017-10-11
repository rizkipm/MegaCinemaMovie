package imastudio.rizki.com.cinemamovie.helper;

import android.os.Parcel;
import android.os.Parcelable;



public class Trailer implements Parcelable {
    private String title;
    private String source;

    public Trailer(String title, String source){
        this.title = title;
        this.source = source;
    }

    public String getTitle(){return title;}
    public String getSource(){return source;}

    @Override
    public int describeContents() {
        return 0;
    }

    public Trailer(Parcel in){
        title = in.readString();
        source = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(source);
    }
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }

    };
}
