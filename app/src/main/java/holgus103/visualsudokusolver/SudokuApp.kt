package holgus103.visualsudokusolver

import android.app.Application
import holgus103.visualsudokusolver.db.DatabaseHelper
import holgus103.visualsudokusolver.db.dao.SudokuDao

class SudokuApp : Application() {

    companion object {
        lateinit var instance: SudokuApp;
    }

    var helper : DatabaseHelper;
    var dao : SudokuDao;

    init {
        instance = this;
        this.helper = DatabaseHelper(this);
        this.dao = SudokuDao(this.helper);
    }

}