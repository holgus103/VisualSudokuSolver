/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   DigitResognizer.cpp
 * Author: holgus103
 * 
 * Created on 13 avril 2018, 21:19
 */

#include <stdio.h>
#include <opencv2/core/hal/interface.h>
#include <opencv2/ml.hpp>

#include "DigitResognizer.h"

using namespace cv;

DigitRecognizer::DigitRecognizer() {
}

DigitRecognizer::DigitRecognizer(const DigitRecognizer& orig) {
}

DigitRecognizer::~DigitRecognizer() {
//    delete this->knn
}

int DigitRecognizer::flipInt(int v){
    int res = 0; 
    res |= (v >> 24);
    res |= (0xff00 & (v >> 8));
    res |= (0xff0000 & (v << 8));
    res |= (0xff000000 & (v << 24));
    return res;    
}

//cv::Mat DigitRecognizer::classify(cv::Mat i){
//    auto response = cv::Mat(1, 1, CV_32FC1);
//    try{
//        auto sample = cv::Mat(i.size(), CV_32FC1);
//        i.convertTo(sample, CV_32FC1);
//        auto input = sample.reshape(0, 1);
//        knn->findNearest(input, 2, response);
//    }
//    catch(cv::Exception ex){
//        std::cout << ex.what();
//    }
//        return response;
//}

void DigitRecognizer::train(char* data, char* labels){
    
    Ptr<cv::ml::KNearest> k = cv::ml::KNearest::create();
    FILE *fData = fopen(data, "rb");
    FILE *fLabels = fopen(labels, "rb");
    
    int count, labelCount;
    int msb, magic;
    int rows, cols;
    
    fread(&msb, sizeof(int), 1, fLabels);
    fread(&labelCount, sizeof(int), 1, fLabels);
    
    fread(&magic, sizeof(int), 1, fData);
    fread(&count, sizeof(int), 1, fData);
    fread(&rows, sizeof(int), 1, fData);
    fread(&cols, sizeof(int), 1, fData);    
    
    count = this->flipInt(count);
    magic = this->flipInt(magic);
    rows = this->flipInt(rows);
    cols = this->flipInt(cols);
    msb = this->flipInt(msb);
    labelCount = this->flipInt(labelCount);
    
    int size = rows * cols;
    
           
    
    if(msb != 2049 || count != labelCount || magic != 2051){
        return;
    }
    
    
    Mat_<float> trainingVectors = Mat_<float>(count, size);

    Mat_<int> trainingClasses = Mat_<int>(1, count);


    char* tmp = new char[size];
    char cls=0; 
    for(int i = 0; i < count; i++){
        
        fread(tmp, size, 1, fData);
        fread(&cls, sizeof(char), 1, fLabels);
        
        trainingClasses.at<int>(0, i) = (int)cls;
        
        for(int j = 0; j < size; j++){
            trainingVectors.at<float>(i, j) = (float)tmp[j];
        }
    }

    k->train(trainingVectors, ml::ROW_SAMPLE, trainingClasses);
    knn = k;
// testing code
//    Mat res;
//    Mat_<float> sample = Mat_<float>(1, size);
//
//    for(int i = 0; i<size; i++){
//        sample.at<float>(0, i) = trainingVectors.at<float>(0, i);
//    }
//    try{
//        k->findNearest(sample, 10, res);
//    }
//    catch(cv::Exception ex){
//        std::cout << ex.what();
//    }
    fclose(fData);
    fclose(fLabels);  
    
    
    
}
