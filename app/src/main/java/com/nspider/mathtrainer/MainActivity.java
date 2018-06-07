package com.nspider.mathtrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ProgressBar timerProgress;
    TextView timerText;
    TextView scoreText;
    TextView liveCount;
    TextView liveText;
    TextView question;
    Button b00;
    Button b01;
    Button b10;
    Button b11;
    GridLayout answerGrid;
    ImageView thumbs;
    TextView info;
    Button play;
    LinearLayout playArea;

    int correctIndex;

    boolean isActive = false;

    long millis;

    Random rn;

    int tickCount = 0;

    int lives = 3;

    CountDownTimer countDownTimer;

    public void timerStart(long time){

        if (time > 30100) time = 30100;

        isActive = true;

        countDownTimer = new CountDownTimer(time+100,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;

                if (!isActive) tickCount++;

                if (tickCount == 10) {
                    isActive = true;
                    tickCount = 0;
                }

                int sec =(int) millisUntilFinished / 100;
                int sec1 = (int) millisUntilFinished / 1000;
                timerText.setText(String.valueOf(sec1));
                timerProgress.setProgress(sec);
            }

            @Override
            public void onFinish() {
                timerProgress.setProgress(0);
                isActive = false;
                //setQuestion();
                //countDownTimer.start();
                stop(true);
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerProgress = findViewById(R.id.timeBar);
        timerText = findViewById(R.id.time);
        scoreText = findViewById(R.id.scoreBoard);
        question = findViewById(R.id.question);
        b00 = findViewById(R.id.button5);
        b01 = findViewById(R.id.button6);
        b10 = findViewById(R.id.button7);
        b11 = findViewById(R.id.button8);
        answerGrid = findViewById(R.id.answerGrid);
        rn = new Random();
        thumbs = findViewById(R.id.thumbs);
        liveCount = findViewById(R.id.scoreBoard2);
        liveText = findViewById(R.id.live);
        info = findViewById(R.id.textView3);
        play = findViewById(R.id.button);
        playArea = findViewById(R.id.gameScreen);

        timerProgress.setRotation(-90f);
        timerProgress.setMax(300);

    }

    private void setQuestion() {

        int x;
        correctIndex = rn.nextInt(answerGrid.getChildCount());
        int correctAnswer = 0;

        ArrayList<Integer> check = new ArrayList<>();

        for (int i = 0 ; i < answerGrid.getChildCount() ; i++ ){

            x = rn.nextInt(89) + 10;

            while (check.contains(x)) x = rn.nextInt(89) + 10;

            check.add(x);

            if (i == correctIndex) correctAnswer = x ;

            ((Button) answerGrid.getChildAt(i)).setText(String.valueOf(x));
        }

        int y;

        y = rn.nextInt(correctAnswer);

        String q;

        int luck = rn.nextInt(2);

        if (luck == 1) q = String.valueOf(y) + " + " + String.valueOf(correctAnswer - y) + " = ?";
        else q = String.valueOf(y + correctAnswer) + " - " + String.valueOf(y) + " = ?";

        question.setText(q);
    }

    public void getAnswer(View view){
        if (isActive) {
            if (view == answerGrid.getChildAt(correctIndex)) {
                // Correct Answer
                //Toast.makeText(this, "Correct Answer !!", Toast.LENGTH_SHORT).show();
                thumbs.setImageResource(R.drawable.right);
                scoreText.setText(String.valueOf(1 + Integer.parseInt(scoreText.getText().toString())));
                countDownTimer.cancel();
                timerStart(millis + 2000);
            } else {
                thumbs.setImageResource(R.drawable.wrong);
                lives--;
                liveCount.setText(Integer.toString(lives));

                if (lives == 0){

                    countDownTimer.cancel();
                    isActive = false;
                    //liveText.setText("You are out of lives");
                    //liveCount.setText("");
                    stop(false);
                }

            }

            thumbs.setAlpha(1f);
            thumbs.animate().alpha(0f).setDuration(1000);

            setQuestion();
        }
    }

    public void stop(boolean type){
        if (type){
            info.setText("Time's up");
        } else {
            info.setText("You're out of lives");
        }
        play.setText("play again");

        info.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        question.setVisibility(View.GONE);
        answerGrid.setVisibility(View.GONE);

    }

    public void restart (View view){
        info.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        playArea.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        answerGrid.setVisibility(View.VISIBLE);

        timerProgress.setProgress(300);

        timerStart(30100);
        //Log.i("Debug","till this...!!");
        setQuestion();
        scoreText.setText("0");
        lives = 3;
        liveCount.setText("3");

    }
}
