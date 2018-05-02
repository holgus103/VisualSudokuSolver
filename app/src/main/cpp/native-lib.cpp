#include <jni.h>
#include "Sudoku.h"
#include <string>

extern "C" JNIEXPORT jintArray

JNICALL Java_holgus103_visualsudokusolver_MainActivity_runRecognition(JNIEnv *env, jclass cls)
{
    int array[] = {
            5,3,0, 0,7,0, 0,0,0,
            6,0,0, 1,9,5, 0,0,0,
            0,9,8, 0,0,0, 0,6,0,

            8,0,0, 0,6,0, 0,0,3,
            4,0,0, 8,0,3, 0,0,1,
            7,0,0, 0,2,0, 0,0,6,

            0,6,0, 0,0,0, 2,8,0,
            0,0,0, 4,1,9, 0,0,5,
            0,0,0, 0,8,0, 0,7,9
    };
    auto res = env->NewIntArray(SUDOKU_SIZE*SUDOKU_SIZE);
    env->SetIntArrayRegion(res, 0, SUDOKU_SIZE*SUDOKU_SIZE, array);
    return res;
}

extern "C" JNIEXPORT jintArray

JNICALL Java_holgus103_visualsudokusolver_SudokuGridActivityBase_solve(JNIEnv * env, jclass cls, jintArray sudoku){

    auto res = env->NewIntArray(SUDOKU_SIZE*SUDOKU_SIZE);
    jint *arr = env->GetIntArrayElements(sudoku, false);
    auto puzzle = Sudoku(arr);
    jint* solution = puzzle.Solve();
    env->SetIntArrayRegion(res, 0, 81, solution);
    return res;
}

extern "C" JNIEXPORT bool

JNICALL Java_holgus103_visualsudokusolver_SolveByHand_checkSudoku(JNIEnv * env, jclass cls, jintArray sudoku) {
    return true;
}





