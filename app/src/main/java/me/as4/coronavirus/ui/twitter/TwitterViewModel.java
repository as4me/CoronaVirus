package me.as4.coronavirus.ui.twitter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TwitterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TwitterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is twitter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}