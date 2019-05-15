package com.example.forumapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

class QuestionViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    TextView textViewQuestion;
    TextView textViewCreatedOn;
    TextView textViewNumUpVotes;
    TextView textViewNumDownVotes;
    ImageButton buttonVoteUp;
    ImageButton buttonVoteDown;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_view_item_question);
        textViewQuestion = itemView.findViewById(R.id.card_view_item_question_title);
        textViewCreatedOn = itemView.findViewById(R.id.card_view_item_question_created_on);
        textViewNumUpVotes = itemView.findViewById(R.id.card_view_item_question_num_upvote);
        textViewNumDownVotes = itemView.findViewById(R.id.card_view_item_question_num_downvote);
        buttonVoteUp = itemView.findViewById(R.id.card_view_item_question_button_upvote);
        buttonVoteDown = itemView.findViewById(R.id.card_view_item_question_button_downvote);
    }
}