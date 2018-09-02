package com.kalpeshwadekar.shaadi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class MatchesCardAdapter extends RecyclerView.Adapter<MatchesCardAdapter.MatchesCardViewHolder> {

    private List<MatchesCard> matchesCardList;
    private RecyclerView mRecyclerView;
    public MatchesCardAdapter(List<MatchesCard> matchesCardList){
        this.matchesCardList = matchesCardList;
    }

    public class MatchesCardViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age, location, gender;
        public Button dislikeBtn, likeBtn;
        public ImageView picture;

        public MatchesCardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            location = itemView.findViewById(R.id.location);
            gender= itemView.findViewById(R.id.gender);
            picture = itemView.findViewById(R.id.picture);
            dislikeBtn = itemView.findViewById(R.id.dislikeBtn);
            likeBtn = itemView.findViewById(R.id.likeBtn);
        }
    }

    @Override
    public MatchesCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matches_card, parent, false);
        return new MatchesCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MatchesCardViewHolder holder, int position) {
        MatchesCard matchesCard = matchesCardList.get(position);
        holder.name.setText("Name: "+ Utils.toTitleCase(matchesCard.getFirstName()+" "+matchesCard.getLastName()));
        holder.age.setText("Age: "+ matchesCard.getAge() + " Years");
        holder.location.setText("City: "+ Utils.toTitleCase(matchesCard.getCity()));
        holder.gender.setText("Gender: "+ Utils.toTitleCase(matchesCard.getGender()));
        Picasso.get().load(matchesCard.getPicture()).placeholder(R.drawable.image_placeholder).into(holder.picture);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard(holder.getAdapterPosition());
            }
        });
        holder.dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCard(holder.getAdapterPosition());
            }
        });
    }

    private void saveCard(int position) {
        //Api call to saved liked profile and notify user on saved
        matchesCardList.remove(position);
        mRecyclerView.setItemAnimator(new SlideInRightAnimator());
        notifyItemRemoved(position);
    }

    private void deleteCard(int position) {
        //Api call to remove this profile to not to show again to this user
        matchesCardList.remove(position);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return matchesCardList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }
}
