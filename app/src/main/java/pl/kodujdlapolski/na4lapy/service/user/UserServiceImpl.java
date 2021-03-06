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
package pl.kodujdlapolski.na4lapy.service.user;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import pl.kodujdlapolski.na4lapy.model.Animal;
import pl.kodujdlapolski.na4lapy.model.UserPreferences;
import pl.kodujdlapolski.na4lapy.model.type.ActivityAnimal;
import pl.kodujdlapolski.na4lapy.model.type.Gender;
import pl.kodujdlapolski.na4lapy.model.type.Size;
import pl.kodujdlapolski.na4lapy.model.type.Species;
import pl.kodujdlapolski.na4lapy.service.preferences.PreferencesService;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserServiceImpl implements UserService {

    private PreferencesService mPreferencesService;
    private UserPreferences mUserPreferences;

    @Inject
    public UserServiceImpl(PreferencesService preferencesService) {
        mPreferencesService = preferencesService;
        mUserPreferences = mPreferencesService.getUserPreferences();
    }

    @Override
    public void saveCurrentUserPreferences(@NonNull UserPreferences userPreferences) {
        UserPreferences newUserPreferences = checkNotNull(userPreferences, "UserPreferences cannot be null");
        mPreferencesService.setUserPreferences(newUserPreferences);
        mUserPreferences = userPreferences;
    }

    @Override
    @NonNull
    public UserPreferences loadCurrentUserPreferences() {
        UserPreferences userPreferences = mPreferencesService.getUserPreferences();
        return userPreferences != null ? userPreferences : new UserPreferences();
    }

    @Override
    public int getPreferencesComplianceLevel(Animal animal) {
        if (mUserPreferences == null) {
            return 0;
        }

        int result = 0;

        if ((mUserPreferences.isTypeDog() && Species.DOG.equals(animal.getSpecies())) ||
                (mUserPreferences.isTypeCat() && Species.CAT.equals(animal.getSpecies())) ||
                (mUserPreferences.isTypeOther() && Species.OTHER.equals(animal.getSpecies()))) {
                    ++result;
        } else {
            return 0;
        }

        if ((mUserPreferences.isGenderMan() && mUserPreferences.isGenderWoman() && Gender.UNKNOWN.equals(animal.getGender())) ||
                (mUserPreferences.isGenderMan() && Gender.MALE.equals(animal.getGender())) ||
                (mUserPreferences.isGenderWoman() && Gender.FEMALE.equals(animal.getGender()))) {
                    ++result;
        }

        if (animal.getBirthDate() != null) {
            int age = Years.yearsBetween(animal.getBirthDate(), LocalDate.now()).getYears();
            if (age >= mUserPreferences.getAgeMin() && age <= mUserPreferences.getAgeMax()) {
                ++result;
            }
        }

        if ((mUserPreferences.isSizeSmall() && mUserPreferences.isSizeMedium() && mUserPreferences.isSizeLarge() && Size.UNKNOWN.equals(animal.getSize())) ||
                (mUserPreferences.isSizeSmall() && Size.SMALL.equals(animal.getSize())) ||
                (mUserPreferences.isSizeMedium() && Size.MEDIUM.equals(animal.getSize())) ||
                (mUserPreferences.isSizeLarge() && Size.LARGE.equals(animal.getSize()))) {
                    ++result;
        }

        if ((mUserPreferences.isActivityLow() && mUserPreferences.isActivityHigh() && ActivityAnimal.UNKNOWN.equals(animal.getActivity())) ||
                (mUserPreferences.isActivityLow() && ActivityAnimal.LOW.equals(animal.getActivity())) ||
                (mUserPreferences.isActivityHigh() && ActivityAnimal.HIGH.equals(animal.getActivity()))) {
                    ++result;
        }

        return result;
    }

    @Override
    public boolean isComplianceLevelAvailable() {
        return mUserPreferences!=null;
    }

    @Override
    public void addToFavourite(Animal animal) {
        if (animal == null || animal.getId() == null) {
            return;
        }
        mPreferencesService.addToFavourite(animal.getId());
    }

    @Override
    public void removeFromFavourite(Animal animal) {
        if (animal == null || animal.getId() == null) {
            return;
        }
        mPreferencesService.removeFromFavourite(animal.getId());
    }

    @Override
    public boolean isFavourite(Animal animal) {
        List<Long> favourites = mPreferencesService.getFavouriteList();
        if (favourites == null || favourites.isEmpty() || animal == null || animal.getId() == null) {
            return false;
        }

        for (Long l : favourites) {
            if (l.equals(animal.getId())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Animal> sortByUserPreferences(List<Animal> animals) {
        if (animals == null) {
            return null;
        }
        Collections.sort(animals, (previousAnimal, nextAnimal) -> {
            Integer previousAnimalComplianceLevel = getPreferencesComplianceLevel(previousAnimal);
            Integer nextAnimalComplianceLevel = getPreferencesComplianceLevel(nextAnimal);
            return nextAnimalComplianceLevel.compareTo(previousAnimalComplianceLevel);
        });

        return animals;

    }

}
