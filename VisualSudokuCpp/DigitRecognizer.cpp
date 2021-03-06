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

int DigitRecognizer::classify(cv::Mat input){
    Mat response;
    try{
        auto width = input.size().width;
        auto height = input.size().height;
        auto sample = cv::Mat_<float>(1, height * width);
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                sample.at<float>(0, height*j + width) = input.at<float>(i, j);
            }
        }
        
        knn->findNearest(sample, NEIGHBOURS_COUNT, response);
    }
    catch(cv::Exception ex){
        std::cout << ex.what();
    }
    
    return response.at<int>(0,0);
}

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
    
           
    
    if(msb != MSB || count != labelCount || magic != MAGIC_NUMBER){
        return;
    }
    
    
    Mat_<float> trainingVectors = Mat_<float>(count, size);

    Mat_<int> trainingClasses = Mat_<int>(1, count);


    char* tmp = new char[size];
    char cls=0; 
    for(int i = 0; i < count; i++){
        
        fread(tmp, size, 1, fData);
        fread(&cls, sizeof(char), 1, fLabels);
        
#ifdef DEBUG
        if(i == 0){
            std::cout << cls;
        }
#endif
        
        trainingClasses.at<int>(0, i) = (int)cls;
        
        for(int j = 0; j < size; j++){
            trainingVectors.at<float>(i, j) = (float)tmp[j];
        }
    }

    k->train(trainingVectors, ml::ROW_SAMPLE, trainingClasses);
    knn = k;
    
#ifdef WORKING_SAMPLE
    Mat res;
    Mat_<float> sample = Mat_<float>(1, size);

    for(int i = 0; i<size; i++){
        sample.at<float>(0, i) = trainingVectors.at<float>(0, i);
    }
    try{
        k->findNearest(sample, 10, res);
    }
    catch(cv::Exception ex){
        std::cout << ex.what();
    }
#endif
    
    fclose(fData);
    fclose(fLabels);  
    
    
    
}
