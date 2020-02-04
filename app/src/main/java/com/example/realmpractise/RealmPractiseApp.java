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

package com.example.realmpractise;

import android.app.Application;

import com.example.realmpractise.data.DaggerRepositoryComponent;
import com.example.realmpractise.data.RepositoryComponent;

import io.realm.Realm;

/**
 * Base application class
 **/

public class RealmPractiseApp extends Application {
    private static RealmPractiseApp app;
    RepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Realm.init(this);
        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .build();
    }

    /** Get an instance of the app */

    public static RealmPractiseApp getApp() {
        return app;
    }

    /** Get an instance of the {@link #mRepositoryComponent} for injections */

    public RepositoryComponent getmRepositoryComponent() {
        return mRepositoryComponent;
    }
}
