    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */

    /* 
     * File:   Sudoku.cpp
     * Author: holgus103
     * 
     * Created on 17 avril 2018, 00:21
     */

    #include "Sudoku.h"
    #define SUDOKU_SIZE 9



    Sudoku::~Sudoku() {
    }


    int* Sudoku::getResult()
    {
            if (this->isValid)
            {
                if(this->result == NULL)
                {
                    this->Solve();
                }
                return result;
            }
            return NULL;
    }

    bool Sudoku::checkRow(int elNum, int val = 0)
    {
        val = val > 0 ? val : this->result[elNum];
        if (val == 0)
        {
            return true;
        }
        int start = (elNum / 9) * 9;
        int end = start + 9;
        for (int i = start; i < end; i++)
        {
            if (i == elNum)
                continue;
            if (this->result[i] == val)
                return false;
        }
        return true;
    }

    bool Sudoku::checkColumn(int elNum, int val = 0)
    {
        val = val > 0 ? val : this->result[elNum];
        if (val == 0)
        {
            return true;
        }
        int start = elNum % 9;
        int end = start + 72;
        for (int i = start; i <= end; i += 9)
        {
            if (i == elNum)
                continue;
            if (this->result[i] == val)
                return false;
        }
        return true;
    }

    bool Sudoku::isEmpty(int elementNumber)
    {
        if (this->sudoku[elementNumber] == 0)
            return true;
        else
            return false;
    }


    bool Sudoku::checkSquare(int elNum, int val = 0)
    {
        val = val > 0 ? val : this->result[elNum];
        if (val == 0)
        {
            return true;
        }
        int rowNumber = elNum / 9;
        int colNumber = elNum % 9;
        int squareX = colNumber / 3;
        int squareY = rowNumber / 3;
        int index;
        int start = 27 * squareY + squareX * 3;

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                index = start + i * 9 + j;
                if (index == elNum)
                    continue;
                if (this->result[index] == val)
                    return false;
            }
        }
        return true;
    }

    bool Sudoku::checkConstraints(int elNum, int val = 0)
    {
        return this->checkColumn(elNum, val) && this->checkRow(elNum, val) && this->checkSquare(elNum, val);
    }

    Sudoku::Sudoku(int* sudoku)
    {
        this->sudoku = sudoku;
        this->isValid = this->IsValid(this->sudoku);
    }

    int* Sudoku::Solve()
    {
        if(this-> result != NULL) return this->result;
        this->result = new int[SUDOKU_SIZE * SUDOKU_SIZE];
        for(auto i=0; i < SUDOKU_SIZE * SUDOKU_SIZE; i++){
            this->result[i] = this->sudoku[i];
        }
        int correctPositions = 0;
        int currentPosition = 0;
        while (correctPositions < SUDOKU_SIZE * SUDOKU_SIZE)
        {
            if (this->isEmpty(currentPosition))
            {
                bool placedSuccessfully = false;
                for (int i = result[currentPosition] + 1; i < 10; i++)
                {
                    if (this->checkConstraints(currentPosition, i))
                    {
                        this->result[currentPosition] = i;
                        currentPosition++;
                        correctPositions++;
                        placedSuccessfully = true;
                        break;
                    }
                }
                if (!placedSuccessfully)
                {
                    bool isEmpty = true;
                    do
                    {
                        if (isEmpty)
                            this->result[currentPosition] = 0;
                        currentPosition--;
                        correctPositions--;
                        isEmpty = this->isEmpty(currentPosition);
                    }
                    while (!isEmpty || this->result[currentPosition] > 8);
                }
            }
            else
            {
                currentPosition++;
                correctPositions++;
            }

        }
        return this->result;

    }

    bool Sudoku::IsValid()
    {
        return this->isValid;
    }

    bool Sudoku::IsValid(int* sudoku)
    {
        this->isValidDetail(sudoku, NULL);
    }

    bool Sudoku::isValidDetail(int* sudoku, bool* details)
    {
        int* temp = result;
        bool res = true;
        this->result = sudoku;
        for (int i = 0; i < 81; i++)
        {
            if (!this->checkConstraints(i))
            {
                res = false;
                if(details == NULL){
                    this->result = temp;
                    return false;
                }
                else{
                    details[i] = false;
                }
            }
            else{
                if(details != NULL){
                    details[i] = true;
                }
            }
        }
        this->result = temp;
        return res;
    }

    bool* Sudoku::IsValidDetail(int* sudoku)
    {   
        bool* details = new bool[SUDOKU_SIZE* SUDOKU_SIZE];
        this->isValidDetail(sudoku, details);
        return details;
    }