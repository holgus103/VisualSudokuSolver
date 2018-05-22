package holgus103.visualsudokusolver

import android.support.v7.app.AppCompatActivity

abstract class SudokuBaseActivity : AppCompatActivity() {

    var rawSudoku : IntArray = IntArray(81);

    /**cze
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun runRecognition(path: String): IntArray

    external fun solve(sudoku: IntArray): IntArray

    external fun checkSudoku(sudoku: IntArray) : BooleanArray

    companion object {

        const val REQUEST_IMAGE_CAPTURE = 1

        // Used to load the 'native-lib' library on application startup.
        init {
            System.load("/data/app/org.opencv.engine-1/lib/arm/libopencv_java3.so")
            System.loadLibrary("native-lib")
        }
    }

}