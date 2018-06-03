package holgus103.visualsudokusolver.db.dao

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "SolvedSudokus")
data class SudokuEntry(

    @DatabaseField(generatedId = true, columnName = SudokuDao.ID)
    var id: Int? = null,

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = SudokuDao.SUDOKU)
    var sudoku: IntArray = IntArray(81),

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = SudokuDao.ORIGINAL_SUDOKU)
    var original: IntArray = IntArray(81),

    @DatabaseField(columnName = SudokuDao.TIMESTAMP, format = "yyyy-MM-dd HH:mm:ss")
    var timestamp: Date = Date()

)