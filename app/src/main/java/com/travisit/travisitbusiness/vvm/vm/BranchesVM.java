package com.travisit.travisitbusiness.vvm.vm;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.Category;
import com.travisit.travisitbusiness.model.forms.EditProfileForm;
import com.travisit.travisitbusiness.utils.FileType;

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

public class BranchesVM extends ViewModel {
    public MutableLiveData<ArrayList<Branch>> branchesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branch> branchMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deletingMutableLiveData = new MutableLiveData<>();

    CompositeDisposable compositeDisposable;

    public void getBranches() {
        Observable<JsonObject> observable = Client.getINSTANCE().getBranches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> {
            ArrayList<Branch> branches = parseBranches(o);
            branchesMutableLiveData.setValue(branches);
        }, e -> Log.d("PVMError", e.getMessage())));
    }

    public void addBranch(Branch branch) {
        Observable<Branch> observable = Client.getINSTANCE().addBranch(branch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> branchMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
    }
    public void editBranch(Branch branch) {
        Observable<Branch> observable = Client.getINSTANCE().updateBranch(branch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o -> branchMutableLiveData.setValue(o), e -> Log.d("PVMError", e.getMessage())));
    }
    public void deleteBranch(int id) {
        Completable observable = Client.getINSTANCE().deleteBranch(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(() -> deletingMutableLiveData.setValue("done"), e -> Log.d("PVMError", e.getMessage())));
    }


    private ArrayList<Branch> parseBranches(JsonObject jsonObject) {
        ArrayList<Branch> branches = new ArrayList<Branch>();
        JsonArray jsonArray = jsonObject.get("rows").getAsJsonArray();
        Log.d("businessXXXXXX", jsonArray.toString());

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject branchObject = jsonArray.get(i).getAsJsonObject();
            Branch branch = new Branch(
                    branchObject.get("id").getAsInt(),
                    branchObject.get("name").getAsString(),
                    branchObject.get("latitute").getAsDouble(),
                    branchObject.get("longitude").getAsDouble()
            );
            branches.add(branch);
        }
        return branches;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
