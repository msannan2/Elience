package com.example.forumapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class AnswerViewHolder extends RecyclerView.ViewHolder {
    TextView textViewAnswer;
    TextView textViewCreatedOn;

    public AnswerViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewAnswer = itemView.findViewById(R.id.card_view_item_answer_title);
        textViewCreatedOn = itemView.findViewById(R.id.card_view_item_answer_created_on);
    }
}