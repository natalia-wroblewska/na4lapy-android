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
package pl.kodujdlapolski.na4lapy.ui.browse.single;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.kodujdlapolski.na4lapy.R;
import pl.kodujdlapolski.na4lapy.model.Animal;
import pl.kodujdlapolski.na4lapy.service.user.UserService;
import pl.kodujdlapolski.na4lapy.ui.browse.BrowseContract;
import pl.kodujdlapolski.na4lapy.ui.browse.OnBrowseElementClickedAction;

public class SingleBrowseAdapter extends RecyclerView.Adapter<SingleBrowseViewHolder> implements BrowseContract.Adapter {

    List<Animal> animals;
    OnBrowseElementClickedAction onBrowseElementClickedAction;
    UserService userService;

    public SingleBrowseAdapter(List<Animal> animals, OnBrowseElementClickedAction onBrowseElementClickedAction, UserService userService) {
        this.animals = animals;
        this.onBrowseElementClickedAction = onBrowseElementClickedAction;
        this.userService = userService;
    }

    @Override
    public SingleBrowseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_single_browse, parent, false);
        return new SingleBrowseViewHolder(v, userService);
    }

    @Override
    public void onBindViewHolder(SingleBrowseViewHolder holder, int position) {
        holder.init(animals.get(position), onBrowseElementClickedAction);
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    @Override
    public void notifyItemChanged(Animal animal) {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRemoved(Animal animal) {
        super.notifyItemRemoved(animals.indexOf(animal));
    }
}
