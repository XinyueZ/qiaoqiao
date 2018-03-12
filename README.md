# ViApp with Tensorflow-Lite

- Use Tensorflow-Lite to classify camera frames.
- This is a standalone branch, it won't be merged in to [https://github.com/XinyueZ/qiaoqiao/tree/feature/dev](dev).
- An Android-app that gives detected information about live shooting, based on Google AI.
- Based on Google's [Vision API](https://cloud.google.com/vision/).

# Task of this branch

- Using and learning how to integrate Tensorflow into mobile project.
- Learning and training models, converting into Tensorflow-Lite mobile.
- Evaluating ```offline``` image-classify for the ViApp.   
- Evaluating a solution that the ViApp supports image-classify with(out) [Vision API](https://cloud.google.com/vision/). 

![sample](media/tf-lite-sample.gif)


# CI and Checkout     
    
[![Build Status](https://travis-ci.org/XinyueZ/qiaoqiao.svg?branch=master)](https://travis-ci.org/XinyueZ/qiaoqiao)
 
- Compile ```./gradlew clean assembleDebug```

- Build APK: Because I have ignored config of Play Service, you must use own one if checkout this project.
    - Dev version ```app/src/dev/google-services.json```
    - Live version ```app/src/live/google-services.json```
    - Complete ```buildtools/release.json``` for a release version.
    - Take care in ```strings.xml```, when use own ```google-services.json```
        -     <!--<string name="web_client_id">@string/default_web_client_id</string>-->
              <string name="web_client_id">[FIXME: Here use @string/default_web_client_id instead]</string>
        - Comment out ```gradle.startParameter.excludedTaskNames``` in ```build.gradle```
            
        

# License

``` 
                            MIT License

                Copyright (c) 2018 Chris Xinyue 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
