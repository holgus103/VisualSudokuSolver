/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Sudoku.h
 * Author: holgus103
 *
 * Created on 17 avril 2018, 00:21
 */
#include <memory>
#ifndef SUDOKU_H
#define SUDOKU_H

class Sudoku {
public:
    Sudoku(int* sudoku);
    virtual ~Sudoku();
    int* getResult();
    int* Solve();
    bool IsValid();
    bool IsValid(int* sudoku);
private:
    int* sudoku;
    int* result;
    bool isValid;
    bool checkRow(int elNum, int val);
    bool checkColumn(int elNum, int val);
    bool isEmpty(int elementNumber);
    bool checkSquare(int elNum, int val);
    bool checkConstraints(int elNum, int val);
    

};

#endif /* SUDOKU_H */

