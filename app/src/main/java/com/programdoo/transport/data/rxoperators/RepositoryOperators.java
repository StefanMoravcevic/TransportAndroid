package com.programdoo.transport.data.rxoperators;

import com.programdoo.transport.utils.AuthErrors;

import java.util.function.Function;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepositoryOperators {
    public static <T, R> Observable<R> createDataStream(
            Observable<T> paramsSource,
            Function<T, Observable<R>> repoCall) {
        return paramsSource
                .switchMap(params -> repoCall.apply(params)
                        .subscribeOn(Schedulers.io())
                        .onErrorResumeNext(error -> {
                            if (AuthErrors.isSessionExpired(error))
                                return Observable.empty();
                            return Observable.error(error);
                        }))
                .replay(1)
                .refCount();
    }

    public static <T, R> Observable<R> createRefreshableDataStream(
            Observable<T> paramsSource,
            Observable<Object> refreshTrigger,
            Function<T, Observable<R>> repoCall) {

        return Observable.combineLatest(
                        paramsSource,
                        refreshTrigger.startWithItem(new Object()),
                        (params, ignored) -> params)
                .switchMap(params -> repoCall.apply(params)
                        .subscribeOn(Schedulers.io())
                        .onErrorResumeNext(error -> {
                            if (AuthErrors.isSessionExpired(error)) return Observable.empty();
                            return Observable.error(error);
                        })
                ).replay(1)
                .refCount();
    }

    public static <T> Observable<Object> createRefreshCompletedStream(
            Observable<Object> refreshTrigger,
            Observable<T> observableDataStream) {

        return refreshTrigger
                .switchMap(ignored ->
                        observableDataStream
                                .take(1)
                                .map(__ -> new Object())
                                .onErrorReturnItem(new Object())
                );
    }
}
