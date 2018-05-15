/*
 * File:   Sudoku.h
 * Author: holgus103
 *
 * Created on 17 avril 2018, 00:21
 */
#include <memory>
#ifndef SUDOKU_H
#define SUDOKU_H

#define SUDOKU_SIZE 9

class Sudoku {
public:
    Sudoku(int* sudoku);
    Sudoku();
    virtual ~Sudoku();
    int* getResult();
    int* Solve();
    bool IsValid();
    bool IsValid(int* sudoku);
    bool* IsValidDetail(int* sudoku);
private:
    int* sudoku;
    int* result;
    bool isValid;
    bool checkRow(int elNum, int val);
    bool checkColumn(int elNum, int val);
    bool isEmpty(int elementNumber);
    bool checkSquare(int elNum, int val);
    bool checkConstraints(int elNum, int val);
    bool isValidDetail(int* sudoku, bool* details);


};

#endif /* SUDOKU_H */

