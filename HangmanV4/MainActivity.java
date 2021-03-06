package com.example.hangmanv4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
//added functionality for entering letters and updating result for winning or losing
// the word to win is APP

    Hangman game; //reference to new Hangman class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (game == null){ //if there's no game, make a game
            game = new Hangman(Hangman.DEFAULT_GUESSES); //start new game
            setContentView(R.layout.activity_main); //set the view
            TextView status = findViewById(R.id.status); //status text view
            status.setText("" + game.getGuessesLeft()); //set status
        }

        //FragmentManager gives the tools for working with fragments
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.game_state) == null) { //checking that the fragment exists
            FragmentTransaction ft = fm.beginTransaction();
            GameStateFragment fragment = new GameStateFragment();
            ft.add(R.id.game_state, fragment);
            ft.commit(); //need this for fragment to be visible
        }

        if (fm.findFragmentById(R.id.game_result) == null) { //copied for game result fragment
            FragmentTransaction ft = fm.beginTransaction();
            GameResultFragment fragment = new GameResultFragment();
            ft.add(R.id.game_result, fragment);
            ft.commit(); //need this for fragment to be visible
        }
    }

    public void play(View view) { //play button's onclick event
        EditText input = findViewById(R.id.letter);
        Editable userText = input.getText(); // "capture the text in the EditText"

        if (userText != null && userText.length() > 0) { //is there a letter typed in?
            //update # of guesses left
            char letter = userText.charAt(0);
            game.guess(letter);

            TextView status = findViewById(R.id.status); //the # in the red rectangle
            status.setText("" + game.getGuessesLeft());

            //add letter to the incomplete word - the game state fragment
            FragmentManager fm = getSupportFragmentManager();
            GameStateFragment gameStateFragment = (GameStateFragment)fm.findFragmentById(R.id.game_state); //STATE
            View gsFragmentView = gameStateFragment.getView();
            TextView TXT_gameState = findViewById(R.id.state_of_game);
            TXT_gameState.setText(game.currentIncompleteWord());

            //clear edit text for next guess
            input.setText("");

            int result = game.gameOver();
            if (result != 0) {
                GameResultFragment gameResultFragment = (GameResultFragment)fm.findFragmentById(R.id.game_result); //RESULT

                //update text in result fragment (green box) to won or lost
                if(result == 1)
                    gameResultFragment.setResult("You Won!"); //added setResult to result fragment
                else if (result == -1)
                    gameResultFragment.setResult("You Lost!");

                //delete hint
                input.setHint("");
            }
        }
    }

    public Hangman getGame() { //method from GameStateFragment
        return game;
    }
}
