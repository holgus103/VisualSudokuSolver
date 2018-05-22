package holgus103.visualsudokusolver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SolveByHand : SudokuReadyGridActivity() {

    override fun prepareSudokuGrid() {
//        for(i in 0..(sudoku_grid.childCount-1)){
//            val row = sudoku_grid.getChildAt(i) as ViewGroup;
//            for(j in 0..(row.childCount-1)){
//                val cell = row.getChildAt(j) as EditText;
//                if(cell.text.toString() != ""){
//
//                }
//            }
//        }
    }

    fun solve(v: View){
        val i = Intent(this, SolvedActivity::class.java)
        this.rawSudoku = solve(this.rawSudoku);
        i.putExtra(getString(R.string.raw_sudoku), this.fixedPuzzleValues)
        i.putExtra(getString(R.string.sudoku), this.rawSudoku);
        startActivity(i);
    }

    fun check(v: View){

        forEachField({cell, _, _, _ ->
            cell.setTextColor(Color.BLACK);
        })

        if(!this.isGridComplete()){
            Toast
                    .makeText(this, getString(R.string.incomplete_message), Toast.LENGTH_LONG)
                    .show()
            return;
        }

        val checked = this.checkSudoku(this.rawSudoku);
        if(checked.all { v -> v })
        {
            Toast
                    .makeText(this, getString(R.string.gz_message), Toast.LENGTH_SHORT)
                    .show();
            // sudoku is ok
        };
        else {
            this.processFailedGridReview(checked, R.string.fail_message);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve_by_hand)
    }
}
