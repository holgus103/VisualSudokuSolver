package holgus103.visualsudokusolver.db.dao

import android.database.Cursor
import com.j256.ormlite.android.AndroidDatabaseResults
import com.j256.ormlite.dao.Dao
import holgus103.visualsudokusolver.db.DatabaseHelper

class SudokuDao(helper: DatabaseHelper) {

    companion object {
        lateinit var dao: Dao<SudokuEntry, Int>
        const val ID : String = "_id";
        const val SUDOKU : String = "sudoku";
        const val TIMESTAMP : String = "timestamp";
    }

    init {
        dao = helper.getDao(SudokuEntry::class.java);
    }

    fun add(entry: SudokuEntry){
        dao.create(entry);
    }

    fun update(entry: SudokuEntry){
        dao.update(entry);
    }

    fun delete(entry: SudokuEntry){
        dao.delete(entry);
    }

    fun getAllOrdered(): Cursor {
        val q = dao.queryBuilder().orderBy(TIMESTAMP, false);
        val res = dao.iterator(q.prepare()).rawResults as AndroidDatabaseResults;
        return res.rawCursor;
    }


}