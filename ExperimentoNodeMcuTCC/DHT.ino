#include <DHT.h>

DHT dht(DHT_PIN, DHT_TYPE);

void initDHT() {
  dht.begin();

  temperature = 0.0;
  humidity = 0.0;
}

void readDHT() {
  float temperatureReading = dht.readTemperature();
  float humidityReading = dht.readHumidity();

  if (!isnan(temperatureReading)) {
    temperature = temperatureReading;
  }

  if (!isnan(humidityReading)) {
    humidity = humidityReading;
  }
}

