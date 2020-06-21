package com.travisit.travisitbusiness.vvm.vm;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.Category;
import com.travisit.travisitbusiness.model.forms.EditProfileForm;
import com.travisit.travisitbusiness.model.forms.SignUpForm;
import com.travisit.travisitbusiness.utils.FileType;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class ProfileVM extends ViewModel {
    public MutableLiveData<JsonObject> profileMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Category>> categoriesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Business> fileLMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Business> fileGMutableLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable;
    public ArrayList<Integer> selectedCategories = new ArrayList<>();

    public void editProfile(String name, String email, String governmentIssuedNumber) {
        EditProfileForm editProfileForm = new EditProfileForm(name, email, governmentIssuedNumber, selectedCategories);
        Observable<JsonObject> observable = Client.getINSTANCE().editProfile(editProfileForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> profileMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
    }

    public void getCategories() {
        Observable<JsonObject> observable = Client.getINSTANCE().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> {
            ArrayList<Category> categories = parseCategories(o);
            categoriesMutableLiveData.setValue(categories);
        }, e -> Log.d("PVMError", e.getMessage())));
    }

    private ArrayList<Category> parseCategories(JsonObject jsonObject) {
        ArrayList<Category> categories = new ArrayList<Category>();
        JsonArray jsonArray = jsonObject.get("category").getAsJsonArray();
        Log.d("businessXXXXXX", jsonArray.toString());

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject categoryObject = jsonArray.get(i).getAsJsonObject();
            Category category = new Category(
                    categoryObject.get("id").getAsInt(),
                    categoryObject.get("name").getAsString()
            );
            categories.add(category);
        }
        return categories;
    }

    public void uploadFile(String filePath, Context context, FileType fileType) {
        String fileName = null;
        if (fileType == FileType.GOVERNMENT_ISSUED_NUMBER) {
            fileName = "governmentIssuedNumberImage";
        } else {
            fileName = "logo";
        }
        if (fileType != null && filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(fileName, file.getName(), requestFile);

            compositeDisposable = new CompositeDisposable();

            if (fileType == FileType.GOVERNMENT_ISSUED_NUMBER) {
                Observable<Business> observable = Client.getINSTANCE().uploadIssuedNumberLogo(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                compositeDisposable.add(observable.subscribe(o -> fileGMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
            } else {
                Observable<Business> observable = Client.getINSTANCE().changeBusinessLogo(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                compositeDisposable.add(observable.subscribe(o -> fileLMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
            }


        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
