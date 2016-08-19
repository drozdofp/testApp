package com.pavel.tinkoff_news.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pavel on 19.08.2016.
 */
public abstract class TargetCallback<R, T> implements Callback<R> {

    private T target;

    public TargetCallback() {
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

    @Override
    public void onResponse(Call<R> call, Response<R> response) {
        if (target == null) {
            return;
        }

        if (response.isSuccessful()) {
            onSuccess(call, response);
        } else {
            onFail(call);
        }
    }

    @Override
    public void onFailure(Call<R> call, Throwable t) {
        if (target == null) {
            return;
        }

        onFail(call);
    }

    protected abstract void onSuccess(Call<R> call, Response<R> response);

    protected abstract void onFail(Call<R> call);

}
