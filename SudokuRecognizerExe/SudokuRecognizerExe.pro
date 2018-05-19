TEMPLATE = app
CONFIG += console c++11
CONFIG -= app_bundle
CONFIG -= qt


INCLUDEPATH += /usr/include/opencv
LIBS += -L/usr/lib64 -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lopencv_ml -lopencv_imgproc
SOURCES += \
        main.cpp \
    DigitRecognizer.cpp

HEADERS += \
    DigitResognizer.h \
    globals.h
