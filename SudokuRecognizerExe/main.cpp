// reference: http://aishack.in/tutorials/sudoku-grabber-opencv-detection/

/* 
 * File:   main.cpp
 * Author: holgus103
 *
 * Created on 30 mars 2018, 02:23
 */
//#include <cv.h>
//#include <highgui.h>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <vector>
#include <algorithm>
#include "globals.h"
#include <fstream>
#include "DigitResognizer.h"


using namespace std;
using namespace cv;

/*
 * 
 */
bool xCompare(Point a, Point b){
    return a.x < b.x;
}

void swap(vector<Point>& v, int src, int dst){
    Point tmp; 
    tmp = v[src];
    v[src] = v[dst];
    v[dst] = tmp;
}

void sortPoints(vector<Point>& v){
    std::sort(v.begin(), v.end(), xCompare);
    Point tmp;
    if(v[0].y > v[1].y){
        // swap
        swap(v, 0, 1);
    }
    
    if(v[2].y > v[3].y){
        swap(v, 2, 3);          
    }

    
}

void show(Mat v) {
    imshow("Display window", v);
    waitKey(0);
    destroyAllWindows();
}

bool areaCompare(std::vector<Point> a, std::vector<Point> b) {
//    return a.size() > b.size();
    return contourArea(a) > contourArea(b);
}

void extractDigitImages(std::string name, std::vector<Mat>& digits, std::vector<int>& labels, bool extractLabels){
    if(extractLabels){
        std::fstream fLabels = std::fstream(name.substr(0, name.length() - 1) + ".txt", std::ios_base::in);
        int lab = 0;
        for(auto i = 0; i < SUDOKU_SIZE * SUDOKU_SIZE; i++){
            fLabels >> lab;
            labels.push_back(lab);
        }
    }
    // load image 
    auto image = imread(name + ".jpg", 0);
//    show(image);
    // create empty image
    auto box = Mat(image.size(), CV_8UC1);
    // smoothout the noise
    GaussianBlur(image, image, Size(11, 11), 0);
    // thresholding
    adaptiveThreshold(image, box, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 2);
    // negate image
    bitwise_not(box, box);
    // fill cracks
    Mat kernel = (Mat_<uchar>(3, 3) << 0, 1, 0, 1, 1, 1, 0, 1, 0);
    dilate(box, box, kernel);

    vector<vector<Point>> contours;
    // find contours
    cv::findContours(box, contours, RETR_LIST, CHAIN_APPROX_SIMPLE);
    // get biggest contour
    std::sort(contours.begin(), contours.end(), areaCompare);

    vector<Point> curve;
    vector<vector<Point>> c;
    // simplify contour
    approxPolyDP(contours[0], curve, 0.1*arcLength(contours[0], true), true);
    c.push_back(curve);
    drawContours(box, c, 0, Scalar(255), CV_FILLED);
    
//    Mat t;
//    cv::resize(box, t, cv::Size(image.cols * 0.2,image.rows * 0.2));
//    show(t);
//    return;

    auto persp = Mat(Size(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE), CV_8UC1);
    Point2f dstPoints[] = {
        Point2f(0,0),
        Point2f(0, TARGET_SQUARE_SIZE*SUDOKU_SIZE),
        Point2f(TARGET_SQUARE_SIZE*SUDOKU_SIZE, 0),
        Point2f(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE)
    };
    
    sortPoints(curve);
    Point2f srcPoints[] = {curve[0], curve[1], curve[2], curve[3]};
    warpPerspective(image, persp, getPerspectiveTransform(srcPoints, dstPoints), Size(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE));
    
#ifdef DISPLAY_PROGRESS
    show(persp);
#endif
    
    for(auto i = 0; i < SUDOKU_SIZE; i++){
        for(auto j = 0; j < SUDOKU_SIZE; j++){
            
#ifdef DISPLAY_PROGRESS
            show(persp(Rect(j*TARGET_SQUARE_SIZE, i*TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE)));         
#endif
            auto digit = persp(Rect(j*TARGET_SQUARE_SIZE, i*TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE));
            auto output = Mat(TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE, CV_32FC1);
            digit.convertTo(output, CV_32FC1);
            output = output.reshape(0, 1);
            digits.push_back(output);
        }
    }
} 

int main(int argc, char** argv) {
    
    std::string fileNames[] = {
        "./../images/sudoku_1a",
        "./../images/sudoku_1b",
        "./../images/sudoku_1c",
        "./../images/sudoku_2a",
        "./../images/sudoku_2b",
        "./../images/sudoku_3a",
        "./../images/sudoku_3b",
        "./../images/sudoku_4a",
        "./../images/sudoku_5a",
        "./../images/sudoku_5b",
        "./../images/sudoku_5c",
        "./../images/sudoku_6b",
        "./../images/sudoku_6c",
        "./../images/sudoku_7a",
        "./../images/sudoku_7b",
        "./../images/sudoku_7d",
        "./../images/sudoku_8a",
        "./../images/sudoku_8c",
        "./../images/sudoku_9a",
        "./../images/sudoku_9c",
        "./../images/sudoku_9d",
        "./../images/sudoku_9e",
        "./../images/sudoku_10a",
        "./../images/sudoku_10b",
        "./../images/sudoku_11a",
        "./../images/sudoku_11b",
        "./../images/sudoku_12a",
        "./../images/sudoku_12b",
        "./../images/sudoku_12c",
        "./../images/sudoku_12d",
        "./../images/sudoku_13a",
        "./../images/sudoku_15a",
        "./../images/sudoku_16a",
        "./../images/sudoku_17a",
        "./../images/sudoku_19a"
    };
    auto digits = std::vector<Mat>();
    auto labels = std::vector<int>();
    int max = sizeof(fileNames)/ sizeof(*fileNames);
    for(auto i = 0; i < max; i++){
        extractDigitImages(fileNames[i], digits, labels, true);
    }
//    return 0;
//    int result[SUDOKU_SIZE][SUDOKU_SIZE];
    auto r = new DigitRecognizer();
    r->train(digits, labels);
    auto test = std::vector<Mat>();
    auto testResults = std::vector<int>();
    int correct = 0;
    extractDigitImages("./../images/sudoku_20a", test, testResults, true);
    for(auto i = 0; i < test.size(); i++){
        auto val = r->classify(test[i]);
        if(abs(val - testResults[i]) < 0.1)
            correct++;
    }

    std::cout << (float) correct / (float)test.size() << std::endl;


}

