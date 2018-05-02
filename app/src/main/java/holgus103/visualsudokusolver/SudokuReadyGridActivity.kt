package holgus103.visualsudokusolver

import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder

abstract class SudokuReadyGridActivity : SudokuGridActivityBase() {

    var fixedPuzzleValues: IntArray = IntArray(81);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fixedPuzzleValues = intent.getIntArrayExtra(getString(R.string.raw_sudoku))
    }

    override fun fillGrid() {
        this.forEachField({cell, i, j, current ->

            if(current == 0){

                cell.text = SpannableStringBuilder( "" );

            }
            else{
                cell.text = SpannableStringBuilder(current.toString());
                cell.inputType = InputType.TYPE_NULL;
                // for previously fixed fields -> bold
                if(this.fixedPuzzleValues[9*i + j] != 0){
                    cell.typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
            }
        });
    }
}