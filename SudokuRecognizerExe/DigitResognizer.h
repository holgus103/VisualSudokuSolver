/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   DigitResognizer.h
 * Author: holgus103
 *
 * Created on 13 avril 2018, 21:19
 */

#ifndef DIGITRESOGNIZER_H
#define DIGITRESOGNIZER_H


#include <opencv2/core/core.hpp>
#include <ml.h>
#include <vector>

class DigitRecognizer {
public:
    DigitRecognizer();
    DigitRecognizer(char* path);
    DigitRecognizer(const DigitRecognizer& orig);
    virtual ~DigitRecognizer();
    void train(char*, char*);
    cv::Mat classify(cv::Mat);
    void serialize();
private:
    cv::ml::KNearest *knn;
    int flipInt(int);
    
};

#endif /* DIGITRESOGNIZER_H */

