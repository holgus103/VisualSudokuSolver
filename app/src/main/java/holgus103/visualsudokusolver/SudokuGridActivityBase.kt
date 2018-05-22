package holgus103.visualsudokusolver

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.sudoku.*

abstract class SudokuGridActivityBase : SudokuBaseActivity() {

    abstract fun prepareSudokuGrid();


    fun forEachField(func : (EditText, Int, Int, Int) -> Unit){

        for (i in 0..(sudoku_grid.childCount-1)) {

            val row = sudoku_grid.getChildAt(i) as ViewGroup;

            for(j in 0..(row.childCount-1)){

                val cell = row.getChildAt(j) as EditText;
                val current = this.rawSudoku[9 * i + j];
                func(cell,  i, j, current);
                // set sudoku values accordingly
;
            }
        }
    }

    open fun fillGrid() =

        this.forEachField({cell, i, j, current ->

            if(current == 0){

                cell.text = SpannableStringBuilder( "" );

            }
            else{

                cell.text = SpannableStringBuilder(current.toString());
            }
        });


    fun parseGrid() =

        this.forEachField({cell, i, j, current ->

            val res = cell.text.toString().toIntOrNull();
            if (res == null || res > 9 || res < 1) {
                this.rawSudoku[i*9 + j] = 0;
            }
            else{
                this.rawSudoku[i*9 + j] = res;
            }
        })

    fun isGridComplete(): Boolean {
        // sync grid
        this.parseGrid();
        // check if all fields are filled
        return !this.rawSudoku.any({ x -> x == 0 })
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.rawSudoku = intent.extras.getIntArray(getString(R.string.sudoku));

    }

    override fun onResume() {

        super.onResume();
        this.fillGrid();
        this.prepareSudokuGrid();
    }

    fun processFailedGridReview(checked: BooleanArray, failMsg : Int){


            forEachField({cell, i, j, current ->
                if(!checked[i*9+j]) {
                    cell.setTextColor(Color.RED)
                    cell.setOnFocusChangeListener({ v, hasFocus  ->
                        cell.setTextColor(Color.BLACK);
                        v.setOnFocusChangeListener(null);
                    })
                }
            })

            Toast
                    .makeText(this, getString(failMsg), Toast.LENGTH_SHORT)
                    .show();
            // sudoku not ok
    }



}