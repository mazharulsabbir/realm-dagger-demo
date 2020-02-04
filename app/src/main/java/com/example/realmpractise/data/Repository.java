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

package com.example.realmpractise.data;

import com.example.realmpractise.models.Result;

import javax.inject.Inject;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Provides the dagger Module and providers for the {@link RepositoryComponent}
 **/

public class Repository {
    private FakeApi mService;

    @Inject
    public Repository(FakeApi fakeApi) {
        mService = fakeApi;
    }

    /**
     *  Fetches a random user data and caches it in Realm db for offline use
     *  Returns a {@link Result} object containing user data
     **/

    public Single<Result> getUser() {
        return mService.getUser()
                .flatMap(response -> Single.just(response.getResults()
                        .get(0))
                .flatMap(result -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    final Result savedResult = realm.copyToRealm(result); // Persist unmanaged objects
                    realm.commitTransaction();
                    return Single.just(realm.copyFromRealm(savedResult));
                }));
    }

    /** Returns a {@link Result} object fetching it from Realm offline db*/

    public Single<Result> getUserOffline() {
        return Single.create(emitter -> {
             Realm realm = Realm.getDefaultInstance();
            final  Result result = realm
                    .where(Result.class)
                    .sort("createdAt", Sort.DESCENDING)
                    .findFirst();
            emitter.onSuccess(realm.copyFromRealm(result));
        });
    }



}
