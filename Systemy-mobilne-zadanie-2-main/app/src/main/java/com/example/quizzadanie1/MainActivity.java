package com.example.quizzadanie1;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;

    private TextView questionTextView;
    int counter = 0;
    private Button hintButton;

    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";

    private static final int REQUEST_CODE_PROMPT = 0;

    private boolean answerWasShown;





    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false)
    };

    private int currentIndex = 0;

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;

        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                counter++;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show(); //wyswietlamy komunikat czy odpowiedz jest poprawna czy nie

        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
    }

    private void setNextQuestion() {

        if (currentIndex == questions.length)
        {
            showQuizResult();
        }
        else{
        questionTextView.setText(questions[currentIndex].getQuestionId());
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);}
    }

    private void showQuizResult() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Koniec quizu");
        builder.setMessage("Liczba poprawnych odpowiedzi: " + counter + "/" + questions.length);
        builder.setPositiveButton("OK", (dialog, which) -> {

            currentIndex = 0;
            counter = 0;
            setNextQuestion();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private ActivityResultLauncher<Intent> promptActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    answerWasShown = result.getData().getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Wywołane onCreate()");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        hintButton = findViewById(R.id.hint_button);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        setNextQuestion();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);

            }

        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1);
                answerWasShown = false;
                setNextQuestion();
            }
        });

        hintButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);

            boolean correctAnswer = questions[currentIndex].isTrueAnswer();

            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);

            promptActivityResultLauncher.launch(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Wywołane onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołane onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołane onResume()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołane onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołane onPause()");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Lifecycle", "Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }






}