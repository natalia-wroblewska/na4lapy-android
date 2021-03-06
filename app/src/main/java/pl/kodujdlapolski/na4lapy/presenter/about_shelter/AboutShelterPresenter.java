/*
 *	Copyright 2017 Stowarzyszenie Na4Łapy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package pl.kodujdlapolski.na4lapy.presenter.about_shelter;

import android.content.Intent;

import javax.inject.Inject;

import pl.kodujdlapolski.na4lapy.Na4LapyApp;
import pl.kodujdlapolski.na4lapy.model.Shelter;
import pl.kodujdlapolski.na4lapy.service.repository.RepositoryService;
import pl.kodujdlapolski.na4lapy.ui.about_shelter.AboutShelterContract;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AboutShelterPresenter {

    @Inject
    RepositoryService repositoryService;
    private AboutShelterContract.View view;
    private final Long shelterId;

    public AboutShelterPresenter(AboutShelterContract.View view) {
        this.view = view;
        shelterId = view.getShelterId();
        ((Na4LapyApp) view.getActivity().getApplication()).getComponent().inject(this);
        startDownloadingData();
    }

    private void startDownloadingData() {
        view.showStateWaitingForData();
        getData();
    }

    private void onShelterAvailable(Shelter shelter) {
        if (shelter != null) {
            view.setShareIntent(getShareIntent());
            view.populateView(shelter);
            view.showStateDataIsAvailable();
        } else {
            view.showStateDataIsEmpty();
        }
    }

    private Intent getShareIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, view.getFormattedTitle());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, view.getFormattedInfoText());
        return sharingIntent;
    }

    private void getData() {
        repositoryService.getShelter(shelterId)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onShelterAvailable,
                        view::showStateError);
    }
}
