// IFetchWeatherService.aidl
package com.example.assignment7;

import com.example.assignment7.IFetchDataListener;
// Declare any non-default types here with import statements

interface IFetchWeatherService {
    void retrieveWeatherData();
    void registerFetchDataListener(IFetchDataListener listener);
    void unregisterFetchDataListener(IFetchDataListener listener);

}
