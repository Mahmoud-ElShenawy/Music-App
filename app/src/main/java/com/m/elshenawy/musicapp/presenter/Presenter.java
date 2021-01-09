package com.m.elshenawy.musicapp.presenter;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// Created By Mahmoud El Shenawy (Email : Mr.Mahmoud.El.Shenawy@Gmail.com)

public abstract class Presenter<T extends Presenter.View> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void initialize() {

    }

    public void terminate() {
        dispose();
    }

    private void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void addDisposableObserver(Disposable disposable) {
        compositeDisposable.add(disposable);
    }


    public interface View {
        Context context();
    }
}

