//
// Created by holgus103 on 19/05/18.
//


#include <opencv2/highgui/highgui.hpp>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <vector>
#include <algorithm>
#include "globals.h"
#include <fstream>
#include "DigitRecognizer.h"

#ifndef VISUALSUDOKUSOLVER_HELPERS_H
#define VISUALSUDOKUSOLVER_HELPERS_H


bool xCompare(cv::Point a, cv::Point b);
void swap(std::vector<cv::Point>& v, int src, int dst);
void sortPoints(std::vector<cv::Point>& v);
void show(cv::Mat v);
bool areaCompare(std::vector<cv::Point> a, std::vector<cv::Point> b);
void extractDigitImages(std::string name, std::vector<cv::Mat>& digits, std::vector<int>& labels, bool extractLabels);

#endif //VISUALSUDOKUSOLVER_HELPERS_H
