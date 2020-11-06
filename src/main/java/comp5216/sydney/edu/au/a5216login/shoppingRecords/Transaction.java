package comp5216.sydney.edu.au.a5216login.shoppingRecords;


import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {


    private String amount;
    private String datetime;
    private String description;



    public Transaction(String amount, String datetime, String description){

        this.amount = amount;
        this.datetime = datetime;
        this.description = description;

    }



    protected  Transaction(Parcel in){
        amount = in.readString();
        datetime = in.readString();
        description = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(datetime);
        dest.writeString(description);
    }





    public String getAmount(){

        return amount;
    }

    public String getDatetime(){

        return datetime;
    }


    public String getDescription(){

        return description;
    }
}
