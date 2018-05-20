#include <jni.h>
#include "Sudoku.h"
#include <string>
#include "helpers.h"
#include <opencv2/core.hpp>
#define CLASSIFIER_PATH "/storage/extSdCard/knn.xml"
extern "C" JNIEXPORT jintArray

JNICALL Java_holgus103_visualsudokusolver_MainActivity_runRecognition(JNIEnv *env, jclass cls, jstring path)
{


    const char *cstr = env->GetStringUTFChars(path, NULL);
    auto p = std::string(cstr);

    std::vector<cv::Mat> digits = std::vector<cv::Mat>();
    std::vector<int> labels = std::vector<int>();

    extractDigitImages(p, digits, labels, false);
    auto d = DigitRecognizer(CLASSIFIER_PATH);

    int array[SUDOKU_SIZE * SUDOKU_SIZE];

    for(auto i = 0; i < SUDOKU_SIZE * SUDOKU_SIZE; i++){
        auto output = d.classify(digits[i]);
        array[i] = (int)(output + 0.5f);
    }
    auto res = env->NewIntArray(SUDOKU_SIZE*SUDOKU_SIZE);
    env->SetIntArrayRegion(res, 0, SUDOKU_SIZE*SUDOKU_SIZE, array);
    return res;
}

extern "C" JNIEXPORT jintArray

JNICALL Java_holgus103_visualsudokusolver_SudokuGridActivityBase_solve(JNIEnv * env, jclass cls, jintArray sudoku){

    auto res = env->NewIntArray(SUDOKU_SIZE*SUDOKU_SIZE);
//    jboolean v = JNI_FALSE;
    jint *arr = env->GetIntArrayElements(sudoku, NULL);
    auto puzzle = Sudoku(arr);
    jint* solution = puzzle.Solve();
    env->SetIntArrayRegion(res, 0, 81, solution);
    return res;
}

extern "C" JNIEXPORT jbooleanArray

JNICALL Java_holgus103_visualsudokusolver_SolveByHand_checkSudoku(JNIEnv * env, jclass cls, jintArray sudoku) {

    auto res = env->NewBooleanArray(SUDOKU_SIZE*SUDOKU_SIZE);
    auto arr = env->GetIntArrayElements(sudoku, NULL);
    Sudoku puzzle = Sudoku();
    auto validityMap = puzzle.IsValidDetail(arr);
//    jboolean jni_validityMap[SUDOKU_SIZE*SUDOKU_SIZE];
//    for(auto i = 0; i< SUDOKU_SIZE* SUDOKU_SIZE; i++){
//        jni_validityMap[i] = (jboolean)
//    }
//    for(auto i = 0; i < SUDOKU_SIZE * SUDOKU_SIZE; i++){
        env->SetBooleanArrayRegion(res, 0, 81, (jboolean*)validityMap);
//    }
    delete validityMap;
    return res;
}





