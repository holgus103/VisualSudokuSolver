package holgus103.visualsudokusolver

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.sudoku.*

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
        if(this.isGridComplete()){
            Toast
                    .makeText(this, getString(R.string.incomplete_message), Toast.LENGTH_LONG)
                    .show()
        }
        if(this.checkSudoku(this.rawSudoku))
        {
            Toast
                    .makeText(this, getString(R.string.gz_message), Toast.LENGTH_SHORT)
                    .show();
            // sudoku is ok
        };
        else{
            Toast
                    .makeText(this, getString(R.string.fail_message), Toast.LENGTH_SHORT)
                    .show();
            // sudoku not ok
        }
        // TODO: display message + mark faulty digits(?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve_by_hand)
    }

    external fun checkSudoku(sudoku: IntArray) : Boolean
}
