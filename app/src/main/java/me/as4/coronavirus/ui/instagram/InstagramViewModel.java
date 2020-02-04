package me.as4.coronavirus.ui.instagram;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InstagramViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InstagramViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Instagram fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}