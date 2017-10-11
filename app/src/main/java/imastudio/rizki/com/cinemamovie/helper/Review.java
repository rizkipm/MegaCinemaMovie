package imastudio.rizki.com.cinemamovie.helper;

import android.os.Parcel;
import android.os.Parcelable;



public class Review implements Parcelable {
    private String author;
    private String body;

    public Review(String author, String body){
        this.author = author;
        this.body = body;
    }

    public String getAuthor(){return author;}


    public String getBody(){return body;}

    @Override
    public int describeContents() {
        return 0;
    }
    private Review(Parcel in){
        author = in.readString();
        body = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(body);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };
}
