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

package pl.kodujdlapolski.na4lapy.ui.browse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.kodujdlapolski.na4lapy.R;
import pl.kodujdlapolski.na4lapy.model.Animal;
import pl.kodujdlapolski.na4lapy.model.Photo;
import pl.kodujdlapolski.na4lapy.service.user.UserService;
import pl.kodujdlapolski.na4lapy.ui.details.DetailsActivity;

public abstract class AbstractBrowseViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.view_holder_animal_name)
    TextView name;
    @BindView(R.id.matching_lvl_image)
    ImageButton matchLevelImage;
    @BindView(R.id.profile_pic_on_list)
    public ImageView profilePic;

    private Context mContext;
    private UserService mUserService;

    public AbstractBrowseViewHolder(View itemView, UserService userService) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mUserService = userService;
        mContext = itemView.getContext();
    }

    public void init(Animal animal, OnBrowseElementClickedAction onBrowseElementClickedAction) {
        String url = animal.getProfilePicUrl();
        if (url != null) {
            Picasso.with(itemView.getContext())
                    .load(url)
                    .into(profilePic);
        }
        String nameText = animal.getBirthDate() != null ? mContext.getString(R.string.animal_details_title,
                animal.getName(), DetailsActivity.getAgeTextShort(mContext, animal.getBirthDate())) :
                animal.getName();
        name.setText(nameText);
        matchLevelImage.setImageLevel(mUserService.getPreferencesComplianceLevel(animal));
        matchLevelImage.setOnClickListener(v -> onBrowseElementClickedAction.complianceLevel());
    }
}
