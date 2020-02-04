/*
 * Copyright 2020 Mushfiqus Salehin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.example.realmpractise.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.realmpractise.RealmPractiseApp;
import com.example.realmpractise.data.Repository;
import com.example.realmpractise.models.Resource;
import com.example.realmpractise.models.Result;
import com.example.realmpractise.models.Status;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Provides the ViewModel for {@link MainActivity}
 **/

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    @Inject
    Repository mRepository;
    private MutableLiveData<Resource<Result>> mUserListener;
    private CompositeDisposable mDisposable;

    public MainViewModel() {
        RealmPractiseApp.getApp().getmRepositoryComponent().inject(this);
    }

    /** Getter for {@link #mUserListener} */

    public MutableLiveData<Resource<Result>> getmUserListener() {
        if (mUserListener == null) {
            mUserListener = new MutableLiveData<>();
        }
        return mUserListener;
    }

    /** Load userdata and push it to {@link #mUserListener} */

    void loadUser(boolean isOnline) {
        if (isOnline) {
            mRepository
                    .getUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                            mUserListener.postValue(new Resource<>(Status.LOADING, null, null));
                        }

                        @Override
                        public void onSuccess(Result result) {
                            mUserListener.postValue(new Resource<>(Status.SUCCESSFUL, result, null));
                        }

                        @Override
                        public void onError(Throwable e) {
                            mUserListener.postValue(new Resource<>(Status.ERROR, null, e));
                        }
                    });
        } else {
            mRepository
                    .getUserOffline()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Result>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                            mUserListener.postValue(new Resource<>(Status.LOADING, null, null));
                        }

                        @Override
                        public void onSuccess(Result result) {
                            mUserListener.postValue(new Resource<>(Status.SUCCESSFUL, result, null));
                        }

                        @Override
                        public void onError(Throwable t) {
                            mUserListener.postValue(new Resource<>(Status.ERROR, null, t));
                        }
                    });
        }

    }

    /** Add disposables to {@link #mDisposable} */

    private void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    /** Dispose {@link #mDisposable} when ViewModel is cleared from memory */

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
