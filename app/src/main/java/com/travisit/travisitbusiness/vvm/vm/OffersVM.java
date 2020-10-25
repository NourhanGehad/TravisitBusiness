package com.travisit.travisitbusiness.vvm.vm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.model.OfferComment;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OffersVM extends ViewModel {
    public MutableLiveData<ArrayList<Offer>> offersMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Offer> offerMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<OfferComment>> offerCommentsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deletingMutableLiveData = new MutableLiveData<>();
    public String startDate = null;
    public String endDate = null;
    public ArrayList<Integer> selectedTags = new ArrayList<>();
    CompositeDisposable compositeDisposable;
    CompositeDisposable compositeDisposable2;
    public MutableLiveData<Offer> photosMutableLiveData = new MutableLiveData<>();

    public void getOffers() {
        Observable<ArrayList<Offer>> observable = Client.getINSTANCE().getOffers(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> {
            //ArrayList<Offer> offers = parseOffers(o);
            offersMutableLiveData.setValue(o);
        }, e -> Log.d("PVMError", e.getMessage())));
    }

    public void addOffer(Offer offer) {
        Observable<Offer> observable = Client.getINSTANCE().addOffer(offer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> offerMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
    }

    public void editOffer(Offer offer) {
        Observable<Offer> observable = Client.getINSTANCE().updateOffer(offer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> offerMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
    }

    public void deleteOffer(int id) {
        Completable observable = Client.getINSTANCE().deleteOffer(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(() ->
                        deletingMutableLiveData.setValue("done")
                , e -> Log.d("PVMError", e.getMessage())));
    }

    public void uploadFiles(String filePath1, String filePath2, String filePath3) {

        if (filePath1 != null && !filePath1.isEmpty()
                && filePath2 != null && !filePath2.isEmpty()
                && filePath3 != null && !filePath3.isEmpty()) {
            File file1 = new File(filePath1);
            File file2 = new File(filePath2);
            File file3 = new File(filePath3);
            RequestBody requestFile1 =
                    RequestBody.create(MediaType.parse("image/*"), file1);
            MultipartBody.Part body1 =
                    MultipartBody.Part.createFormData("firstImage", file1.getName(), requestFile1);

            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("image/*"), file2);
            MultipartBody.Part body2 =
                    MultipartBody.Part.createFormData("secondImage", file2.getName(), requestFile2);

            RequestBody requestFile3 =
                    RequestBody.create(MediaType.parse("image/*"), file3);
            MultipartBody.Part body3 =
                    MultipartBody.Part.createFormData("thirdImage", file3.getName(), requestFile3);

            compositeDisposable2 = new CompositeDisposable();
            Observable<Offer> observable = Client.getINSTANCE().uploadOfferPhotos(body1, body2, body3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            compositeDisposable2.add(observable.subscribe(o -> photosMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));

        }
    }

    private ArrayList<Offer> parseOffers(JsonArray jsonArray) {
        ArrayList<Offer> offers = new ArrayList<Offer>();
//        JsonArray jsonArray = jsonObject.get("rows").getAsJsonArray();
        Log.d("rbusinessXXXXXX", jsonArray.toString());

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject offerObject = jsonArray.get(i).getAsJsonObject();
            Log.d("addedoffer", "startdate: " + offerObject.get("startDate").getAsString());
            Log.d("addedoffer", "enddate: " + offerObject.get("endDate").getAsString());
/*
            String img1 = "";
            String img2 = "";
            String img3 = "";
            if(offerObject.has("firstImage")){
                img1 = offerObject.get("firstImage").getAsString();
                Log.d("addedoffer","img1:happy: "+img1);
            } else {
                Log.d("addedoffer","img1:not sad: "+img1);
            }
*/

            Offer offer = new Offer(
                    offerObject.get("id").getAsInt(),
                    offerObject.get("title").getAsString(),
                    offerObject.get("description").getAsString(),
                    offerObject.get("startDate").getAsString(),
                    offerObject.get("endDate").getAsString()
            );
            /*                    offerObject.get("firstImage").getAsString(),
                    offerObject.get("secondImage").getAsString(),
                    offerObject.get("thirdImage").getAsString()*/
            offers.add(offer);
        }
        return offers;
    }

    public void getOfferComments(Integer offerId, Integer pageNumber, Integer pageSize) {
        Observable<ArrayList<OfferComment>> observable = Client.getINSTANCE().getOfferComments(offerId, pageNumber, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> {
            Log.d("hey", "heyg" + o.size());
            offerCommentsMutableLiveData.setValue(o);
        }, e -> Log.d("PVMError", e.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
