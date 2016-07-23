package pl.kodujdlapolski.na4lapy.service.system;

import android.content.Intent;

import rx.subjects.PublishSubject;

public interface SystemService {

    boolean isOnline();
    PublishSubject<Boolean> getNetworkStatusPublisher();

    <T> Intent getShareIntent(T shareItem);
}