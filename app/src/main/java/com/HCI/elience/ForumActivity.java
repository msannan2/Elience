package com.HCI.elience;

import android.app.AlertDialog;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import static com.HCI.elience.QuestionDetailActivity.QUESTION_KEY;

public class ForumActivity extends AppCompatActivity {

    public static final int BLUE_VOTE_UP_COLOR = Color.rgb(30, 144, 255);
    public static final int RED_VOTE_DOWN_COLOR = Color.rgb(220, 20, 60);
    public static final int WHITE_COLOR = Color.rgb(255, 255, 255);
    public static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final static int REQUEST_CODE_1 = 1;

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseRecyclerPagingAdapter<Question, QuestionViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        getWindow().setStatusBarColor(Color.BLACK);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions");

        // Initialize PagedList Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(3)
                .setPageSize(10)
                .build();

        // Initialize FirebasePagingOptions
        DatabasePagingOptions<Question> options = new DatabasePagingOptions.Builder<Question>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, new SnapshotParser<Question>() {
                    @NonNull
                    @Override
                    public Question parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Question question = snapshot.getValue(Question.class);
                        question.setId(snapshot.getKey());

                        long numUpVotes = snapshot.child("upVotes").getChildrenCount();
                        long numDownVotes = snapshot.child("downVotes").getChildrenCount();
                        question.setNumUpVotes(numUpVotes);
                        question.setNumDownVotes(numDownVotes);

                        if (snapshot.child("upVotes").child(USER_ID).exists()) {
                            question.setVotedUpByUser(1);
                        }

                        if (snapshot.child("downVotes").child(USER_ID).exists()) {
                            question.setVotedDownByUser(1);
                        }

                        return question;
                    }
                })
                .build();

        // Create new Adapter
        mAdapter = new FirebaseRecyclerPagingAdapter<Question, QuestionViewHolder>(options) {
            @NonNull
            @Override
            public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_question, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final QuestionViewHolder holder,
                                            final int position,
                                            @NonNull final Question model) {
                holder.textViewQuestion.setText(model.getTitle());
                holder.textViewCreatedOn.setText(model.getCreatedOn());
                holder.textViewNumUpVotes.setText(String.valueOf(model.getNumUpVotes()));
                holder.textViewNumDownVotes.setText(String.valueOf(model.getNumDownVotes()));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ForumActivity.this, QuestionDetailActivity.class);
                        intent.putExtra(QUESTION_KEY, model);
                        startActivityForResult(intent, REQUEST_CODE_1);
                    }
                });
                holder.buttonVoteUp.setColorFilter(WHITE_COLOR);
                holder.buttonVoteDown.setColorFilter(WHITE_COLOR);

                if (model.getVotedUpByUser() == 1) {
                    holder.buttonVoteUp.setColorFilter(BLUE_VOTE_UP_COLOR);
                }

                if (model.getVotedDownByUser() == 1) {
                    holder.buttonVoteDown.setColorFilter(RED_VOTE_DOWN_COLOR);
                }

                holder.buttonVoteUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasNotAlreadyVoted(model)) {
                            mDatabase.child(model.getId())
                                    .child("upVotes")
                                    .child(USER_ID).setValue(1);
                            holder.buttonVoteUp.setColorFilter(BLUE_VOTE_UP_COLOR);
                            model.setNumUpVotes(model.getNumDownVotes() + 1);
                            holder.textViewNumUpVotes.setText(String.valueOf(model.getNumUpVotes()));
                            model.setVotedUpByUser(1);
                        }
                    }
                });

                holder.buttonVoteDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hasNotAlreadyVoted(model)) {
                            mDatabase.child(model.getId())
                                    .child("downVotes")
                                    .child(USER_ID)
                                    .setValue(1);
                            holder.buttonVoteDown.setColorFilter(RED_VOTE_DOWN_COLOR);
                            model.setNumDownVotes(model.getNumDownVotes() + 1);
                            holder.textViewNumDownVotes.setText(String.valueOf(model.getNumDownVotes()));
                            model.setVotedDownByUser(1);
                        }
                    }
                });

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
        findViewById(R.id.fab_add_question).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showQuestionCreateDialog();
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

    protected void onResume() {
        // mAdapter.notifyItemChanged(QUESTION_POSITION);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAdapter.stopListening();
        super.onPause();
    }

    // Dialog to add new questions
    private void showQuestionCreateDialog() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(ForumActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View mView = inflater.inflate(R.layout.dialog_add_question_layout, null);

        final EditText mQuestionEditText = mView.findViewById(R.id.dialog_add_edit_text_question);

        mDialog.setView(mView)
                .setTitle("Create Question")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String questionTxt = mQuestionEditText.getText().toString().trim();
                        if (!TextUtils.isEmpty(questionTxt) && ProfanityChecker.CheckforProfanity(questionTxt)) {
                            Question question = new Question(questionTxt);
                            mDatabase.push().
                                    setValue(question).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ForumActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                            // mAdapter.
                                        }
                                    });

                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .create()
                .show();
    }

    private boolean hasNotAlreadyVoted(Question question) {
        return question.getVotedUpByUser() == 0 && question.getVotedDownByUser() == 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        switch (requestCode) {
            case REQUEST_CODE_1:
                if (resultCode == RESULT_OK) {
                    mAdapter.refresh();
                }
        }
    }
}

