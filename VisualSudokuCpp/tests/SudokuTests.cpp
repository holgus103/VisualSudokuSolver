/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   SudokuTests.cpp
 * Author: holgus103
 *
 * Created on 22 avr. 2018, 22:18:16
 */

#include "SudokuTests.h"
#include "../Sudoku.h"


CPPUNIT_TEST_SUITE_REGISTRATION(SudokuTests);

SudokuTests::SudokuTests() {
}

SudokuTests::~SudokuTests() {
}

void SudokuTests::setUp() {
}

void SudokuTests::tearDown() {
}

void SudokuTests::testSolveEasy() {
    
    int array[] = {
                0,0,0, 0,0,0, 4,8,3,
                0,0,0, 0,0,0, 2,7,9,
                0,0,0, 0,0,0, 5,1,6,

                5,8,2, 3,6,7, 1,9,4,
                1,4,9, 2,5,8, 3,6,7,
                7,6,3, 1,4,9, 8,2,5,

                2,3,8, 7,9,4, 6,5,1,
                6,1,7, 8,3,5, 9,4,2,
                4,9,5, 6,1,2, 7,3,8
    };
    Sudoku sudoku = Sudoku(array);
    int* result = sudoku.Solve();
    if (true /*check result*/) {
        CPPUNIT_ASSERT(sudoku.IsValid());
    }
}

void SudokuTests::testSolveMedium() {
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
    Sudoku sudoku = Sudoku(array);
    int* result = sudoku.Solve();
    if (true /*check result*/) {
        CPPUNIT_ASSERT(sudoku.IsValid());
    }
}
    