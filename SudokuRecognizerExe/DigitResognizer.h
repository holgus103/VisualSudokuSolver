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
#include "globals.h"

class DigitRecognizer {
public:
    DigitRecognizer();
    DigitRecognizer(char* path);    
    DigitRecognizer(std::string path);
    virtual ~DigitRecognizer();
    void train(std::vector<cv::Mat> data, std::vector<int> labels);
    float classify(cv::Mat);
    void saveClassifier(std::string path);
    void serialize();
private:
    cv::Ptr<cv::ml::KNearest> knn;
    int flipInt(int);
    
};

#endif /* DIGITRESOGNIZER_H */

