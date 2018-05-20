//
// Created by holgus103 on 19/05/18.
//


#include <opencv2/core/core.hpp>
#include <opencv2/ml.hpp>
#include <vector>
#include "globals.h"

class DigitRecognizer {
public:
    DigitRecognizer();
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
