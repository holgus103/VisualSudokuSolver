package holgus103.visualsudokusolver

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.text.InputType
import android.util.Log
import android.view.View
import holgus103.visualsudokusolver.db.dao.SudokuEntry
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.content.ContentValues
import android.provider.MediaStore


class SolvedActivity : SudokuReadyGridActivity() {

    override fun prepareSudokuGrid() =

        this.forEachField({ cell, _, _, _ ->
            cell.inputType = InputType.TYPE_NULL;
        });

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved)

        val m = PreferenceManager.getDefaultSharedPreferences(this);
        val autosave = m.getBoolean(getString(R.string.autosave_key), false);
        if(autosave){
            val sudoku = SudokuEntry(sudoku = this.rawSudoku)
            SudokuApp.instance.dao.add(sudoku);
        }

    }

    fun solveManually(v: View){
        val i = Intent(this, SolveByHand::class.java);
        i.putExtra(getString(R.string.raw_sudoku), this.fixedPuzzleValues)
        i.putExtra(getString(R.string.sudoku), this.rawSudoku)
        startActivity(i);
    }

    fun save(v: View){

        val sudoku = SudokuEntry(sudoku = this.rawSudoku)
        SudokuApp.instance.dao.add(sudoku);
        val i = Intent(this, MainActivity::class.java);
        this.startActivity(i);
    }

    fun saveSolvedImage(v: View) =
        this.saveSudokuImage(v, this.rawSudoku)

    fun saveOrigjnalSudokuImage(v: View) =
            this.saveSudokuImage(v, this.fixedPuzzleValues)

    fun saveSudokuImage(v: View, arr: IntArray){
        val bitmap = Bitmap.createBitmap(270, 270, Bitmap.Config.RGB_565);
        val c = Canvas(bitmap)
        c.drawColor(Color.WHITE)
        val p = Paint();
        p.isAntiAlias = true;
        p.color = Color.BLACK;
        p.style = Paint.Style.STROKE;
        p.strokeJoin = Paint.Join.ROUND;
        p.strokeWidth = 2f;
        val t = Paint()
        t.color = Color.BLACK
        t.textSize = 30f;
        t.style = Paint.Style.STROKE
        t.strokeJoin = Paint.Join.ROUND
        var h = 0.0F;
        var w = 0.0F;
        // draw horizontal lines
        for(i in 0..8) {
            c.drawLine(0.0F, h, 270.0F, h, p)
            h += 30
        }
        // draw vertical lines
        for(i in 0..8){
            c.drawLine(w, 0.0F, w, 270.0F, p)
            w += 30
        }
        val step = 30;
        // draw digits
        // TODO: draw digits
        for(i in 0..8){
            for(j in 0..8){
                if(rawSudoku[i*9+j] != 0) {
                    c.drawText(
                            this.rawSudoku[i * 9 + j].toString(),
                            step * j + 10.0F,
                            step * i + 25.0F,
                            t)
                }
            }

        }

        val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var file = File.createTempFile(Date().toString(), ".png", path)
        val stream = file.outputStream()
        try {
            val res = bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        catch(e: Exception){
            Log.d("EXCEPTION", e.message)
            // TODO: Error handling
        }
        finally{
            if(stream != null){
                stream.close()

                val values = ContentValues()

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, file.absolutePath);

                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        }


    }
}
