package holgus103.visualsudokusolver.db.dao

import android.database.Cursor
import com.j256.ormlite.android.AndroidDatabaseResults
import com.j256.ormlite.dao.Dao
import holgus103.visualsudokusolver.db.DatabaseHelper
import holgus103.visualsudokusolver.exceptions.DbException

class SudokuDao(helper: DatabaseHelper) {

    companion object {
        lateinit var dao: Dao<SudokuEntry, Int>
        const val ID : String = "_id";
        const val SUDOKU : String = "sudoku";
        const val ORIGINAL_SUDOKU : String = "original";
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

    fun get(id: Int?): SudokuEntry{
        val l = dao.queryBuilder().where().eq(ID, id).query()
        if(l.count() > 1){
            throw DbException(DbException.MULTIPLES)
        }
        if(l.count() == 0){
            throw DbException(DbException.NOT_FOUND)
        }
        return l[0];
    }


}