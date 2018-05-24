package holgus103.visualsudokusolver

import android.content.ContextWrapper
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


abstract class SudokuBaseActivity : AppCompatActivity() {

    var rawSudoku : IntArray = IntArray(81);

    /**cze
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */



    private fun copyFiletoExternalStorage(resourceId: Int, resourceName: String, suffix: String) {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        try {
            val p = File(storageDir.absolutePath + '/' + MODEL_PATH);
            val inStream = resources.openRawResource(resourceId)
            var out: FileOutputStream? = p.outputStream()
            val buff = ByteArray(1024)
            var read = 0
            try {
                while (true) {
                    read = inStream.read(buff)
                    if(read > 0)
                        out!!.write(buff, 0, read)
                    else
                        break;
                }
            }
            catch(ex: Exception){
                Log.d("EXCEPTION", ex.message)
            }
            finally {
                inStream.close()
                out!!.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    external fun runRecognition(path: String, model: String): IntArray

    external fun solve(sudoku: IntArray): IntArray

    external fun checkSudoku(sudoku: IntArray) : BooleanArray

    companion object {

        const val REQUEST_IMAGE_CAPTURE = 1
        const val MODEL_PATH = "knn.xml";

        // Used to load the 'native-lib' library on application startup.
        init {
            System.load("/data/app/org.opencv.engine-1/lib/arm/libopencv_java3.so")
            System.loadLibrary("native-lib")
        }
    }

}