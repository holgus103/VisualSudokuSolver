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
    this->knn = cv::ml::KNearest::create();
}

DigitRecognizer::DigitRecognizer(std::string path){
    this->knn = cv::ml::KNearest::load<cv::ml::KNearest>(path);
}


DigitRecognizer::~DigitRecognizer() {
    delete this->knn;
}

void DigitRecognizer::saveClassifier(std::string path){
    this->knn->save(path);
}

int DigitRecognizer::flipInt(int v){
    int res = 0; 
    res |= (v >> 24);
    res |= (0xff00 & (v >> 8));
    res |= (0xff0000 & (v << 8));
    res |= (0xff000000 & (v << 24));
    return res;    
}

float DigitRecognizer::classify(cv::Mat input){
    Mat response;        
    knn->findNearest(input, NEIGHBOURS_COUNT, response);
    
    return response.at<float>(0,0);
}

void DigitRecognizer::train(std::vector<Mat> data, std::vector<int> labels){

    Mat_<float> trainingSamples = Mat_<float>(data.size(), TARGET_SQUARE_SIZE*TARGET_SQUARE_SIZE);
    Mat_<int> trainingLabels = Mat_<int>(1, labels.size());

    for(auto i = 0; i < data.size(); i++){
        for(auto j = 0; j < TARGET_SQUARE_SIZE * TARGET_SQUARE_SIZE; j++){
            trainingSamples.at<float>(i, j) = data[i].at<float>(0, j);
        }
        trainingLabels.at<int>(0, i) = labels[i];
    }

    try{
        this->knn->train(trainingSamples, ml::ROW_SAMPLE, trainingLabels);
    }
    catch(cv::Exception ex){
        auto msg = ex.msg;
        std::cout << ex.msg << std::endl;
    }
    
#ifdef WORKING_SAMPLE
    int correct = 0;
    for(int i = 0; i < data.size(); i++){
        Mat res;
        Mat_<float> sample = Mat_<float>(1, TARGET_SQUARE_SIZE*TARGET_SQUARE_SIZE);

        for(int j = 0; j<TARGET_SQUARE_SIZE * TARGET_SQUARE_SIZE; j++){
            sample.at<float>(0, j) = trainingSamples.at<float>(i, j);
        }
        try{
            k->findNearest(sample, 1, res);
            std::cout << "result: " << res.at<float>(0, 0) << std::endl;
            std::cout << "expected: " << trainingLabels.at<int>(0, i) << std::endl;
        }
        catch(cv::Exception ex){
            std::cout << ex.what();
        }
        if(abs((float)trainingLabels.at<int>(0, i) - res.at<float>(0,0)) < 0.1){
            correct++;
        }
    }
    std::cout << (float)correct / (float)data.size();
#endif 
    
}
