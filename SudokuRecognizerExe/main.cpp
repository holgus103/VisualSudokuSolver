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
#include <features2d.hpp>
#include <vector>
#include <algorithm>

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

int main(int argc, char** argv) {
    auto r = new DigitRecognizer();
    r->train("train-images-idx3-ubyte", "train-labels-idx1-ubyte");
    // load image 
    auto image = imread("sudoku-original.jpg", 0);
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
    
//    for(int i = 0; i < curve.size(); i++){
//        circle(image, curve[i], 5, Scalar(255));
//    }
   
    auto persp = Mat(Size(252, 252), CV_8UC1);
    Point2f dstPoints[] = {Point2f(0,0), Point2f(0, 251), Point2f(251, 0), Point2f(251, 251)};
    sortPoints(curve);
    Point2f srcPoints[] = {curve[0], curve[1], curve[2], curve[3]};
    warpPerspective(image, persp, getPerspectiveTransform(srcPoints, dstPoints), Size(251, 251));
    show(persp);
    Mat d = persp(Rect(0, 28, 28, 28));
    r->classify(d);
    show(d);
    return 0;
}

