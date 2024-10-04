package com.zybooks.tictactoe;

//Group project
//ISYS 221 VL1
//Michelle Beckett
//Tyler Ranshaw

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private boolean xTurn = true; // X starts
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        status = findViewById(R.id.status);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Initialize buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                String buttonID = "button" + row + col;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[row][col] = findViewById(resID);
                buttons[row][col].setOnClickListener(new ButtonClickListener(row, col));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_game) {
            resetGame();
            return true;
        } else if (item.getItemId() == R.id.action_quit) {
            showQuitDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
        xTurn = true;
        status.setText("Turn: X");
    }

    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private class ButtonClickListener implements View.OnClickListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            buttons[row][col].setText(xTurn ? "X" : "O");
            buttons[row][col].setEnabled(false);
            if (checkForWin()) {
                showWinner(xTurn ? "X" : "O");
            } else if (isBoardFull()) {
                showWinner("CAT");
            } else {
                xTurn = !xTurn; // Switch turns
                status.setText("Turn: " + (xTurn ? "X" : "O"));
            }
        }
    }

    private boolean checkForWin() {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if ((buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][0].getText().equals(buttons[i][2].getText()) &&
                    !buttons[i][0].getText().toString().isEmpty()) ||
                    (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                            buttons[0][i].getText().equals(buttons[2][i].getText()) &&
                            !buttons[0][i].getText().toString().isEmpty())) {
                return true;
            }
        }
        return (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[0][0].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().toString().isEmpty()) ||
                (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                        buttons[0][2].getText().equals(buttons[2][0].getText()) &&
                        !buttons[0][2].getText().toString().isEmpty());
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinner(String winner) {
        String message = winner.equals("CAT") ? "It's a CAT!" : winner + " wins!";
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                })
                .show();
    }
}
