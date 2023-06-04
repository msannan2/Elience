package com.HCI.elience;

import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.HCI.elience.models.Answer;
import com.HCI.elience.models.Question;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import static com.HCI.elience.ForumActivity.BLUE_VOTE_UP_COLOR;
import static com.HCI.elience.ForumActivity.RED_VOTE_DOWN_COLOR;
import static com.HCI.elience.ForumActivity.USER_ID;

public class QuestionDetailActivity extends AppCompatActivity {

    public static final String QUESTION_KEY = "question";

    private ImageButton mButtonVoteUp;
    private ImageButton mButtonVoteDown;

    private TextView mQuestionTitleTextView;
    private TextView mQuestionCreatedOnTextView;

    private TextView mTextViewQuestionNumUpVotes;
    private TextView mTextViewQuestionNumDownVotes;

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseAnswerRef;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseRecyclerPagingAdapter<Answer, AnswerViewHolder> mAdapter;

    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        getWindow().setStatusBarColor(Color.BLACK);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_question_detail);

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view_question_detail);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        // Get and set Question properties
        question = getIntent().getParcelableExtra(QUESTION_KEY);

        // Initialize text views
        mQuestionTitleTextView = findViewById(R.id.question_detail_title);
        mQuestionCreatedOnTextView = findViewById(R.id.question_detail_title_created_on);
        mTextViewQuestionNumUpVotes = findViewById(R.id.question_detail_num_upvote);
        mTextViewQuestionNumDownVotes = findViewById(R.id.question_detail_num_downvote);

        // Initialize buttons
        mButtonVoteUp = findViewById(R.id.question_detail_button_upvote);
        mButtonVoteDown = findViewById(R.id.question_detail_button_downvote);

        // Set button values
        if (question.getVotedUpByUser() == 1) {
            mButtonVoteUp.setColorFilter(BLUE_VOTE_UP_COLOR);
        }

        if (question.getVotedDownByUser() == 1) {
            mButtonVoteDown.setColorFilter(RED_VOTE_DOWN_COLOR);
        }

        // Set text view values
        mQuestionTitleTextView.setText(question.getTitle());
        mQuestionCreatedOnTextView.setText(question.getCreatedOn());
        mTextViewQuestionNumUpVotes.setText(String.valueOf(question.getNumUpVotes()));
        mTextViewQuestionNumDownVotes.setText(String.valueOf(question.getNumDownVotes()));

        // Initialize Database
        mDatabaseAnswerRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions")
                .child(question.getId())
                .child("answers");

        final DatabaseReference mDatabaseQuestionRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions")
                .child(question.getId());

        // Initialize PagedList Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(3)
                .setPageSize(10)
                .build();

        // Initialize FirebasePagingOptions
        DatabasePagingOptions<Answer> options = new DatabasePagingOptions.Builder<Answer>()
                .setLifecycleOwner(this)
                .setQuery(mDatabaseAnswerRef, config, Answer.class)
                .build();

        // Create new Adapter
        mAdapter = new FirebaseRecyclerPagingAdapter<Answer, AnswerViewHolder>(options) {
            @NonNull
            @Override
            public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AnswerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_answer, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull AnswerViewHolder holder,
                                            int position,
                                            @NonNull Answer model) {
                holder.textViewAnswer.setText(model.getTitle());
                holder.textViewCreatedOn.setText(model.getCreatedOn());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        // Reached end of Data set
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        // retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };

        // Set Adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //Set listener to SwipeRefreshLayout for refresh action
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });

        // Show Dialog to add Items in Database
        findViewById(R.id.question_detail_button_add_answer).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createdQuestion();
                    }
                });


        mButtonVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasNotAlreadyVoted(question)) {
                    mDatabaseQuestionRef.child("upVotes")
                            .child(USER_ID).setValue(1);
                    mButtonVoteUp.setColorFilter(BLUE_VOTE_UP_COLOR);
                    question.setNumUpVotes(question.getNumDownVotes() + 1);
                    mTextViewQuestionNumUpVotes.setText(String.valueOf(question.getNumUpVotes()));
                    question.setVotedUpByUser(1);
                }
            }
        });

        mButtonVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasNotAlreadyVoted(question)) {
                    mDatabaseQuestionRef.child("downVotes")
                            .child(USER_ID)
                            .setValue(1);
                    mButtonVoteDown.setColorFilter(RED_VOTE_DOWN_COLOR);
                    question.setNumDownVotes(question.getNumDownVotes() + 1);
                    mTextViewQuestionNumDownVotes.setText(String.valueOf(question.getNumDownVotes()));
                    question.setVotedDownByUser(1);
                }
            }
        });
    }


    // Start Listening Adapter
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    // Stop Listening Adapter
    @Override
    protected void onStop() {
        mAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mAdapter.stopListening();
        super.onPause();
    }

    // Dialog to add new questions
    private void createdQuestion() {

        final EditText answerEditText = findViewById(R.id.question_detail_edit_text_answer);
        String answerText = answerEditText.getText().toString().trim();

        if (answerText.isEmpty()) {
            answerEditText.setError("Answer can't be empty");
            return;
        }
        if(!ProfanityChecker.CheckforProfanity(answerText))
        {
            answerEditText.setError("Profanity Detected ! Please keep this Forum clean.");
            return;
        }

        Answer answer = new Answer(answerText);
        mDatabaseAnswerRef.push().setValue(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideKeyboard();
                answerEditText.setText("");
                Toast.makeText(QuestionDetailActivity.this, "Successfully posted", Toast.LENGTH_SHORT).show();
                mAdapter.refresh();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean hasNotAlreadyVoted(Question question) {
        return question.getVotedUpByUser() == 0 && question.getVotedDownByUser() == 0;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

