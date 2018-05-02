package holgus103.visualsudokusolver.exceptions

class DbException(msg: String) : Exception(msg) {
    companion object {
        const val MULTIPLES = "Multiple entries with the same id";
        const val NOT_FOUND = "Entry not found"
    }
}