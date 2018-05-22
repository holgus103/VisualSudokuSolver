//
// Created by holgus103 on 19/05/18.
//

#include "helpers.h"

#include <opencv2/highgui.hpp>
using namespace std;
using namespace cv;

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

void extractDigitImages(std::string name, std::vector<Mat>& digits){
    // load image
    auto image = imread(name, 0);
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
//    drawContours(box, c, 0, Scalar(255), CV_FILLED);

//    Mat t;
//    cv::resize(box, t, cv::Size(image.cols * 0.2,image.rows * 0.2));
//    show(t);

    auto persp = Mat(Size(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE), CV_8UC1);
    Point2f dstPoints[] = {
            Point2f(0,0),
            Point2f(0, TARGET_SQUARE_SIZE*SUDOKU_SIZE),
            Point2f(TARGET_SQUARE_SIZE*SUDOKU_SIZE, 0),
            Point2f(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE)
    };

    sortPoints(curve);
    Point2f srcPoints[] = {curve[0], curve[1], curve[2], curve[3]};
    warpPerspective(box, persp, getPerspectiveTransform(srcPoints, dstPoints), Size(TARGET_SQUARE_SIZE*SUDOKU_SIZE, TARGET_SQUARE_SIZE*SUDOKU_SIZE));
    // make white or black
    for(auto i = 0; i < TARGET_SQUARE_SIZE * SUDOKU_SIZE; i++){
        for(auto j = 0; j < TARGET_SQUARE_SIZE * SUDOKU_SIZE; j++){
            auto val = persp.at<uchar>(i, j);
            if(val > 127){
                persp.at<uchar>(i, j) = 255;
            }
            else{
                persp.at<uchar>(i, j) = 0;
            }
        }
    }
#ifdef DISPLAY_PROGRESS
    show(persp);
#endif

    for(auto i = 0; i < SUDOKU_SIZE; i++){
        for(auto j = 0; j < SUDOKU_SIZE; j++){
            // remove nasty edges
            auto digit = persp(Rect(j*TARGET_SQUARE_SIZE, i*TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE));
            for(auto k = 0; k < TARGET_SQUARE_SIZE; k++){
                floodFill(digit, Point(k, 0), Scalar(0));
                floodFill(digit, Point(k, TARGET_SQUARE_SIZE -1), Scalar(0));
                floodFill(digit, Point(0, k), Scalar(0));
                floodFill(digit, Point(TARGET_SQUARE_SIZE -1, k), Scalar(0));
            }
            int topY, leftX, rightX, bottomY;
            topY = leftX = rightX = bottomY = 0;
            // find top
            for(auto k = 0; k < TARGET_SQUARE_SIZE; k++){
                for(auto l = 0; l < TARGET_SQUARE_SIZE; l++){
                    auto val = digit.at<uchar>(k, l);
                    if(val > 0){
                        topY = k;
                    }
                }
            }
            // find left
            for(auto k = 0; k < TARGET_SQUARE_SIZE; k++){
                for(auto l = 0; l < TARGET_SQUARE_SIZE; l++){
                    auto val = digit.at<uchar>(l, k);
                    if(val > 0){
                        leftX = k;
                    }
                }
            }
            // find bottom
            for(auto k = TARGET_SQUARE_SIZE-1; k >=0; k--){
                for(auto l = 0; l < TARGET_SQUARE_SIZE; l++){
                    auto val = digit.at<uchar>(k, l);
                    if(val > 0){
                        bottomY = k;
                    }
                }
            }
            // find right
            for(auto k = TARGET_SQUARE_SIZE-1; k >= 0; k--){
                for(auto l = 0; l < TARGET_SQUARE_SIZE; l++){
                    auto val = digit.at<uchar>(l, k);
                    if(val > 0){
                        rightX = k;
                    }
                }
            }
            auto width = rightX - leftX;
            auto height = bottomY - topY;
            Point2f src[] = {
                    Point2f(rightX, topY),
                    Point2f(leftX, topY),
                    Point2f(rightX, bottomY),
                    Point2f(leftX, bottomY)
            };
            Point2f dst[] ={
                    Point2f(TARGET_SQUARE_SIZE-1, 0),
                    Point2f(0, 0),
                    Point2f(TARGET_SQUARE_SIZE-1, TARGET_SQUARE_SIZE-1),
                    Point2f(0, TARGET_SQUARE_SIZE-1)
            };
            Mat p_digit;
            warpPerspective(digit, p_digit, getPerspectiveTransform(src, dst), Size(TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE));


#ifdef DISPLAY_PROGRESS
            show(p_digit);
#endif
            auto output = Mat(TARGET_SQUARE_SIZE, TARGET_SQUARE_SIZE, CV_32FC1);
//            for(auto k = 0; k < TARGET_SQUARE_SIZE; k++){
//                for(auto l = 0; l < TARGET_SQUARE_SIZE; l++){
//                    auto val = p_digit.at<uchar>(k, l);
//                    if(val > 127){
//                        p_digit.at<uchar>(k, l) = 255;
//                    }
//                    else{
//                        p_digit.at<uchar>(k, l) = 0;
//                    }
//                }
//            }
            p_digit.convertTo(output, CV_32FC1);
            output = output.reshape(0, 1);
            digits.push_back(output);
        }
    }
}
