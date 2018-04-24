package holgus103.visualsudokusolver

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.sudoku.*

abstract class SudokuGridActivityBase : SudokuBaseActivity() {

    abstract fun prepareSudokuGrid();

    fun forEachField(func : (EditText, Int, Int, Int) -> Unit){

        for (i in 0..(sudoku_grid.childCount-1)) {

            val row = sudoku_grid.getChildAt(i) as ViewGroup;

            for(j in 0..(row.childCount-1)){

                val cell = row.getChildAt(j) as EditText;
                val current = this.rawSudoku[9 * i + j];
                // set sudoku values accordingly
;
            }
        }
    }

    fun fillGrid() =

        this.forEachField({cell, i, j, current ->

            if(current == 0){

                cell.text = SpannableStringBuilder( "" );

            }
            else{

                cell.text = SpannableStringBuilder();
            }
        });


    fun parseGrid() =

        this.forEachField({cell, i, j, current ->

            val res = cell.text.toString().toIntOrNull();
            if (res == null) {
                this.rawSudoku[i*9 + j] = 0;
            }
            else{
                this.rawSudoku[i*9 + j] = res;
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.rawSudoku = intent.extras.getIntArray(getString(R.string.sudoku));

    }

    override fun onResume() {

        super.onResume();
        this.fillGrid();
        this.prepareSudokuGrid();
    }

}