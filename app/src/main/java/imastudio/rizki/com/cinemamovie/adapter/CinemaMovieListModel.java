package imastudio.rizki.com.cinemamovie.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;



public class CinemaMovieListModel implements Parcelable {
    public final String LOG_TAG = CinemaMovieListModel.class.getSimpleName();
    String mImageURl;
    String mtitle;
    String mrelease_date;
    String msynopsis;
    String mrating;
    int mid;
    String mbackUrl;

    public CinemaMovieListModel(String Imageurl, String Title, String Release_date, String Synopsis , String Rating, int Id, String PosterUrl ) {
        this.mImageURl = Imageurl;
        this.mtitle = Title;
        this.mrelease_date = Release_date;
        this.msynopsis = Synopsis;
        this.mrating = Rating;
        this.mid = Id;
        this.mbackUrl=PosterUrl;
    }
    private CinemaMovieListModel(Parcel in){
        mImageURl=in.readString();
        mtitle=in.readString();
        mrelease_date=in.readString();
        msynopsis=in.readString();
        mrating=in.readString();
        mid=in.readInt();
        mbackUrl=in.readString();
    }



    public String getImageurl() {

        return mImageURl;
    }
    public String getBackPoster() {

        return  mbackUrl;
    }

    public String getTitle() {
        return mtitle;
    }
    public String getRelease_date(){
        return mrelease_date;
    }
    public String getSynopsis(){
        return msynopsis;
    }
    public String getRating () {
        return mrating;
    }

    public int getId(){ return mid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImageURl);
        parcel.writeString(mtitle);
        parcel.writeString(mrelease_date);
        parcel.writeString(msynopsis);
        parcel.writeString(mrating);
        parcel.writeInt(mid);
        parcel.writeString(mbackUrl);
    }
    public final static Parcelable.Creator<CinemaMovieListModel> CREATOR = new Parcelable.Creator<CinemaMovieListModel>() {
        @Override
        public CinemaMovieListModel createFromParcel(Parcel parcel) {
            return new CinemaMovieListModel(parcel);
        }

        @Override
        public CinemaMovieListModel[] newArray(int i) {
            return new CinemaMovieListModel[i];
        }
    };

}
