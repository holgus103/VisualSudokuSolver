/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   SudokuTests.h
 * Author: holgus103
 *
 * Created on 22 avr. 2018, 22:18:15
 */

#ifndef SUDOKUTESTS_H
#define SUDOKUTESTS_H

#include <cppunit/extensions/HelperMacros.h>

class SudokuTests : public CPPUNIT_NS::TestFixture {
    CPPUNIT_TEST_SUITE(SudokuTests);

    CPPUNIT_TEST(testSolveEasy);
    CPPUNIT_TEST(testSolveMedium);


    CPPUNIT_TEST_SUITE_END();

public:
    SudokuTests();
    virtual ~SudokuTests();
    void setUp();
    void tearDown();

private:
    void testSolveEasy();
    void testSolveMedium();

};

#endif /* SUDOKUTESTS_H */

